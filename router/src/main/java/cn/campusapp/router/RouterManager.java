package cn.campusapp.router;

import android.content.Context;
import android.support.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.campusapp.router.route.IRoute;
import cn.campusapp.router.router.ActivityRouter;
import cn.campusapp.router.router.BrowserRouter;
import cn.campusapp.router.router.HistoryItem;
import cn.campusapp.router.router.IActivityRouteTableInitializer;
import cn.campusapp.router.router.IRouter;
import timber.log.Timber;

/**
 * Created by kris on 16/3/17.
 *
 * router 应该是个单例
 */
public class RouterManager {

    private static final RouterManager singleton = new RouterManager();

    //注意这是个list是有顺序的，所以排在前面的优先级会比较高
    static List<IRouter> mRouters = new LinkedList<>();
    static ActivityRouter mActivityRouter = new ActivityRouter();   //Activity
    static BrowserRouter mBrowserRouter = new BrowserRouter();  //浏览器


    static {
        try {
            Constructor<?> constructor =  Class.forName("cn.campusapp.router.router.AnnotatedRouterTableInitializer").getConstructor();
            IActivityRouteTableInitializer initializer = (IActivityRouteTableInitializer) constructor.newInstance();
            mActivityRouter.initActivityRouterTable(initializer);

        } catch (Exception e) {
            //do nothing
        }
    }


    private RouterManager(){}

    static RouterManager getSingleton(){
        return singleton;
    }


    public synchronized void addRouter(IRouter router){
        if(router != null){
            //first remove all the duplicate routers
            List<IRouter> duplicateRouters = new ArrayList<>();
            for(IRouter r : mRouters){
                if(r.getClass().equals(router.getClass())){
                    duplicateRouters.add(r);
                }
            }
            mRouters.removeAll(duplicateRouters);
            mRouters.add(router);
        } else {
            Timber.e(new NullPointerException("The Router" +
                    "is null" +
                    ""), "");
        }
    }

    public synchronized void initBrowserRouter(Context context){
        mBrowserRouter.init(context);
        addRouter(mBrowserRouter);
    }


    public synchronized void initActivityRouter(Context context){
        mActivityRouter.init(context);
        addRouter(mActivityRouter);
    }

    public synchronized void initActivityRouter(Context context, String ... schemes){
        initActivityRouter(context, null, schemes);
    }

    public synchronized void initActivityRouter(Context context, IActivityRouteTableInitializer initializer, String ... schemes){
        if(initializer == null) {
            mActivityRouter.init(context);
        } else {
            mActivityRouter.init(context, initializer);
        }
        if(schemes != null && schemes.length > 0){
            mActivityRouter.setMatchSchemes(schemes);
        }
        addRouter(mActivityRouter);
    }

    public List<IRouter> getRouters(){
        return mRouters;
    }


    public boolean open(String url){
        for(IRouter router : mRouters){
            if(router.canOpenTheUrl(url)){
                return router.open(url);
            }
        }
        return false;
    }

    /**
     * the route of the url, if there is not router to process the url, return null
     * @param url
     * @return
     */
    @Nullable
    public IRoute getRoute(String url){
        for(IRouter router : mRouters){
            if(router.canOpenTheUrl(url)){
                return router.getRoute(url);
            }
        }
        return null;
    }

    public boolean open(Context context, String url){
        for(IRouter router : mRouters){
            if(router.canOpenTheUrl(url)){
                return router.open(context, url);
            }
        }
        return false;
    }


    public boolean openRoute(IRoute route){
        for(IRouter router : mRouters){
            if(router.canOpenTheRoute(route)){
                return router.open(route);
            }
        }
        return false;
    }

    /**
     * set your own activity router
     */
    public void setActivityRouter(ActivityRouter router){
        addRouter(router);
    }

    /**
     * set your own BrowserRouter
     */
    public void setBrowserRouter(BrowserRouter router){
        addRouter(router);
    }


    public Queue<HistoryItem> getActivityChangedHistories(){
        ActivityRouter aRouter = null;
        for(IRouter router : mRouters){
            if(router instanceof ActivityRouter){
                aRouter = (ActivityRouter) router;
                break;
            }
        }
        return aRouter != null ? aRouter.getRouteHistories() : null;
    }


}
