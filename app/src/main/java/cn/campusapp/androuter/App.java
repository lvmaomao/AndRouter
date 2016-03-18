package cn.campusapp.androuter;

import android.app.Activity;
import android.app.Application;

import java.util.Map;

import cn.campusapp.router.Router;
import cn.campusapp.router.router.IActivityRouteTableInitializer;
import timber.log.Timber;

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
        Router.initBrowserRouter(getApplicationContext());
        Timber.plant(new Timber.DebugTree());
    }
}
