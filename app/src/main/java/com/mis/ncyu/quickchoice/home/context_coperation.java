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
    private String ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_coperation);
        get_name();
        date = (TextView)findViewById(R.id.date);
        position = (TextView)findViewById(R.id.postiton2);
        detail = (TextView)findViewById(R.id.detail);
        http();
    }

    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            ids = context.getString("ids");
        }
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/context_coperation.php";
        HashMap postData = new HashMap();
        postData.put("ids",ids);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(context_coperation.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("data",s);
                try{
                    JSONObject data = new JSONObject(s);
                    date.setText(data.getString("used_card_time"));
                    detail.setText(data.getString("used_card_detail"));
                    position.setText(data.getString("used_card_name"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        readTask.execute(url);
    }
}
