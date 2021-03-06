package com.mis.ncyu.quickchoice.login;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.home.new_home2;

import java.util.HashMap;


public class sign_new extends AppCompatActivity implements View.OnClickListener{

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
        Button cln = (Button)findViewById(R.id.clnbtn);
        cln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid.setText("");
                userpw.setText("");
                username.setText("");
                useremail.setText("");
            }
        });

    }

    @Override
    public void onClick(View view) {
        HashMap postData = new HashMap();
        postData.put("userid", userid.getText().toString());
        postData.put("userpw", userpw.getText().toString());
        postData.put("username", username.getText().toString());
        postData.put("useremail", useremail.getText().toString());
        PostResponseAsyncTask task = new PostResponseAsyncTask(sign_new.this,postData ,new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("回傳狀態",s);
                if (s.equals("success")) {
                    Toast.makeText(sign_new.this, "註冊成功", Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(s.equals("exist")){
                    Toast.makeText(sign_new.this, "此帳號已註冊過", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(sign_new.this, "註冊失敗", Toast.LENGTH_LONG).show();
                }
            }
        });
        task.execute("http://35.194.203.57/connectdb/test.php");
    }
}
