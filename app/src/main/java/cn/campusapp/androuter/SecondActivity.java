package cn.campusapp.androuter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends Activity {
    Button ret ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ret = (Button) findViewById(R.id.back);
        String name = getIntent().getStringExtra("name");
        Toast.makeText(this, String.format("%s", name), Toast.LENGTH_SHORT).show();
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ret();
            }
        });
    }

    private void ret(){
        setResult(200);
        finish();
    }
}
