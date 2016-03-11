package cn.campusapp.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.campusapp.router.exception.InvalidRouteException;
import cn.campusapp.router.exception.InvalidRoutePathException;
import cn.campusapp.router.exception.InvalidValueTypeException;
import cn.campusapp.router.utils.UrlUtils;
import timber.log.Timber;

import static cn.campusapp.router.utils.UrlUtils.getPathSegments;

/**
 * Created by kris on 16/3/10.
 */
public class Router implements IRouter {
    static Router mSharedRouter = new Router();
    private static final String TAG = "Router";

    Context mBaseContext;
    Map<String, Class<? extends Activity>> mRouteTable = new HashMap<>();

    public static Router getSharedRouter(){
        return mSharedRouter;
    }



    @Override
    public void init(Context appContext, IRouterTableInitializer initializer) throws InvalidRoutePathException{
        mBaseContext = appContext;
        initializer.initRouterTable(mRouteTable);
        for(String pathRule : mRouteTable.keySet()){
            boolean isValid = RouteRuleBuilder.isRouteRuleValid(pathRule);
            if(!isValid){
                throw new InvalidRoutePathException(pathRule);
            }
        }
    }

    /**
     * not recommend
     * @param route
     */
    @Deprecated
    @Override
    public void open(Route route) {
        open(route, null);
    }

    @Override
    public void open(Route route, Context context) {
        try {
            checkRoute(route);
        } catch (InvalidRouteException e){
            Timber.e(e, "");
            return;
        }

        try {
            Intent intent = match(route);
            if(route.getInAnimation() != -1 && route.getOutAnimation() != -1 && route.getActivity() != null){
                route.getActivity().overridePendingTransition(route.getInAnimation(), route.getOutAnimation());
            }
            if(context == null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mBaseContext.startActivity(intent);
            } else {
                context.startActivity(intent);
            }
        } catch (Exception e){
            Timber.e(e, "");
        }
    }

    @Override
    public void openForResult(Route route, Activity activity, int requestCode) {
        try {
            checkRoute(route);
        } catch (InvalidRouteException e){
            Timber.e(e, "");
            return;
        }

        try {
            Intent intent = match(route);
            if(route.getInAnimation() != -1 && route.getOutAnimation() != -1 && route.getActivity() != null){
                route.getActivity().overridePendingTransition(route.getInAnimation(), route.getOutAnimation());
            }
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e){
            Timber.e(e, "");
        }
    }

    @Override
    public void openForResult(Route route, Fragment fragment, int requestCode) {
        try {
            checkRoute(route);
        } catch (InvalidRouteException e){
            Timber.e(e, "");
            return;
        }

        try {
            Intent intent = match(route);
            if(route.getInAnimation() != -1 && route.getOutAnimation() != -1 && route.getActivity() != null){
                route.getActivity().overridePendingTransition(route.getInAnimation(), route.getOutAnimation());
            }
            fragment.startActivityForResult(intent, requestCode);
        } catch (Exception e){
            Timber.e(e, "");
        }
    }

    @Override
    public void openForResult(Route route, android.app.Fragment fragment, int requestCode) {
        try {
            checkRoute(route);
        } catch (InvalidRouteException e){
            Timber.e(e, "");
            return;
        }

        try {
            Intent intent = match(route);
            if(route.getInAnimation() != -1 && route.getOutAnimation() != -1 && route.getActivity() != null){
                route.getActivity().overridePendingTransition(route.getInAnimation(), route.getOutAnimation());
            }
            fragment.startActivityForResult(intent, requestCode);
        } catch (Exception e){
            Timber.e(e, "");
        }
    }


    private void checkRoute(Route route) throws InvalidRouteException{
        if(!Route.isTheRouteValid(route)){
            throw  new InvalidRouteException();
        }
    }



    /**
     *
     * @param route
     * @return String the match routePath
     */
    private String findMatchedPath(Route route) {
        List<String> givenPathSegs = getPathSegments(route.getPath());
        OutLoop:
        for(String routePath : mRouteTable.keySet()){
            List<String> routePathSegs = getPathSegments(routePath);
            if(givenPathSegs.size() != routePathSegs.size()){
                continue;
            }
            for(int i=0;i<routePathSegs.size();i++){
                if(!routePathSegs.get(i).startsWith(":")
                        &&!TextUtils.equals(routePathSegs.get(i), givenPathSegs.get(i))) {
                    continue OutLoop;
                }
            }
            //find the match route
            return routePath;
        }

        return null;
    }

