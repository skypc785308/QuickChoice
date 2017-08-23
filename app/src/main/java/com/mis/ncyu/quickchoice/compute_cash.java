package com.mis.ncyu.quickchoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * Created by UserMe on 2017/8/22.
 */

public class compute_cash extends Fragment {

    public compute_cash() {}

    private String username;
    private String perfer;
    private String pos;
    private ListView listV;
    private List<card_datatype> card_list;
    private mylistadapter adapter;
    private result_type[] mresult_types;
    private int index = 0;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        username =((compute_recommend)getActivity()).put_user_name();
        perfer = ((compute_recommend)getActivity()).put_perfer();
        pos = ((compute_recommend)getActivity()).put_pos();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compute_red,container,false);
        listV=(ListView)view.findViewById(R.id.show_money_list);
        card_list = new ArrayList<card_datatype>();
        http();
        return view;
    }
    public void shownodata(){
        card_list.add(new card_datatype("none","沒有卡片啦~","快去新增!!",12345679));
        adapter = new mylistadapter(getActivity(),card_list);
        listV.setAdapter(adapter);
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
            Integer value = mresult_types[i].getKey();
            card_list.add(new card_datatype(r.toString(),mresult_types[i].getName(),mresult_types[i].getKeyword(),value));
        }
        showdata();
        return Boolean.TRUE;
    }

    public void showdata(){
        adapter = new mylistadapter(getActivity(),card_list);
        listV.setAdapter(adapter);
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), key_in_recommend.class);
                Bundle context = new Bundle();
                context.putString("card_name", mresult_types[index-1-i].getName());
                context.putString("user_name", username);
                context.putString("pos", pos);
                context.putString("type", "red");
                intent.putExtras(context);
                startActivity(intent);

            }
        });
    }

    private void http(){
        String url = "http://35.194.203.57/connectdb/cash_compute.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        postData.put("pos",pos);
        postData.put("perfer",perfer);
        Log.e("ewewe",username);
        Log.e("ewewe",pos);
        Log.e("ewewe",perfer);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("ewewe",s);
                if(s.equals("{\"data\":null}")){
                    shownodata();
                }
                else {
                    try{
                        JSONObject init_title = new JSONObject(s);
                        JSONArray data = init_title.getJSONArray("data");
                        index = data.length();
                        mresult_types = new result_type[index];
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jasondata = data.getJSONObject(i);
                            String card =jasondata.getString("card_name");
                            mresult_types[i] = new result_type();
                            mresult_types[i].setName(card);
                            String keyword = jasondata.getString("card_domestic");
                            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                            Matcher m = p.matcher(keyword);
                            String clean = m.replaceAll("");
                            mresult_types[i].setKeyword(clean.replace("%",""));
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
