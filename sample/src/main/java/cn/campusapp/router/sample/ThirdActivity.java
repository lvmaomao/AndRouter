package cn.campusapp.router.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.campusapp.router.annotation.RouterMap;


@RouterMap({"activity2://third", "activity://third2"})
public class ThirdActivity extends Activity {

    TextView vTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        vTimeTv = (TextView) findViewById(R.id.time);
        Date date = (Date) getIntent().getSerializableExtra("date");
        vTimeTv.setText(new SimpleDateFormat("HH:mm").format(date));
    }
}
