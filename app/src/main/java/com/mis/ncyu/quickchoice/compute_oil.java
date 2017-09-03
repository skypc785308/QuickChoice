package com.mis.ncyu.quickchoice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class compute_oil extends Fragment {

    private String username;
    private String perfer;
    private String pos;
    private ListView listV;
    List<card_datatype> card_list;
    result_type[] mresult_types;
    private mylistadapter adapter;

    public compute_oil() {}



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username =((compute_recommend)getActivity()).put_user_name();

        pos = ((compute_recommend)getActivity()).put_pos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.e("oil","oil");
        View view = inflater.inflate(R.layout.fragment_compute_oil,container,false);
        listV=(ListView)view.findViewById(R.id.show_all_list);
        card_list = new ArrayList<card_datatype>();


        return view;
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
            card_list.add(new card_datatype(r.toString(),mresult_types[i].getName(),mresult_types[i].getKeyword(),0.123));
        }
        showdata();
        return Boolean.TRUE;
    }

    public void showdata(){
        adapter = new mylistadapter(getActivity(),card_list);
        listV.setAdapter(adapter);
    }

    public void shownodata(){
        card_list.add(new card_datatype("none","沒有卡片啦~","快去新增!!",0.123456789));
        adapter = new mylistadapter(getActivity(),card_list);
        listV.setAdapter(adapter);
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/oil_compute.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        postData.put("pos",pos);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("ewewe",s);
//                if(s.equals("{\"data\":null}")){
//                    shownodata();
//                }
//                else {
//                    int index = 0;
//                    try{
//                        JSONObject init_title = new JSONObject(s);
//                        JSONArray data = init_title.getJSONArray("data");
//                        index = data.length();
//                        mresult_types = new result_type[index];
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject jasondata = data.getJSONObject(i);
//                            String card =jasondata.getString("card_name");
//                            String keyword =jasondata.getString("card_offer");
//                            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//                            Matcher m = p.matcher(keyword);
//                            String clean = m.replaceAll("");
//                            mresult_types[i] = new result_type();
//                            mresult_types[i].setName(card);
//                            mresult_types[i].setKeyword(clean,"元 / 公升");
//                            Log.e("ewewe",mresult_types[i].getKeyword());
//                        }
//                    }catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    compute(index);
//                }
            }
        });
        readTask.execute(url);
    }

}
