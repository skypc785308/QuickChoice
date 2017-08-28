package com.mis.ncyu.quickchoice;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class news extends AppCompatActivity {

    MyDBHelper helper;
    SQLiteDatabase db;
    Button up;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        up = (Button)findViewById(R.id.button2);
        text = (TextView)findViewById(R.id.textView23);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http();

            }
        });

//        for (int i=0;i<20;i++){
//            TextView a = new TextView(this);
//            a.setText("123");
//            Button c = new Button(this);
//            c.setHeight(30);
//            c.setWidth(50);
//            c.setText("安安");
//            TextView b = new TextView(this);
//            b.setHeight(20);
//            b.setText(" ");
//            ll.addView(a);
//            ll.addView(c);
//            ll.addView(b);
//        }


        helper = MyDBHelper.getInstance(this);
        db = helper.getWritableDatabase();
//        String a="";
//        if (db.isOpen()){
//            a="yes";
//        }
//        Log.e("ewewe",a);
//        ContentValues cv = new ContentValues();
//        cv.put("discount_limit","123");
//        cv.put("point_limit","456");
//        cv.put("change_percent","12%");
//        String [] clom = {"_id","discount_limit","point_limit","change_percent"};
//        long id = db.insert("total_change",null,cv);
//        Log.e("ewewe",String.valueOf(id));
//        Cursor c = db.rawQuery("SELECT * FROM total_change",null);
//        String g;
//        String str="";
//        c.moveToFirst();
//        for (int i=0;i<c.getCount();i++){
//            str+=c.getString(3);
//            c.moveToNext();
//
//        }Log.e("ewewe",str);
//
//        db.close();


    }
    private void http(){
        String url = "http://35.194.203.57/connectdb/get_chang.php";
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(news.this, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                try{
                    JSONObject init_title = new JSONObject(s);
                    JSONArray data = init_title.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                        String bank =jasondata.getString("bank");
                        String discount_limit = jasondata.getString("discount_limit");
                        String point_limit = jasondata.getString("point_limit");
                        String change_percent = jasondata.getString("change_percent");
                        change_percent = change_percent.replace("1000點=元","");
                        Log.e("比例",change_percent);
                        ContentValues cv = new ContentValues();
                        cv.put("bank",bank);
                        cv.put("discont_limit",discount_limit);
                        cv.put("point_limit",point_limit);
                        cv.put("change_percent",change_percent);
                        long id = db.insert("total_change",null,cv);
                        Log.e("id",String.valueOf(id));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Cursor c = db.rawQuery("SELECT * FROM total_change",null);
                text.setText("有"+String.valueOf(c.getCount())+"資料");
                c.close();
                db.close();
            }
        });
        readTask.execute(url);
    }

}
