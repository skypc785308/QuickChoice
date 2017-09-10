package com.mis.ncyu.quickchoice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by UserMe on 2017/9/10.
 */

public class compute_history extends Fragment {

    List<card_datatype> history;
    RecyclerView mList;

    public compute_history() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compute_history,container,false);
        mList = (RecyclerView) view.findViewById(R.id.list_view);
        history = new ArrayList<>();
        getHistory();
        return view;
    }

    private void getHistory(){
        String url = "http://35.194.203.57/connectdb/get_sum_history.php";
        HashMap postData = new HashMap();
        postData.put("userid",((compute_recommend)getActivity()).put_user_name());
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("data_history",s);
                try{
                    JSONObject init_title = new JSONObject(s);
                    JSONArray data = init_title.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                        String card = jasondata.getString("card_id");
                        String sum = jasondata.getString("total");
                        if (sum.equals("null")){
                            sum="0.0";
                        }
                        history.add(new card_datatype("",card,"",Double.valueOf(sum)));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                RecycleHistoryAdapter myAdapter = new RecycleHistoryAdapter(history);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mList.setLayoutManager(layoutManager);
                mList.setAdapter(myAdapter);
            }
        });
        readTask.execute(url);
    }
}
