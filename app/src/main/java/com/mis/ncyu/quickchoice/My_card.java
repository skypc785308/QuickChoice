package com.mis.ncyu.quickchoice;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class My_card extends AppCompatActivity {
    private Context Context;
    private String[] listviewdata;
    private String[] max_cost;
    private String[] countdate;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        getAsynHttp(get_user_id());
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent();
                intent3.setClass(My_card.this, Add_new_card.class);
                Bundle context = new Bundle();
                context.putString("user_name", username);
                intent3.putExtras(context);
                startActivity(intent3);
                Toast.makeText(My_card.this, "新增卡片", Toast.LENGTH_LONG).show();
            }
        });
    }
    public  String get_user_id(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
        }
        return username;
    }
    private void getAsynHttp(String userid) {
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userid",userid)
                .build();
        Request request = new Request.Builder()
                .url("http://35.194.203.57/connectdb/get_my_card.php")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("err","errrrrrrrrrrrrrrrr");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    JSONObject init_title = new JSONObject(response.body().string());
                    JSONArray data = init_title.getJSONArray("data");
                    listviewdata = new String[data.length()];
                    max_cost = new String[data.length()];
                    countdate = new String[data.length()];
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                        listviewdata[i] = jasondata.getString("card_id");
                        max_cost[i] = jasondata.getString("max_cost");
                        countdate[i] = jasondata.getString("date");
                        Log.e("data",listviewdata[i]);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show_in_list_view();
                    }
                });
            }
        });
    }

    public void show_in_list_view(){
        ListView showcards = (ListView) findViewById(R.id.my_card_list);
        String[] values = listviewdata;
        if (values != null){
            ListAdapter adapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 ,values);
            showcards.setAdapter(adapter);
        }
        else{
            String[] value ={"空空如也"};
            ListAdapter adapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 ,value);
            showcards.setAdapter(adapter);
        }

    }
}
