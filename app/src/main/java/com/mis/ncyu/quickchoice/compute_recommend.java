package com.mis.ncyu.quickchoice;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class compute_recommend extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private String username;
    private String pos;
    private Integer sum;
    private List<Total_data> mTotal_data;
    TabLayout tabLayout;
    int cash,oil,red,plane,movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute_recommend);
        setup();
        get_name();
        http();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("推薦結果");
        toolbar.setSubtitle("pos");
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container2);


        tabLayout = (TabLayout) findViewById(R.id.tabs2);

    }
    public String put_user_name()
    {
        return this.username;
    }
    public String put_pos()
    {
        return this.pos;
    }
    public List<Total_data> put_data(){
        return this.mTotal_data;
    }

    @Override
    protected void onDestroy() {
        update();
        super.onDestroy();
    }

    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
            pos = context.getString("pos");
        }
    }
    public void setup(){
        MyDBHelper helper = MyDBHelper.getInstance(compute_recommend.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM login",null);
        if (c.getCount()>0) {
            c.moveToFirst();
            String login_id = c.getString(1);
            cash = c.getInt(c.getColumnIndex("cash"));
            oil = c.getInt(c.getColumnIndex("oil"));
            red = c.getInt(c.getColumnIndex("red"));
            plane = c.getInt(c.getColumnIndex("plane"));
            movie = c.getInt(c.getColumnIndex("movie"));
            Log.e("times1",String.valueOf(cash));
            Log.e("times2",String.valueOf(oil));
            Log.e("times3",String.valueOf(red));
            Log.e("times4",String.valueOf(plane));
            Log.e("times5",String.valueOf(movie));
            c.close();
            db.close();
            SectionsPageAdapter.times = new int[]{cash,oil,red,plane,movie};
        }
    }
    public void update(){
        MyDBHelper helper = MyDBHelper.getInstance(compute_recommend.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        int[] data = SectionsPageAdapter.times;
        String a =String.valueOf(data[0]);
        String b =String.valueOf(data[1]);
        String c =String.valueOf(data[2]);
        String d =String.valueOf(data[3]);
        String e =String.valueOf(data[3]);
        Log.e("times1",a);
        Log.e("times2",b);
        Log.e("times3",c);
        Log.e("times4",d);
        Log.e("times5",e);
        values.put("cash", a);
        values.put("oil", b);
        values.put("red", c);
        values.put("plane",d);
        db.update("login", values,null, null);
        db.close();
    }
    private void http(){
        String url = "http://35.194.203.57/connectdb/get_all_total.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        postData.put("pos",pos);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(compute_recommend.this, postData, new AsyncResponse() {
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
                            mTotal_data.add(new Total_data(card,bank,buy,red,cash,movie,oil_cash,card_offer,plane,store));
                        }
                        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
                        // Set up the ViewPager with the sections adapter.
                        setupViewPager(mViewPager);
                        tabLayout.setupWithViewPager(mViewPager);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
        readTask.execute(url);
    }


    private void setupViewPager(ViewPager viewPager) {
        mSectionsPageAdapter.addFragment(new compute_all(), "綜合優惠排序");
        mSectionsPageAdapter.addFragment(new tab1_fragment(), "現金優惠");
        mSectionsPageAdapter.addFragment(new tab1_fragment(), "加油");
        mSectionsPageAdapter.addFragment(new tab1_fragment(), "紅利積點");
        mSectionsPageAdapter.addFragment(new tab1_fragment(), "旅遊");
        mSectionsPageAdapter.addFragment(new tab1_fragment(), "電影");
        viewPager.setAdapter(mSectionsPageAdapter);
    }
}
