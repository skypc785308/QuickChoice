package com.mis.ncyu.quickchoice;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    public static boolean isFinish;
    EditText etUsername, etPassword;
    MyDBHelper helper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText)findViewById(R.id.edit_id);
        etPassword = (EditText)findViewById(R.id.edit_password);
        Button login_button = (Button)findViewById(R.id.button3);
        login_button.setOnClickListener(this);
        Button signup_button = (Button)findViewById(R.id.button4);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, sign_new.class));
                //到註冊頁面的按鈕
            }
        });

    }

    @Override
    public void onClick(View v){
        //實作按下登入畫面的動作
        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtUsername", etUsername.getText().toString());
        postData.put("txtPassword", etPassword.getText().toString());
        //將要傳送的資料用post打包好
        PostResponseAsyncTask task = new PostResponseAsyncTask(this,postData,this);
        task.execute("http://35.194.203.57/connectdb/connow.php");

    }

    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            helper = MyDBHelper.getInstance(this);
            db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("user_id",etUsername.getText().toString());
            cv.put("login_date",date);
            long id = db.insert("login",null,cv);
            db.close();
            //顯示登入成功
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, new_home2.class);
            Bundle context = new Bundle();
            context.putString("user_name", etUsername.getText().toString());
            intent.putExtras(context);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
    }

    //點擊進入'忘記密碼頁
    public void gotoforget(View v){
        startActivity(new Intent(this, ForgetActivity.class));
    }


}
