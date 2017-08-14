package com.mis.ncyu.quickchoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class content_my_card extends AppCompatActivity {
    private String username;
    private String cardname;
     private TextView bank_name,card_name,date,max_cost,card_number,expirdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        get_name();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_my_card);
        bank_name = (TextView)findViewById(R.id.bank_name);
        card_name = (TextView)findViewById(R.id.card_name);
        date = (TextView)findViewById(R.id.date);
        max_cost = (TextView)findViewById(R.id.max_cost);
        card_number = (TextView)findViewById(R.id.card_number);
        expirdate = (TextView)findViewById(R.id.expirdate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        http();
    }

    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
            cardname = context.getString("card_name");
        }
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/card_content.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        postData.put("cardid",cardname);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(content_my_card.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("data",s);
                try{
                    JSONObject data = new JSONObject(s);
                    card_name.setText(data.getString("card_id"));
                    date.setText(data.getString("date"));
                    max_cost.setText(data.getString("max_cost"));
                    card_number.setText(data.getString("card_number"));
                    expirdate.setText(data.getString("expir"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        readTask.execute(url);
    }
}
