package com.mis.ncyu.quickchoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    EditText etUsername, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText)findViewById(R.id.edit_id);
        etPassword = (EditText)findViewById(R.id.edit_password);
        Button login_button = (Button)findViewById(R.id.button3);
        login_button.setOnClickListener(this);
        Button signup_button = (Button)findViewById(R.id.button4);
        signup_button.setOnClickListener(signuplisten);


    }
    View.OnClickListener signuplisten = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent2 = new Intent(MainActivity.this, sign_new.class);
            startActivity(intent2);
            //到註冊頁面的按鈕
        }
    };

    @Override
    public void onClick(View v){
        //實作按下登入畫面的動作
        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtUsername", etUsername.getText().toString());
        postData.put("txtPassword", etPassword.getText().toString());
        //將要傳送的資料用post打包好
        PostResponseAsyncTask task = new PostResponseAsyncTask(this,postData);
        task.execute("http://192.168.1.143/connectdb/connow.php");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu m){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, m);
        return super.onCreateOptionsMenu(m);
    }

    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            //顯示登入成功
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, Logined_menu.class);
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
        Intent it =new Intent(this, ForgetActivity.class);
        startActivity(it);
    }


}
