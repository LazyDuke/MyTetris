package com.lazyduke.mytetris2.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lazyduke.mytetris2.R;

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
            intent.putExtra("go",true);
            startActivity(intent);
            finish();
        }
    }

}
