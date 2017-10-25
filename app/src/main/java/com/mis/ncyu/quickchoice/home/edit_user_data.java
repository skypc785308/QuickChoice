package com.mis.ncyu.quickchoice.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.Total_data;
import com.mis.ncyu.quickchoice.recommend.activity_recommend;
import com.mis.ncyu.quickchoice.recommend.compute_recommend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class edit_user_data extends AppCompatActivity {

    EditText pw,pw_confirm,name,email;
    Button ok,cancle,edit_password;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);
        get_name();
//        pw_confirm = (EditText)findViewById(R.id.edit_pw_confirm);
        name = (EditText)findViewById(R.id.user);
//        pw = (EditText)findViewById(R.id.edit_pw);
        email = (EditText)findViewById(R.id.user_email);
        ok = (Button)findViewById(R.id.okbtn);
        cancle = (Button)findViewById(R.id.clnbtn);
        edit_password = (Button)findViewById(R.id.edit_password);
        getdata();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View item = LayoutInflater.from(edit_user_data.this).inflate(R.layout.alertdialog_edit_password, null);
                new AlertDialog.Builder(edit_user_data.this)
                        .setTitle("更改密碼")
                        .setView(item)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText old_pw = (EditText) item.findViewById(R.id.old_password);
                                EditText new_pw = (EditText) item.findViewById(R.id.new_password);
                                EditText chk_pw = (EditText) item.findViewById(R.id.check_password);
                                if(TextUtils.isEmpty(old_pw.getText().toString())){
                                    Toast.makeText(edit_user_data.this, "請輸入舊密碼", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(new_pw.getText().toString())) {
                                    Toast.makeText(edit_user_data.this, "請輸入新密碼", Toast.LENGTH_SHORT).show();
                                }
                                else if (TextUtils.isEmpty(chk_pw.getText().toString())) {
                                    Toast.makeText(edit_user_data.this, "請再次輸入新密碼", Toast.LENGTH_SHORT).show();
                                }
                                else if (!new_pw.getText().toString().equals(chk_pw.getText().toString())) {
                                    Toast.makeText(edit_user_data.this, "兩次的密碼輸入不同", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    String url = "http://35.194.203.57/connectdb/updata_user_data.php";
                                    HashMap postData = new HashMap();
                                    postData.put("userid",username);
                                    postData.put("old_pw",old_pw.getText().toString());
                                    postData.put("new_pw",new_pw.getText().toString());
                                    PostResponseAsyncTask readTask = new PostResponseAsyncTask(edit_user_data.this, postData, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String s) {
                                            Log.e("更改密碼結果",s);
                                            if(s.equals("success")){
                                                Toast.makeText(edit_user_data.this, "更改密碼成功", Toast.LENGTH_SHORT).show();

                                            }
                                            else if (s.equals("error_password")){
                                                Toast.makeText(edit_user_data.this, "錯誤的舊密碼", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(edit_user_data.this, "更改密碼失敗", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    readTask.execute(url);

                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }
    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
        }
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/updata_user_data.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
//        postData.put("pw",pw.getText().toString());
        postData.put("name",name.getText().toString());
        postData.put("email",email.getText().toString());
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(edit_user_data.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.equals("success")){
                    Toast.makeText(edit_user_data.this, "更新成功", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(edit_user_data.this, "失敗", Toast.LENGTH_SHORT).show();
                }
            }
        });
        readTask.execute(url);
    }
    private void getdata(){
        String url = "http://35.194.203.57/connectdb/updata_user_data.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(edit_user_data.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("12123",s);
                try {
                    JSONObject userdata = new JSONObject(s);
                    name.setText(userdata.getString("name"));
                    email.setText(userdata.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        readTask.execute(url);
    }
}
