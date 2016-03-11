package cn.campusapp.androuter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.campusapp.router.Route;
import cn.campusapp.router.RoutePathBuilder;
import cn.campusapp.router.Router;
import cn.campusapp.router.exception.NotAllKeySetException;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String path = new RoutePathBuilder(RouterRules.SECOND)
                            .withKeyValue("id", "1")
                            .build();
                    Timber.i("Path %s", path);
                    Route route = new Route.Builder()
                            .setPath(path)
                            .withParams("extra", "It works")
                            .build();
                    Router.getSharedRouter().open(route);
                } catch (NotAllKeySetException e) {
                    e.printStackTrace();
                }

            }
        });



    }

}
