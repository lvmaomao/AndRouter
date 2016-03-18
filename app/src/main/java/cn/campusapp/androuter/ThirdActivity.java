package cn.campusapp.androuter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

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
