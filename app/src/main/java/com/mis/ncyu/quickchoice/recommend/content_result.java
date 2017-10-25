package com.mis.ncyu.quickchoice.recommend;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.MyDBHelper;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.Total_data;
import com.mis.ncyu.quickchoice.home.new_home2;

import java.util.HashMap;
import java.util.List;

public class content_result extends AppCompatActivity {

    List<Total_data> mTotal_data;
    TextView card,bank,type,key_word,compute;
    String card_name,bank_name,key,value;
    Button submit;
    private PackageInfo packageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_result);
        mTotal_data = compute_recommend.mTotal_data;
        card = (TextView)findViewById(R.id.card_name);
        bank = (TextView)findViewById(R.id.bank_name);
        type = (TextView)findViewById(R.id.type);
        key_word = (TextView)findViewById(R.id.key_word);
        compute = (TextView)findViewById(R.id.compute);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginame = "";
                MyDBHelper helper = MyDBHelper.getInstance(content_result.this);
                SQLiteDatabase db = helper.getWritableDatabase();
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

                String url = "http://35.194.203.57/connectdb/add_recommend_record.php";
                HashMap postData = new HashMap();
                postData.put("userid",loginame);
                postData.put("pos",compute_recommend.pos);
                postData.put("card_name",card_name);
                postData.put("cost_price",String.valueOf(compute_recommend.money));
                postData.put("type",type.getText().toString());
                PostResponseAsyncTask readTask = new PostResponseAsyncTask(content_result.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.equals("success")){



                            Toast.makeText(content_result.this, s, Toast.LENGTH_SHORT).show();
                            Intent[] intents = new Intent[2];
                            Intent intent2 = content_result.this.getPackageManager().getLaunchIntentForPackage("com.google.android.apps.walletnfcrel");
                            Intent intent = new Intent(content_result.this, new_home2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                            intents [0] = intent;
                            intents [1] = intent2;
                            //檢查是否有android pay
                            try {
                                packageInfo =  content_result.this.getPackageManager().getPackageInfo(
                                        "com.google.android.apps.walletnfcrel", 0);

                            } catch (PackageManager.NameNotFoundException e) {
                                packageInfo = null;
                                e.printStackTrace();
                            }
                            if(packageInfo ==null){
                                Toast.makeText(content_result.this, "未偵測到android pay!，請至google play安裝", Toast.LENGTH_LONG).show();
                                Uri uri = Uri.parse("market://details?id=com.google.android.apps.walletnfcrel");
                                Intent intent_android_pay = new Intent(Intent.ACTION_VIEW, uri);
                                intents [1] = intent_android_pay;
                                System.out.println("not installed");
                            }else{

                            }
                            startActivities(intents);
                            finish();

                        }
                        else {
                            Toast.makeText(content_result.this, s, Toast.LENGTH_LONG).show();
                        }

                    }
                });
                readTask.execute(url);
            }
        });
        getcontent();


    }

    public void getcontent(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            card_name = context.getString("card");
            bank_name = context.getString("bank");
            key = context.getString("key_word");
            value = context.getString("value");
        }
        Double compute_value = compute_recommend.money*Double.valueOf(value)/100;
        compute.setText(String.valueOf(compute_recommend.money) + "*" + value + "=" + String.valueOf(compute_value));
        String type ="";
        switch (compute_recommend.fragment_pos){
            case 0:
                break;
            case 1:
                type="綜合優惠";
                break;
            case 2:
                type="現金回饋";
                break;
            case 3:
                type="加油";
                break;
            case 4:
                type="紅利積點";
                compute_value = compute_recommend.money/Double.valueOf(value);
                compute.setText(String.valueOf(compute_recommend.money) + "/" + value + "=" + String.valueOf(compute_value));
                break;
            case 5:
                type="旅遊";
                break;
            case 6:
                type="電影";
                break;
        }
        this.type.setText(type);
        bank.setText(bank_name);
        card.setText(card_name);
        key_word.setText(key);


    }
}
