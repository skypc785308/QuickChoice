package com.mis.ncyu.quickchoice;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by UserMe on 2017/8/19.
 */

public class compute_red extends Fragment  {
    public compute_red() {}
    private String username;
    private String perfer;
    private String pos;
    private ListView listV;
    private List<card_datatype> card_list;
    private mylistadapter adapter;
    private result_type[] mresult_types;
    private int index = 0;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int times = 0;
    private List<Total_data> data;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        username =((compute_recommend)getActivity()).put_user_name();
        pos = ((compute_recommend)getActivity()).put_pos();
        data = ((compute_recommend)getActivity()).put_data();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compute_red,container,false);
        get_wanted_data();
        RecycleAdapter myAdapter = new RecycleAdapter(card_list);
        RecyclerView mList = (RecyclerView) view.findViewById(R.id.list_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(myAdapter);
        return view;
    }
    public void get_wanted_data(){
        card_list = new ArrayList<>();
        for (int i=0;i<data.size();i++){
            Total_data row = data.get(i);
            String card = row.getCard_name();
            String bank = row.getCard_bank();
            String red = row.getRed();
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(red);
            String clean = m.replaceAll("");
            String value = clean.replaceAll("元／點","");
            if (value.equals("null")){
                value="0.0";
            }
            card_list.add(new card_datatype(bank,card,clean,Double.valueOf(value)));
        }
        for(int i=0;i<card_list.size();i++){
            for(int j=0;j<card_list.size()-1;j++){
                if (card_list.get(j).getValue() < card_list.get(j+1).getValue()){
                    Collections.swap(card_list,j,j+1);
                }
            }
        }
    }
}
