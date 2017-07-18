package com.mis.ncyu.quickchoice;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;


public class sign_new extends AppCompatActivity implements View.OnClickListener, AsyncResponse   {

    EditText userid, userpw, username, useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_new);

        userid = (EditText)findViewById(R.id.user_id);
        userpw = (EditText)findViewById(R.id.user_password);
        username = (EditText)findViewById(R.id.user);
        useremail = (EditText)findViewById(R.id.user_email);
        Button sign_up = (Button)findViewById(R.id.okbtn);
        sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        HashMap postData = new HashMap();
        postData.put("userid", userid.getText().toString());
        postData.put("userpw", userpw.getText().toString());
        postData.put("username", username.getText().toString());
        postData.put("useremail", useremail.getText().toString());
        PostResponseAsyncTask task = new PostResponseAsyncTask(this,postData);
        task.execute("http://10.3.204.7/connectdb/test.php");


    }

    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            Toast.makeText(this, "註冊成功", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Toast.makeText(this, "註冊失敗", Toast.LENGTH_LONG).show();
        }
    }
}
