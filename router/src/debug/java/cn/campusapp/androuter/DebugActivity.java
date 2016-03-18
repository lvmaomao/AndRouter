package cn.campusapp.androuter;

import android.app.Activity;
import android.os.Bundle;

import cn.campusapp.router.R;

public class DebugActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }
}
