package com.mis.ncyu.quickchoice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

public class welcome_view extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_view);
        handler.postDelayed(runnable,2000);
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(welcome_view.this,MainActivity.class));
            finish();
        }
    };
}
