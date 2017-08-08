package com.mis.ncyu.quickchoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Start_recommend extends AppCompatActivity {

    private String username;
    private String perfer;
    private String pos;
    private ListView listV;
    List<card_datatype> card_list;
    result_type[] mresult_types;
    private mylistadapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        get_name();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_recommend);
        listV=(ListView)findViewById(R.id.result_view);
        card_list = new ArrayList<card_datatype>();
        http();
    }


    public void get_name(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
            perfer = context.getString("perfer");
            pos = context.getString("pos");
        }
    }
    public Boolean compute(int index){
       for (int i=0;i<index;i++){
           for (int j = 0; j < index-1; j++)
               if (mresult_types[j].getKey() > mresult_types[j + 1].getKey()) {
                   result_type t = mresult_types[j + 1];
                   mresult_types[j + 1] = mresult_types[j];
                   mresult_types[j]=t;
               }
       }
        for (Integer i=index-1;i>=0;i--){
            Integer r = index-i;
            card_list.add(new card_datatype(r.toString(),mresult_types[i].getName(),mresult_types[i].getKeyword(),r));
        }
        showdata();
        return Boolean.TRUE;
    }


    public void showdata(){
        adapter = new mylistadapter(Start_recommend.this,card_list);
        listV.setAdapter(adapter);
    }

    public void shownodata(){
        card_list.add(new card_datatype("none","沒有卡片啦~","快去新增!!",123456789));
        adapter = new mylistadapter(Start_recommend.this,card_list);
        listV.setAdapter(adapter);
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/recommend.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        postData.put("pos",pos);
        postData.put("perfer",perfer);
        Log.e("ewewe",username);
        Log.e("ewewe",pos);
        Log.e("ewewe",perfer);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(Start_recommend.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("ewewe",s);
                if(s.isEmpty()){
                    shownodata();
                }
                else {
                    int index = 0;
                    try{
                        JSONObject init_title = new JSONObject(s);
                        JSONArray data = init_title.getJSONArray("data");
                        index = data.length();
                        mresult_types = new result_type[index];
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jasondata = data.getJSONObject(i);
                            String card =jasondata.getString("card_id");
                            mresult_types[i] = new result_type();
                            mresult_types[i].setName(card);
                            mresult_types[i].setKeyword(jasondata.getString("key_word"));
                            Log.e("ewewe",mresult_types[i].getKeyword());
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    compute(index);
                }
            }
        });
        readTask.execute(url);
    }


}
