package com.mis.ncyu.quickchoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class compute_get_map_json extends AppCompatActivity implements AsyncResponse {
    String [] pos = {"家樂福","新光三越","大遠百","秀泰廣場","陶板屋","UNIQLO","NET","星巴克","野宴","逐鹿炭火"};
    ArrayList<LatLng> LatLngpos;
    ArrayList<String> pos_name;
    ArrayList<Double>lat;
    ArrayList<Double>lng;
    Integer count = 0;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute_get_map_json);
        for(int i=0;i<pos.length;i++){
            httpall(pos[i]);
        }

    }
    public void httpall(String position){
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        sb.append("query=" + position+ "+" + "嘉義");
        sb.append("&key=AIzaSyBmQhDajl0S9NJtvaidNY_nxNOp0sbe-EQ");
        String url = sb.toString();
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(this,this);
        readTask.execute(url);
    }

    @Override
    public void processFinish(String s) {
        Log.e("seeeeee", String.valueOf(Thread.currentThread().getId()));
        try {
            JSONObject init_title = new JSONObject(s);
            JSONArray data = init_title.getJSONArray("results");
            for(int i=0;i<data.length();i++){
                JSONObject geometry = (JSONObject)data.get(i);
                JSONObject pos = geometry.getJSONObject("geometry").getJSONObject("location");
                lat.add((Double)pos.get("lat"));
                lng.add((Double)pos.get("lng"));
                String iconurl =  geometry.getString("icon");
                String name =  geometry.getString("name");
                pos_name.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        count++;
        if (count==pos.length){
            Intent intent = new Intent(compute_get_map_json.this, new_home2.class);
            Bundle context = new Bundle();
            context.putString("user_name",user_name);
            Double latarry[] = (Double[])lat.toArray(new Double[0]);
            Double lngarry[] = (Double[])lng.toArray(new Double[0]);
            context.putStringArrayList("pos_name",pos_name);
            intent.putExtras(context);
            startActivity(intent);
            finish();
        }
    }
}