    /**
     * find the key value in the path and set them in the intent
     * @param routePath the matched route path
     * @param givenPath the given path
     * @param intent the intent
     * @return the intent
     */
    private Intent setKeyValueInThePath(String routePath, String givenPath, Intent intent) throws InvalidValueTypeException{
        List<String> routePathSegs = getPathSegments(routePath);
        List<String> givenPathSegs = getPathSegments(givenPath);
        for(int i = 0;i<routePathSegs.size();i++){
            //路由规则的判断在编译器已经做过，所以这里我们认为路由都是符合规范的，就不做一些对规则的判断了
            String seg = routePathSegs.get(i);
            if(seg.startsWith(":")){
                int indexOfLeft = seg.indexOf("{");
                int indexOfRight = seg.indexOf("}");
                String key = seg.substring(indexOfLeft + 1, indexOfRight);
                char typeChar = seg.charAt(1);
                switch (typeChar){
                    //interger type
                    case 'i':
                        try {
                            int value = Integer.parseInt(givenPathSegs.get(i));
                            intent.putExtra(key, value);
                        } catch(Exception e){
                            Log.e(TAG, "解析整形类型失败 "+ givenPathSegs.get(i), e);
                            if(BuildConfig.DEBUG){
                                throw new InvalidValueTypeException(givenPath, givenPathSegs.get(i));
                            } else{
                                //如果是在release情况下则给一个默认值
                                intent.putExtra(key, 0);
                            }
                        }
                        break;
                    case 'f':
                        //float type
                        try {
                            float value = Float.parseFloat(givenPathSegs.get(i));
                            intent.putExtra(key, value);
                        } catch(Exception e){
                            Log.e(TAG, "解析浮点类型失败 " + givenPathSegs.get(i), e);
                            if(BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenPath, givenPathSegs.get(i));
                            } else {
                                intent.putExtra(key, 0f);
                            }
                        }
                        break;
                    case 'l':
                        //long type
                        try{
                            long value = Long.parseLong(givenPathSegs.get(i));
                            intent.putExtra(key, value);
                        } catch(Exception e){
                            Log.e(TAG, "解析长整形失败 " + givenPathSegs.get(i), e);
                            if(BuildConfig.DEBUG){
                                throw new InvalidValueTypeException(givenPath, givenPathSegs.get(i));
                            } else {
                                intent.putExtra(key, 0l);
                            }
                        }
                        break;
                    case 'd':
                        try{
                            double value = Double.parseDouble(givenPathSegs.get(i));
                            intent.putExtra(key, value);
                        } catch (Exception e){
                            Log.e(TAG, "解析double类型失败 " + givenPathSegs.get(i), e);
                            if(BuildConfig.DEBUG){
                                throw new InvalidValueTypeException(givenPath, givenPathSegs.get(i));
                            } else {
                                intent.putExtra(key, 0d);
                            }
                        }
                        break;
                    case 'c':
                        try {
                            char value = givenPathSegs.get(i).charAt(0);
                        } catch(Exception e){
                            Log.e(TAG, "解析Character类型失败" + givenPathSegs.get(i), e);
                            if(BuildConfig.DEBUG){
                                throw new InvalidValueTypeException(givenPath, givenPathSegs.get(i));
                            } else {
                                intent.putExtra(key, ' ');
                            }
                        }
                        break;
                    case 's':
                    default:
                        intent.putExtra(key, givenPathSegs.get(i));
                }
            }

        }
        return intent;
    }

    private Intent setOptionParams(String path, Intent intent){
        Map<String, String> queryParams = UrlUtils.getOptionParams(path);
        for(String key: queryParams.keySet()){
            intent.putExtra(key, queryParams.get(key));
        }

        return intent;
    }

    private Intent setExtras(Bundle bundle, Intent intent){
        intent.putExtras(bundle);
        return intent;
    }

    private Intent match(Route route) throws InvalidValueTypeException {
        String matchedPath = findMatchedPath(route);
        if(matchedPath == null){
            return null;
        }
        Class<? extends Activity> matchedActivity = mRouteTable.get(matchedPath);
        Intent intent = new Intent(mBaseContext, matchedActivity);
        //find the key value in the path
        intent = setKeyValueInThePath(matchedPath, route.getPath(), intent);
        intent = setOptionParams(route.getPath(), intent);
        intent = setExtras(route.getExtras(), intent);
        return intent;
    }


}
