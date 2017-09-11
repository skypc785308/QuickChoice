package com.mis.ncyu.quickchoice.recommend;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.MyDBHelper;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.SectionsPageAdapter;
import com.mis.ncyu.quickchoice.Total_data;
import com.mis.ncyu.quickchoice.card_datatype;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

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
    public static String pos;
    public static  Integer money = 0;
    private Integer sum;
    public static List<Total_data> mTotal_data;
    public static List<card_datatype> card_history;
    SmartTabLayout tabLayout;
    int cash,oil,red,plane,movie;
    public static int fragment_pos = 0;
    public static int[] timess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute_recommend);
        setup();
        get_name();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("推薦結果");
        toolbar.setSubtitle(pos);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.container2);

        tabLayout = (SmartTabLayout) findViewById(R.id.tab2);
        http();


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
    public List<card_datatype> put_histpry(){
        return this.card_history;
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
            money = Integer.valueOf(context.getString("money"));
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

            c.close();
            db.close();
            timess = new int[]{cash,oil,red,plane,movie};
            Log.e("times1",String.valueOf(timess[0]));
            Log.e("times2",String.valueOf(timess[1]));
            Log.e("times3",String.valueOf(timess[2]));
            Log.e("times4",String.valueOf(timess[3]));
            Log.e("times5",String.valueOf(timess[4]));
        }
    }
    public void update(){
        MyDBHelper helper = MyDBHelper.getInstance(compute_recommend.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        int[] data = timess;
        String a =String.valueOf(data[0]);
        String b =String.valueOf(data[1]);
        String c =String.valueOf(data[2]);
        String d =String.valueOf(data[3]);
        String e =String.valueOf(data[4]);
        Log.e("times1",a);
        Log.e("times2",b);
        Log.e("times3",c);
        Log.e("times4",d);
        Log.e("times5",e);
        values.put("cash", a);
        values.put("oil", b);
        values.put("red", c);
        values.put("plane",d);
        values.put("movie",e);
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
                            if (!store.equals(pos)){
                                store = "null";
                            }
                            mTotal_data.add(new Total_data(card,bank,buy,red,cash,movie,oil_cash,card_offer,plane,store));
                        }
                        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
                        // Set up the ViewPager with the sections adapter.
                        setupViewPager(mViewPager);
                        tabLayout.setViewPager(mViewPager);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
        readTask.execute(url);
    }




    private void setupViewPager(ViewPager viewPager) {
        mSectionsPageAdapter.addFragment(new compute_history(), "本月已刷卡金額");
        mSectionsPageAdapter.addFragment(new compute_all(), "綜合優惠排序");
        mSectionsPageAdapter.addFragment(new compute_cash(), "現金回饋");
        mSectionsPageAdapter.addFragment(new compute_oil(), "加油");
        mSectionsPageAdapter.addFragment(new compute_red(), "紅利積點");
        mSectionsPageAdapter.addFragment(new compute_plane(), "旅遊");
        mSectionsPageAdapter.addFragment(new compute_movie(), "電影");
        viewPager.setAdapter(mSectionsPageAdapter);
    }
}
