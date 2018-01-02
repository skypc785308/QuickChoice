package com.mis.ncyu.quickchoice.recommend;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mis.ncyu.quickchoice.MyDBHelper;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.ShowHistory;
import com.mis.ncyu.quickchoice.home.new_home2;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class activity_history extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private String loginame, card_name;
    private TextView hs_card_name,hs_red,hs_total,hs_limit_cost;

    MyDBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        helper = MyDBHelper.getInstance(activity_history.this);
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM login",null);
        if (c.getCount()>0){
            c.moveToFirst();
            loginame = c.getString(1);
            c.close();
            db.close();
        }
        else {
            c.close();
            db.close();
            finish();
        }

        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            card_name = context.getString("card");
        }




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        hs_card_name = (TextView)findViewById(R.id.hs_card_name);
        hs_red = (TextView)findViewById(R.id.hs_collect_red);
        hs_total = (TextView)findViewById(R.id.hs_cost_total);
        hs_limit_cost = (TextView)findViewById(R.id.hs_limit_cost);
        http3();
    }

    public void http3(){
        RequestBody formBody = new FormBody.Builder()
                .add("userid", loginame)
                .add("card_name", card_name)
                .build();

        final Request request = new Request.Builder().url("http://35.194.203.57/connectdb/get_card_history.php")
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
                final List<ShowHistory> jsonData = new Gson().fromJson(resStr, new TypeToken<List<ShowHistory>>(){}.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for( ShowHistory json : jsonData) {
                            hs_card_name.setText(json.getCardName());
                            hs_red.setText(String.valueOf(json.getRedGet()));
                            hs_total.setText(String.valueOf(json.getTotalCost()));
                            hs_limit_cost.setText(String.valueOf(json.getCostLimit()));
                        }
                    }
                });
            }
        });

    }
}
