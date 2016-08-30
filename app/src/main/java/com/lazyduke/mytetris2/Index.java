package com.lazyduke.mytetris2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Lenovo on 2016/8/30.
 */
public class Index extends Activity {

    private Button start;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        start=(Button)findViewById(R.id.start);
        start.setOnClickListener(new CListener());

    }

    public class CListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.setClass(Index.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
