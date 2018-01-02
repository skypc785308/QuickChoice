package com.mis.ncyu.quickchoice.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.ShowHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class target_set extends AppCompatActivity {
    String username;
    String[] user_card;
    Spinner card_spin;
    Spinner spinner;
    String select_card, select_type;
    EditText target_value;
    final String[] lunch = {"刷卡金額", "紅利"};
    private final OkHttpClient client = new OkHttpClient();

    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_set);
        get_name();

        spinner = (Spinner)findViewById(R.id.tareget_select);
        card_spin = (Spinner)findViewById(R.id.card_select);
        Button okbtn = (Button)findViewById(R.id.okbtn);
        Button cancle = (Button)findViewById(R.id.clnbtn);
        target_value = (EditText)findViewById(R.id.target_value);

        ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource(target_set.this,
                R.array.target,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(lunchList);
        http();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                select_type = lunch[position];
                Toast.makeText(target_set.this, "你選的是" + lunch[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_http3();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void update_http3(){

        RequestBody formBody = new FormBody.Builder()
                .add("userid", username)
                .add("card_name", select_card)
                .add("type", select_type)
                .add("value",target_value.getText().toString())
                .build();

        final Request request = new Request.Builder().url("http://35.194.203.57/connectdb/update_target.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("測試失敗", "失敗");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resStr = response.body().string();
                Log.e("收到的結果",resStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resStr.equals("success")){
                            Toast.makeText(target_set.this, "設定成功!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(target_set.this, "設定失敗!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void http3_get_current(){

        RequestBody formBody = new FormBody.Builder()
                .add("userid", username)
                .build();

        final Request request = new Request.Builder().url("http://35.194.203.57/connectdb/update_target.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("測試失敗", "失敗");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e("收到的結果",res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(res.equals("null")){
                            Toast.makeText(target_set.this,"目前未設定目標!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            JSONObject init_title = null;
                            try {
                                init_title = new JSONObject(res);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                String card=init_title.getString("card_id");
                                String type = init_title.getString("target_type");
                                String value = init_title.getString("target_value");
                                for (int i=0;i<lunch.length;i++) {
                                    if (lunch[i].equals(type)) {
                                        int index = i;
                                        spinner.setSelection(index);
                                        break;
                                    }
                                }
                                for (int i=0;i<user_card.length;i++) {
                                    if (user_card[i].equals(card)) {
                                        int index = i;
                                        card_spin.setSelection(index);
                                        break;
                                    }
                                }
                                target_value.setText(value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        });

    }


    private void http(){
        String url = "http://35.194.203.57/connectdb/get_my_card.php";
        HashMap postData = new HashMap();
        postData.put("userid", username);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(target_set.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.equals("{\"data\":null}")){

                }
                else {
                    try{
                        JSONObject init_title = new JSONObject(s);
                        JSONArray data = init_title.getJSONArray("data");
                        user_card = new String[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jasondata = data.getJSONObject(i);
                            user_card[i] = jasondata.getString("card_id");
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> cardlist = new ArrayAdapter<>(target_set.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            user_card);
                    card_spin.setAdapter(cardlist);

                    card_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            select_card = user_card[position];
                            Toast.makeText(target_set.this, "你選的是" + user_card[position], Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    http3_get_current();

                }
            }
        });
        readTask.execute(url);
    }
}
