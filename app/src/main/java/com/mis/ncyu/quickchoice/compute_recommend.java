package com.mis.ncyu.quickchoice;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class compute_recommend extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private String username;
    private String perfer;
    private String pos;
    private String cash,dis,red,mile;
    private Integer sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute_recommend);
        get_name();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("推薦結果");
        setSupportActionBar(toolbar);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container2);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(mViewPager);
    }
    public String put_user_name()
    {
        return this.username;
    }
    public String put_perfer()
    {
        return this.perfer;
    }
    public String put_pos()
    {
        return this.pos;
    }
    public String put_cash()
    {
        return this.cash;
    }
    public String put_dis()
    {
        return this.dis;
    }
    public String put_red()
    {
        return this.red;
    }
    public String put_mile()
    {
        return this.mile;
    }
    public Integer put_sum()
    {
        return this.sum;
    }

    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
            perfer = context.getString("perfer");
            pos = context.getString("pos");
            cash = context.getString("cash");
            dis = context.getString("dis");
            red = context.getString("red");
            mile = context.getString("mile");
        }
        sum=Integer.parseInt(cash)+Integer.parseInt(dis)+Integer.parseInt(red)+Integer.parseInt(mile);
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new compute_all(), "綜合優惠排序");
        adapter.addFragment(new compute_money(), "現金優惠");
        adapter.addFragment(new tab1_fragment(), "折扣");
        adapter.addFragment(new tab1_fragment(), "紅利積點");
        adapter.addFragment(new tab1_fragment(), "刷卡里程");
        viewPager.setAdapter(adapter);
    }
}
