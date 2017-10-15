package com.mis.ncyu.quickchoice.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.mis.ncyu.quickchoice.login.MainActivity;
import com.mis.ncyu.quickchoice.MyDBHelper;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.home_page_adapter;
import com.mis.ncyu.quickchoice.login.sign_new;
import com.mis.ncyu.quickchoice.recommend.activity_recommend;
import com.mis.ncyu.quickchoice.tab1_fragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import static com.mis.ncyu.quickchoice.R.string.app_name;

public class new_home2 extends AppCompatActivity {

    private home_page_adapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private String loginame,email;
    MyDBHelper helper;
    SQLiteDatabase db;
    ArrayList<LatLng> LatLngpos;
    ArrayList<String> pos_name;
    SmartTabLayout mTabLayout;
    Toolbar toolbar;
    Drawer drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper = MyDBHelper.getInstance(new_home2.this);
        db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM login",null);
        if (c.getCount()>0){
            c.moveToFirst();
            loginame = c.getString(1);
            email = c.getString(2);
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
        toolbar.setTitle(app_name);
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
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(app_name).withIcon(GoogleMaterial.Icon.gmd_credit_card);
        PrimaryDrawerItem item2 = (PrimaryDrawerItem) new PrimaryDrawerItem().withName("會員資料").withIcon(FontAwesome.Icon.faw_user);
        PrimaryDrawerItem item3 = (PrimaryDrawerItem) new PrimaryDrawerItem().withName("登出").withIcon(FontAwesome.Icon.faw_sign_out);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ic_launcher_back)
                .addProfiles(
                        new ProfileDrawerItem().withName(loginame).withEmail(email).withIcon(getResources().getDrawable(R.drawable.butterfly))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        drawer = new DrawerBuilder()
                .withActivity(new_home2.this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("關於").withIcon(GoogleMaterial.Icon.gmd_info)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch(position){
                            case 1:
                                break;
                            case 3:
                                Intent intent = new Intent(new_home2.this, edit_user_data.class);
                                Bundle context = new Bundle();
                                context.putString("user_name", loginame);
                                intent.putExtras(context);
                                startActivity(intent);
                                break;
                            case 5:
                                db = helper.getWritableDatabase();
                                db.delete("login",null, null);
                                db.close();
                                Intent intent2 = new Intent(new_home2.this, MainActivity.class);
                                // 不再重新启动这个Activity的实例，而且这个Activity上方的所有Activity都将关闭，
                                // 然后这个Intent会作为一个新的Intent投递到老的Activity（现在位于顶端）中。
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent2);
                                finish();
                                break;
                            case 7:
                                showAboutDialog();
                                break;
                        }

                        drawer.closeDrawer();
                        return false;
                    }
                })
                .build();
    }
    public String put_login_name()
    {
        return this.loginame;
    }

    private void setupViewPager(ViewPager viewPager) {
        home_page_adapter adapter = new home_page_adapter(getSupportFragmentManager());
        adapter.addFragment(new map_fragment(), "立即推薦");
        adapter.addFragment(new mycard_fragment(), "我的信用卡");
        adapter.addFragment(new history_fragment(), "歷史紀錄");
        adapter.addFragment(new coperation(), "合作店家優惠");

        viewPager.setAdapter(adapter);

    }
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("關於我們");
        builder.setMessage("我們是畢業專題第4組");
        builder.setPositiveButton("確定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }
        );
        builder.show();
    }
}
