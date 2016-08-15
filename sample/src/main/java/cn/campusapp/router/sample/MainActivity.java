package cn.campusapp.router.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.Queue;

import cn.campusapp.router.Router;
import cn.campusapp.router.annotation.RouterMap;
import cn.campusapp.router.route.ActivityRoute;
import cn.campusapp.router.router.HistoryItem;
import timber.log.Timber;
@RouterMap("activity://main")
public class MainActivity extends Activity {


    Button btn1;

    Button btn2;

    Button btn3;

    Button btn4;

    Button btn5;

    Button btn6;

    Button btn7;

    Button btn8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);




        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openSecondActivity();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondActivityWithVerticalAnim();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondActivityWithHorizontalAnim();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondActivityForResult();
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.open("http://www.baidu.com");
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThirdActivityWithExtraValue();
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThirdActivityWithExtraValueUsingAnotherRoute();
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUnknowUrl();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Queue<HistoryItem> historyItems = Router.getActivityChangedHistories();
        for(HistoryItem item : historyItems){
            Timber.i("%s %s", item.getFrom().toString(), item.getTo().toString());
        }
    }

    private void openSecondActivity(){
        Router.open(this, "activity://second/汤二狗");
    }

    private void openSecondActivityWithVerticalAnim(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute
                .setAnimation(this, R.anim.in_from_left, R.anim.out_to_right)
                .open();


    }

    private void openSecondActivityWithHorizontalAnim(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute.setAnimation(this, R.anim.in_from_top, R.anim.out_to_bottom)
                .open();
    }

    private void openSecondActivityForResult(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute.withOpenMethodStartForResult(this, 200)
                .open();
    }

    private void openThirdActivityWithExtraValue(){
        Date date = new Date();
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity2://third");
        activityRoute
                .withParams("date", date)
                .open();
    }


    private void openThirdActivityWithExtraValueUsingAnotherRoute(){
        Date date = new Date();
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity2://third2?text=33");
        toasts("" + activityRoute
                .withParams("date", date)
                .open());
    }

    private void openUnknowUrl(){
        toasts("" + Router.open("activity://unknow"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            Toast.makeText(this, "Result code "+ resultCode, Toast.LENGTH_SHORT).show();
        }
    }


    private void toasts(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
