package cn.campusapp.router.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.campusapp.router.annotation.RouterMap;
import cn.campusapp.router.router.ActivityRouter;


@RouterMap({"activity2://third", "activity://third2"})
public class ThirdActivity extends Activity {

    TextView vTimeTv;
    Button vBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        vTimeTv = (TextView) findViewById(R.id.time);
        Date date = (Date) getIntent().getSerializableExtra("date");
        vTimeTv.setText(new SimpleDateFormat("HH:mm").format(date));
        vBtn = (Button) findViewById(R.id.set_flags_btn);
        vBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityRouter
                        .getInstance()
                        .getRoute("activity://main")
                        .withOpenMethodStart(ThirdActivity.this)
                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                        .open();
            }
        });
    }
}
