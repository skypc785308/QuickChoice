package com.mis.ncyu.quickchoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Button btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(btnlisten);
    }

    View.OnClickListener btnlisten = new View.OnClickListener() {
        public void onClick(View v){
            Toast.makeText(MainActivity.this,"大家好",Toast.LENGTH_SHORT).show();
        }
    };

}
