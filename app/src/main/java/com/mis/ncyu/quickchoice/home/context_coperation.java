package com.mis.ncyu.quickchoice.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class context_coperation extends AppCompatActivity {

    private TextView detail,date,position;
    private String get_data, get_pos, get_detial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_coperation);
        get_name();
        date = (TextView)findViewById(R.id.date);
        position = (TextView)findViewById(R.id.postiton2);
        detail = (TextView)findViewById(R.id.detail);
        date.setText(get_data);
        position.setText(get_pos);
        detail.setText(get_detial);

    }

    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            get_data = context.getString("get_data");
            get_pos = context.getString("get_pos");
            get_detial = context.getString("get_detial");
        }
    }
}
