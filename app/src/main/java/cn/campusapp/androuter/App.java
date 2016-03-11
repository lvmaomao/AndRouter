package cn.campusapp.androuter;

import android.app.Activity;
import android.app.Application;

import java.util.Map;

import cn.campusapp.router.IRouterTableInitializer;
import cn.campusapp.router.Router;
import timber.log.Timber;

/**
 * Created by kris on 16/3/11.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Router.getSharedRouter().init(this, new IRouterTableInitializer() {
                @Override
                public void initRouterTable(Map<String, Class<? extends Activity>> router) {
                    router.put(RouterRules.MAIN, MainActivity.class);
                    router.put(RouterRules.SECOND, SecondActivity.class);
                }
            });
        } catch (Exception e){
            Timber.e(e, "");
        }

        Timber.plant(new Timber.DebugTree());
    }
}
