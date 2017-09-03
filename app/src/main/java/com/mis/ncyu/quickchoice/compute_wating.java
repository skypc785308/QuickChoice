package com.mis.ncyu.quickchoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class compute_wating extends AppCompatActivity {

    private String username;
    private String pos;
    private List<Total_data> mTotal_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute_wating);
        get_name();
    }

    @Override
    protected void onResume() {
        super.onResume();
        http();
    }

    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
            pos = context.getString("pos");
        }
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/get_all_total.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        postData.put("pos",pos);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(compute_wating.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("data_all",s);
                try{
                    JSONObject init_title = new JSONObject(s);
                    JSONArray data = init_title.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                        String card = jasondata.getString("card_name");
                        String bank = jasondata.getString("card_bank");
                        String buy = jasondata.getString("buy");
                        String red = jasondata.getString("red");
                        String cash = jasondata.getString("cash");
                        String movie = jasondata.getString("movie");
                        String oil_cash = jasondata.getString("oil_cash");
                        String card_offer = jasondata.getString("card_offer");
                        String plane = jasondata.getString("plane");
                        mTotal_data.add(new Total_data(card,bank,buy,red,cash,movie,oil_cash,card_offer,plane));
                    }
                    Intent intent = new Intent(compute_wating.this, compute_recommend.class);
                    Bundle context = new Bundle();
                    context.putString("user_name", username);

                    context.putString("pos", pos);
                    intent.putExtras(context);
                    startActivity(intent);
                    finish();

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        readTask.execute(url);
    }
}
