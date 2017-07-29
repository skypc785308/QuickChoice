package com.mis.ncyu.quickchoice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chose_card_type extends AppCompatActivity {

    private String[] card_data;
    private String[] card_id;
    private String[] offer_context;
    private String bankname;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_card_type);
        getAsynHttp(get_bank_id());
    }
    public  String get_bank_id(){
        String bank_id = null;
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            bank_id = context.getString("bankid");
            bankname = context.getString("bankname");
            username = context.getString("user_name");
        }
        return bank_id;
    }

    public void show_in_list_view() {
        ListView showcards = (ListView) findViewById(R.id.show_card_type);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, card_data);
        showcards.setAdapter(adapter);

        showcards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 利用索引值取得點擊的項目內容。
                Toast.makeText(Chose_card_type.this, card_data[i], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Chose_card_type.this, key_in_card.class);
                Bundle context = new Bundle();
                context.putString("card",card_data[i]);
                context.putString("id", card_id[i]);
                context.putString("bankname", bankname);
                context.putString("user_name", username);
                intent.putExtras(context);
                startActivity(intent);
            }
        });
    }

    private void getAsynHttp(String bankid) {
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("bankid",bankid)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://192.168.1.143/connectdb/get_card_list.php")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("data","GGGGG");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    JSONObject init_title = new JSONObject(response.body().string());
                    JSONArray data = init_title.getJSONArray("data");
                    card_data = new String[data.length()];
                    offer_context = new String[data.length()];
                    card_id = new String[data.length()];
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                        card_data[i] = jasondata.getString("name");
                        card_id[i] = jasondata.getString("id");
                        offer_context[i] = jasondata.getString("offer_context");
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
}
