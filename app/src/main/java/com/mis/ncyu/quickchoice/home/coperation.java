package com.mis.ncyu.quickchoice.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by UserMe on 2017/9/26.
 */

public class coperation extends Fragment {

    public coperation(){}

    ListView list;
    List<HashMap<String , String>> default_list_data = new ArrayList<>();
    List<HashMap<String , String>> list_data = new ArrayList<>();
    private String[] storedata;
    private String[] detaildata;
    private Integer[] ids;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coperation,container,false);
        list = (ListView)view.findViewById(R.id.cop_list);
        HashMap<String , String> hashMap = new HashMap<>();
        hashMap.put("title" , "沒有銀行資料");
        hashMap.put("text" , "趕快新增卡片");
        default_list_data.add(hashMap);
        ListAdapter adapter = new SimpleAdapter(getActivity(),default_list_data, android.R.layout.simple_list_item_2 ,new String[]{"title" , "text"} ,
                new int[]{android.R.id.text1 , android.R.id.text2});
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), context_coperation.class);
                Bundle context = new Bundle();
                context.putString("ids", String.valueOf(ids[position]));
                intent3.putExtras(context);
                startActivity(intent3);
            }
        });
        http();
        return view;
    }


    private void http(){
        String url = "http://35.194.203.57/connectdb/get_coperation.php";
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(), new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                    try{
                        JSONObject init_title = new JSONObject(s);
                        JSONArray data = init_title.getJSONArray("data");
                        storedata = new String[data.length()];
                        detaildata = new String[data.length()];
                        ids = new Integer[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jasondata = data.getJSONObject(i);
                            storedata[i] = jasondata.getString("used_card_name");
                            detaildata[i] = jasondata.getString("used_card_time");
                            ids[i] = Integer.valueOf(jasondata.getString("used_card_id"));
                            HashMap<String , String> hashMap = new HashMap<>();
                            hashMap.put("title" , storedata[i]);
                            hashMap.put("text" , detaildata[i]);
                            list_data.add(hashMap);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                ListAdapter adapter = new SimpleAdapter(getActivity(),list_data, android.R.layout.simple_list_item_2 ,new String[]{"title" , "text"} ,
                        new int[]{android.R.id.text1 , android.R.id.text2});
                list.setAdapter(adapter);
            }
        });
        readTask.execute(url);
    }

}
