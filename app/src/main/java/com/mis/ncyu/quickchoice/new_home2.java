package com.mis.ncyu.quickchoice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

public class new_home2 extends AppCompatActivity {

    private home_page_adapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private String loginame;
    MyDBHelper helper;
    SQLiteDatabase db;
    ArrayList<LatLng> LatLngpos;
    ArrayList<String> pos_name;
    SmartTabLayout mTabLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper = MyDBHelper.getInstance(new_home2.this);
        db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM login",null);
        if (c.getCount()>0){
            c.moveToFirst();
            loginame = c.getString(1);
            c.close();
            db.close();
        }
        else {
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // App Logo
        toolbar.setLogo(R.mipmap.ic_launcher);
        // Title
        toolbar.setTitle(loginame);
        // Sub Title
        toolbar.setSubtitle("子標題");
        setSupportActionBar(toolbar);


        mSectionsPageAdapter = new home_page_adapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        mTabLayout = (SmartTabLayout) findViewById(R.id.tab);
        mTabLayout.setViewPager(mViewPager);
        initDrawer();

    }
    private void initDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.app_name).withIcon(GoogleMaterial.Icon.gmd_wb_sunny);
        SecondaryDrawerItem item2 = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("會員資料").withIcon(FontAwesome.Icon.faw_github);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.back)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.butterfly))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName(R.string.app_name)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return false;
                    }
                })
                .build();
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
        home_page_adapter adapter = new home_page_adapter(getSupportFragmentManager());
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
        switch (item.getItemId()) {
            case R.id.about: showAboutDialog(); break;
            case R.id.log_out:
                db = helper.getWritableDatabase();
                db.delete("login",null, null);
                db.close();
                Intent intent = new Intent(new_home2.this, MainActivity.class);
                // 不再重新启动这个Activity的实例，而且这个Activity上方的所有Activity都将关闭，
                // 然后这个Intent会作为一个新的Intent投递到老的Activity（现在位于顶端）中。
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MainActivity.isFinish = true;
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("關於我們");
        builder.setMessage("我們是畢業專題第10組");
        builder.setPositiveButton("確定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                }
        );
        builder.show();
    }
}
