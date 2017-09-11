package com.mis.ncyu.quickchoice.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by UserMe on 2017/7/25.
 */

public class mycard_fragment extends Fragment {



    private String[] listviewdata;
    private String username;
    private String[] max_cost;
    private String[] countdate;
    ListView showcards;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        username =((new_home2)getActivity()).put_login_name();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_card,container,false);
        showcards = (ListView) view.findViewById(R.id.my_card_list);
        mSwipeRefreshLayout =(SwipeRefreshLayout) view.findViewById(R.id.id_swipe_refresh_first);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);

            }
        });
        if (listviewdata == null){
            http();
        }
        else {
            showdata();
        }

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), Add_new_card.class);
                Bundle context = new Bundle();
                context.putString("user_name", username);
                intent3.putExtras(context);
                startActivity(intent3);
                Toast.makeText(getActivity(), "新增卡片", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    public void showdata(){
        if (listviewdata != null){
            ListAdapter adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,listviewdata);
            showcards.setAdapter(adapter);
            showcards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), content_my_card.class);
                    Bundle context = new Bundle();
                    context.putString("card_name", listviewdata[i]);
                    context.putString("user_name", username);
                    intent.putExtras(context);
                    startActivity(intent);

                }
            });
        }
        else{
            String[] value ={"空空如也"};
            ListAdapter adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,value);
            showcards.setAdapter(adapter);
        }
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/get_my_card.php";
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
                        listviewdata = new String[data.length()];
                        max_cost = new String[data.length()];
                        countdate = new String[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jasondata = data.getJSONObject(i);
                            listviewdata[i] = jasondata.getString("card_id");
                            max_cost[i] = jasondata.getString("max_cost");
                            countdate[i] = jasondata.getString("date");
                            Log.e("data",listviewdata[i]);
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
