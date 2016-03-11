package cn.campusapp.router;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import cn.campusapp.router.exception.InvalidRoutePathException;

/**
 * Created by kris on 16/3/10.
 */
public interface IRouter {

    /**
     * init the router
     * @param appContext the context of the application
     * @param routerTable the class to init the router table
     */
    void init(Context appContext, IRouterTableInitializer routerTable) throws InvalidRoutePathException;

    void open(Route route);

    void open(Route route, Context context);

    void openForResult(Route route, Activity activity, int requestCode);

    void openForResult(Route route, Fragment fragment, int requestCode);

    void openForResult(Route route, android.app.Fragment fragment, int requestCode);
}
