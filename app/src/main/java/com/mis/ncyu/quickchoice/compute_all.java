package com.mis.ncyu.quickchoice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by UserMe on 2017/8/6.
 */

public class compute_all extends Fragment {

    private String username;
    private String perfer;
    private String pos;
    private ListView listV;
    private Integer sum;
    private String cash;

    List<card_datatype> card_list;
    result_type[] mresult_types;
    private mylistadapter adapter;
    private List<Total_data> data;

    public compute_all() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username =((compute_recommend)getActivity()).put_user_name();
        pos = ((compute_recommend)getActivity()).put_pos();
        data = ((compute_recommend)getActivity()).put_data();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compute_all,container,false);
        get_wanted_data();
        listV=(ListView)view.findViewById(R.id.show_money_list);
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
            String buy = row.getBuy();
            card_list.add(new card_datatype(bank,card,buy,1000.0));
        }
    }

    public Boolean compute(int index){
        for (int i=0;i<index;i++) {
            for (int j = 0; j < index - 1; j++)
                if (mresult_types[j].getKey() > mresult_types[j + 1].getKey()) {
                    result_type t = mresult_types[j + 1];
                    mresult_types[j + 1] = mresult_types[j];
                    mresult_types[j] = t;
                }
        }
        for (Integer i=index-1;i>=0;i--){
            Integer r = index-i;
            Double value = mresult_types[i].getKey();
            card_list.add(new card_datatype(r.toString(),mresult_types[i].getName(),mresult_types[i].getKeyword(),value));
        }
        return Boolean.TRUE;
    }

}
