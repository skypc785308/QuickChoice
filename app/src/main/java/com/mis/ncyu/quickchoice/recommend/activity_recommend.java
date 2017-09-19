package com.mis.ncyu.quickchoice.recommend;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.MyDBHelper;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.SectionsPageAdapter;
import com.mis.ncyu.quickchoice.Total_data;
import com.mis.ncyu.quickchoice.card_datatype;
import com.mis.ncyu.quickchoice.home.history_fragment;
import com.mis.ncyu.quickchoice.home.map_fragment;
import com.mis.ncyu.quickchoice.home.mycard_fragment;
import com.mis.ncyu.quickchoice.home_page_adapter;
import com.mis.ncyu.quickchoice.tab1_fragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_recommend extends AppCompatActivity {

    private String username;
    public  String pos;
    public   String money;
    public static List<Total_data> mTotal_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        get_name();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        http();
    }
    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
            pos = context.getString("pos");
            money = context.getString("money");
        }
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/get_all_total.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        postData.put("pos",pos);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(activity_recommend.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("data_all",s);
                mTotal_data = new ArrayList<>();
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
                        String store = jasondata.getString("store");
                        if (!store.equals(pos)){
                            store = "null";
                        }
                        mTotal_data.add(new Total_data(card,bank,buy,red,cash,movie,oil_cash,card_offer,plane,store));
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(activity_recommend.this, compute_recommend.class);
                Bundle context = new Bundle();
                context.putString("user_name", username);
                context.putString("pos", pos);
                context.putString("money", money);
                intent.putExtras(context);
                startActivity(intent);
                finish();
            }
        });
        readTask.execute(url);
    }
}
