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
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.Jaccard;
import com.mis.ncyu.quickchoice.MyDBHelper;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.SectionsPageAdapter;
import com.mis.ncyu.quickchoice.StoreInfo;
import com.mis.ncyu.quickchoice.Total_data;
import com.mis.ncyu.quickchoice.card_datatype;
import com.mis.ncyu.quickchoice.home.history_fragment;
import com.mis.ncyu.quickchoice.home.map_fragment;
import com.mis.ncyu.quickchoice.home.mycard_fragment;
import com.mis.ncyu.quickchoice.home.new_home2;
import com.mis.ncyu.quickchoice.home_page_adapter;
import com.mis.ncyu.quickchoice.tab1_fragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mis.ncyu.quickchoice.recommend.compute_recommend.timess;

public class activity_recommend extends AppCompatActivity {

    private String username;
    ArrayList<String> like_user = new ArrayList<String>();
    static int [][] jccard_user = new int[3][6];

    int jccard_user_index = -1;
    private String [] type ={"綜合優惠","現金回饋","加油","紅利積點","旅遊","電影"};
    private int[] type_count = new int[6];
    public  String pos;
    public   String money;
    public static List<Total_data> mTotal_data;
    int card_hlod_martix [][]= new int[100][100];
    ArrayList<String> card_name = new ArrayList<String>();
    ArrayList<String> user_name_array = new ArrayList<String>();
    int count_user = 0;
    private static String[] historydata;
    private static Integer[] historycount;
    private String loginame,email;
    MyDBHelper helper;
    SQLiteDatabase db;

    private final OkHttpClient client = new OkHttpClient();
    static List<StoreInfo> tmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        get_name();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        http3();
        http_history();
        compute_jaccard();
    }
    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
            pos = context.getString("pos");
            money = context.getString("money");
        }
        helper = MyDBHelper.getInstance(activity_recommend.this);
        db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM login",null);
        if (c.getCount()>0){
            c.moveToFirst();
            loginame = c.getString(1);
            email = c.getString(2);
            c.close();
            db.close();
        }
    }
    private void http_history(){
        String url = "http://35.194.203.57/connectdb/get_user_history_count.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(activity_recommend.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.equals("{\"data\":null}")){

                }
                else {
                    try{
                        JSONObject init_title = new JSONObject(s);
                        JSONArray data = init_title.getJSONArray("data");
                        historydata = new String[data.length()];
                        historycount = new Integer[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jasondata = data.getJSONObject(i);
                            historydata[i] = jasondata.getString("type");
                            historycount[i] = jasondata.getInt("times");
                            switch (jasondata.getString("type")){
                                case "綜合優惠":
                                    type_count[0] = jasondata.getInt("times");
                                    break;
                                case "現金回饋":
                                    type_count[1] = jasondata.getInt("times");
                                    break;
                                case "加油":
                                    type_count[2] = jasondata.getInt("times");
                                    break;
                                case "紅利積點":
                                    type_count[3] = jasondata.getInt("times");
                                    break;
                                case "旅遊":
                                    type_count[4] = jasondata.getInt("times");
                                    break;
                                case "電影":
                                    type_count[5] = jasondata.getInt("times");
                                    break;
                            }

                            Log.e("555555555",historydata[i]+historycount[i]);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                timess = type_count;
                Log.e("綜合優惠",String.valueOf(timess[0]));
                Log.e("現金回饋",String.valueOf(timess[1]));
                Log.e("加油",String.valueOf(timess[2]));
                Log.e("紅利積點",String.valueOf(timess[3]));
                Log.e("旅遊",String.valueOf(timess[4]));
                Log.e("電影",String.valueOf(timess[5]));
            }
        });
        readTask.execute(url);
    }
    private void get_user_count(String user_id){
        String url = "http://35.194.203.57/connectdb/get_user_history_count.php";
        HashMap postData = new HashMap();
        postData.put("userid",user_id);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(activity_recommend.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("取得點及次數",s);
                jccard_user_index++;
                int my_index = jccard_user_index;
                try{
                    JSONObject init_title = new JSONObject(s);
                    JSONArray data = init_title.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                            switch (jasondata.getString("type")){
                                case "綜合優惠":
                                    jccard_user[my_index][0] = jasondata.getInt("times");
                                    break;
                                case "現金回饋":
                                    jccard_user[my_index][1] = jasondata.getInt("times");
                                    break;
                                case "加油":
                                    jccard_user[my_index][2] = jasondata.getInt("times");
                                    break;
                                case "紅利積點":
                                    jccard_user[my_index][3] = jasondata.getInt("times");
                                    break;
                                case "旅遊":
                                    jccard_user[my_index][4] = jasondata.getInt("times");
                                    break;
                                case "電影":
                                    jccard_user[my_index][5] = jasondata.getInt("times");
                                    break;
                        }
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<3;i++){
                    StringBuilder log = new StringBuilder();
                    for(int j=0;j<6;j++){
                        log.append(String.valueOf(jccard_user[i][j])+"  ");
                    }
                    Log.e("最像三人優惠典籍次數",log.toString());

                }
            }
        });
        readTask.execute(url);
    }

    private void compute_jaccard(){
        String url = "http://35.194.203.57/connectdb/get_all_user_card.php";
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(activity_recommend.this, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("取得所有人的卡片",s);
                try{
                    JSONObject init_title = new JSONObject(s);
                    JSONArray data = init_title.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                        String user = jasondata.getString("user_id");
                        if(!user_name_array.contains(user)){
                            user_name_array.add(user);
                        }
                        String card = jasondata.getString("card_id");
                        if(!card_name.contains(card)){
                            card_name.add(card);
                        }
                        card_hlod_martix[user_name_array.indexOf(user)][card_name.indexOf(card)] = 2;
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                computejaccard();

                for(int i=0;i<10;i++){
                    StringBuilder log = new StringBuilder();
                    for(int j=0;j<10;j++){
                        log.append(String.valueOf(card_hlod_martix[i][j])+"  ");
                    }
                    Log.e("555555555",log.toString());

                }
                http();

            }
        });
        readTask.execute(url);

    }
    public void computejaccard(){
        int user_index = user_name_array.indexOf(username);
        ArrayList<Double> user_card_index = new ArrayList<>();
        Jaccard data = new Jaccard(card_hlod_martix);
        for(int i=0;i<user_name_array.size();i++){
            user_card_index.add(data.JaccardCount(user_index,i));
        }
        if(user_card_index.size()>=3){
            for(int i=0;i<user_card_index.size();i++){
                for(int j=0;j<user_card_index.size()-1;j++){
                    if (user_card_index.get(j)<user_card_index.get(j+1)){
                        Collections.swap(user_card_index,j,j+1);
                        Collections.swap(user_name_array,j,j+1);
                    }
                }
            }
            for(int i=0;i<4;i++){
                if(!user_name_array.get(i).equals(loginame)){
                    get_user_count(user_name_array.get(i));
                    like_user.add(user_name_array.get(i));
                }
            }
            for(int i=0;i<3;i++){
                Log.e("最像的三人",like_user.get(i));
            }


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

    public void http3() {
        RequestBody formBody = new FormBody.Builder()
                .add("userid", username)
                .build();
        final Request request = new Request.Builder().url("http://35.194.203.57/connectdb/get_store_inform.php")
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
                System.out.print(resStr);
                tmp = new Gson().fromJson(resStr, new TypeToken<List<StoreInfo>>() {}.getType());
            }
        });
    }
}
