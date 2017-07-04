package com.mis.ncyu.quickchoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Logined_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logined_menu);
        getlogin_name();
    }

    public  void getlogin_name(){
        String userid;
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            userid = context.getString("帳號");
            TextView output = (TextView) findViewById(R.id.textView8);
            output.setText(userid);
        }
    }
}
