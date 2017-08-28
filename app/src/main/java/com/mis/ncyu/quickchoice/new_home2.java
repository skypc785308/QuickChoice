package com.mis.ncyu.quickchoice;

import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class new_home2 extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private String loginame;
    ArrayList<LatLng> LatLngpos;
    ArrayList<String> pos_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getlogin_name();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // App Logo
        toolbar.setLogo(R.mipmap.ic_launcher);
        // Title
        toolbar.setTitle(loginame);
        // Sub Title
        toolbar.setSubtitle("子標題");
        setSupportActionBar(toolbar);


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public  void getlogin_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            loginame = context.getString("user_name");
        }
    }
    public String put_login_name()
    {
        return this.loginame;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Recommend(), "立即推薦");
        adapter.addFragment(new mycard_fragment(), "我的信用卡");
        adapter.addFragment(new history_fragment(), "歷史紀錄");
        adapter.addFragment(new tab1_fragment(), "最新優惠");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
