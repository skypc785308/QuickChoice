package com.mis.ncyu.quickchoice;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class key_in_recommend extends AppCompatActivity {


    String card,user_name,pos,type;
    EditText card_name,position,cost_price;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getcard_pos();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_in_recommend);
        card_name = (EditText)findViewById(R.id.card_name);
        position = (EditText)findViewById(R.id.position_name);
        cost_price = (EditText)findViewById(R.id.cost_price);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http();
            }
        });
        card_name.setText(card);
        position.setText(pos);

    }

    public  void getcard_pos(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            user_name = context.getString("user_name");
            pos = context.getString("pos");
            card = context.getString("card_name");
            type = context.getString("type");
        }
    }
    private void http(){
        String url = "http://35.194.203.57/connectdb/add_recommend_record.php";
        HashMap postData = new HashMap();
        postData.put("userid",user_name);
        postData.put("pos",pos);
        postData.put("card_name",card);
        postData.put("cost_price",cost_price.getText().toString());
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.equals("success")){
                    Toast.makeText(key_in_recommend.this, s, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(key_in_recommend.this, new_home2.class);
                    Bundle context = new Bundle();
                    context.putString("user_name", user_name);
                    intent.putExtras(context);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(key_in_recommend.this, s, Toast.LENGTH_LONG).show();
                }
            }
        });
        readTask.execute(url);
    }
}
