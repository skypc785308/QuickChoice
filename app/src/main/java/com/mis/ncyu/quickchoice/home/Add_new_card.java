package com.mis.ncyu.quickchoice.home;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mis.ncyu.quickchoice.Chose_card_type;
import com.mis.ncyu.quickchoice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Add_new_card extends AppCompatActivity {
    private String[] listviewdata;
    private String[] bank_id;
    private String username;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);
        get_user_id();
        mSwipeRefreshLayout =(SwipeRefreshLayout) findViewById(R.id.id_swipe_refresh_first);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAsynHttp();
    }

    public  String get_user_id(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
        }
        return username;
    }
    private void getAsynHttp() {
        OkHttpClient mOkHttpClient=new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://35.194.203.57/connectdb/get_card.php")
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
                    bank_id = new String[data.length()];
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                        listviewdata[i] = jasondata.getString("name");
                        bank_id[i] = jasondata.getString("id");
                        Log.e("data",bank_id[i]);
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
        ListView showcards = (ListView) findViewById(R.id.show_bank_listview);
        String[] values = listviewdata;
        ListAdapter adapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 ,values);
        showcards.setAdapter(adapter);
        showcards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 利用索引值取得點擊的項目內容。
                Toast.makeText(Add_new_card.this, listviewdata[i], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Add_new_card.this, Chose_card_type.class);
                Bundle context = new Bundle();
                context.putString("bankid", bank_id[i]);
                context.putString("bankname", listviewdata[i]);
                context.putString("user_name", username);
                intent.putExtras(context);
                startActivity(intent);
            }
        });
    }
}
