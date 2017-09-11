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

public class content_history extends AppCompatActivity {

    private TextView card_name,date,cost_price,position;
    private String ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_history);
        get_name();
        card_name = (TextView)findViewById(R.id.card_name);
        date = (TextView)findViewById(R.id.date);
        cost_price = (TextView)findViewById(R.id.cost_price);
        position = (TextView)findViewById(R.id.position);
        http();
    }
    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            ids = context.getString("ids");
        }
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/history_content.php";
        HashMap postData = new HashMap();
        postData.put("ids",ids);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(content_history.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("data",s);
                try{
                    JSONObject data = new JSONObject(s);
                    card_name.setText(data.getString("card_name"));
                    date.setText(data.getString("create_date"));
                    cost_price.setText(data.getString("cost_price"));
                    position.setText(data.getString("position"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        readTask.execute(url);
    }
}
