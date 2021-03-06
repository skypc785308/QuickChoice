package com.mis.ncyu.quickchoice.login;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.view.WindowManager;

import com.mis.ncyu.quickchoice.MyDBHelper;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.home.new_home2;

public class welcome_view extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler.postDelayed(runnable,3500);
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MyDBHelper helper = MyDBHelper.getInstance(welcome_view.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM login",null);
            if (c.getCount()>0){
                c.moveToFirst();
                String login_id = c.getString(1);
                Intent intent = new Intent(welcome_view.this, new_home2.class);
                c.close();
                db.close();
                startActivity(intent);
                finish();
            }
            else {
                c.close();
                db.close();
                startActivity(new Intent(welcome_view.this,MainActivity.class));
                finish();
            }
        }
    };
}
