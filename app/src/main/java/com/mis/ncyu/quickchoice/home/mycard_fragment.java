package com.mis.ncyu.quickchoice.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.mis.ncyu.quickchoice.mylistadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    ArrayList<String> mylistdata;
    SwipeRefreshLayout mSwipeRefreshLayout;
    static int card_count = 0;
    mylistadapter mylistadapter;

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
                http(true);

            }
        });
        if (listviewdata == null){
            http(false);
        }
        else {
            showdata(false);
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

            }
        });
        return view;
    }

    public void showdata(Boolean update){
        if (listviewdata != null){
             mylistadapter = new mylistadapter(getActivity(),mylistdata);
//            ListAdapter adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,listviewdata);
//            showcards.setAdapter(adapter);
            showcards.setAdapter(mylistadapter);
            showcards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    String card_name = mylistdata.get(position);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("確定刪除嗎?")
                            .setMessage("想要刪除" + card_name + " 嗎?")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String card_name = mylistdata.get(position);
                                    mylistdata.remove(position);
                                    if (mylistdata.isEmpty()){
                                        String[] value ={"尚未加入信用卡"};
                                        ListAdapter adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,value);
                                        showcards.setAdapter(adapter);
                                    }
                                    else {
                                        showcards.setAdapter(mylistadapter);
                                    }
                                    delete_http(card_name);

                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                    return true;
                }
            });
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

            if (update){
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
        else{
            String[] value ={"尚未加入信用卡"};
            ListAdapter adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,value);
            showcards.setAdapter(adapter);
        }
    }
    private void delete_http(String card){
        String url = "http://35.194.203.57/connectdb/delete_user_card.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        postData.put("card_id",card);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.equals("success")){
                    Toast.makeText(getActivity(), "刪除成功!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "刪除失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        readTask.execute(url);
    }

    private void http(final Boolean update){
        String url = "http://35.194.203.57/connectdb/get_my_card.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.equals("{\"data\":null}")){
                    showdata(false);
                }
                else {
                    try{
                        card_count =0;
                        JSONObject init_title = new JSONObject(s);
                        JSONArray data = init_title.getJSONArray("data");
                        mylistdata = new ArrayList<String>();
                        listviewdata = new String[data.length()];
                        max_cost = new String[data.length()];
                        countdate = new String[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            card_count +=1;
                            JSONObject jasondata = data.getJSONObject(i);
                            listviewdata[i] = jasondata.getString("card_id");
                            mylistdata.add(jasondata.getString("card_id"));
                            max_cost[i] = jasondata.getString("max_cost");
                            countdate[i] = jasondata.getString("date");
                            Log.e("data",listviewdata[i]);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showdata(update);
                }
            }
        });
        readTask.execute(url);
    }
}
