package com.mis.ncyu.quickchoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by UserMe on 2017/8/22.
 */

public class history_fragment extends Fragment {

    public history_fragment() {}

    ListView showhistory;
    String username;
    List<HashMap<String , String>> list = new ArrayList<>();
    private String[] historydata;
    private String[] positiondata;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        username =((new_home2)getActivity()).put_login_name();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        showhistory=(ListView) view.findViewById(R.id.history_list);
        list = new ArrayList<>();
        http();
        return view;
    }
    public void showdata(){
        if (historydata != null){
            ListAdapter adapter = new SimpleAdapter(getActivity(),list, android.R.layout.simple_list_item_2 ,new String[]{"title" , "text"} ,
                    new int[]{android.R.id.text1 , android.R.id.text2});
            showhistory.setAdapter(adapter);
        }
        else {
            HashMap<String , String> hashMap = new HashMap<>();
            hashMap.put("title" , "沒有資料~");
            hashMap.put("text" , "趕快去推薦~");
            list.add(hashMap);
            ListAdapter adapter = new SimpleAdapter(getActivity(),list, android.R.layout.simple_list_item_2 ,new String[]{"title" , "text"} ,
                    new int[]{android.R.id.text1 , android.R.id.text2});
            showhistory.setAdapter(adapter);
        }

    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/get_history.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.isEmpty()){
                    showdata();
                }
                else {
                    try{
                        JSONObject init_title = new JSONObject(s);
                        JSONArray data = init_title.getJSONArray("data");
                        historydata = new String[data.length()];
                        positiondata = new String[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jasondata = data.getJSONObject(i);
                            historydata[i] = jasondata.getString("card_name");
                            positiondata[i] = jasondata.getString("position");
                            HashMap<String , String> hashMap = new HashMap<>();
                            hashMap.put("title" , historydata[i]);
                            hashMap.put("text" , positiondata[i]);
                            list.add(hashMap);
                            Log.e("data",historydata[i]);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showdata();
                }
            }
        });
        readTask.execute(url);
    }
}
