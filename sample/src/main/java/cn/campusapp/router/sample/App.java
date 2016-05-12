package cn.campusapp.router.sample;

import android.app.Activity;
import android.app.Application;

import java.util.Map;

import cn.campusapp.router.Router;
import cn.campusapp.router.router.IActivityRouteTableInitializer;

/**
 * Created by kris on 16/3/11.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.initActivityRouter(getApplicationContext(), new IActivityRouteTableInitializer() {
            @Override
            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
                router.put("activity://second/:{name}", SecondActivity.class);
            }
        });
        // Router.initActivityRouter(this);
        Router.initBrowserRouter(getApplicationContext());
        // to output logs of AndRouter
        Router.setDebugMode(true);
    }
}
