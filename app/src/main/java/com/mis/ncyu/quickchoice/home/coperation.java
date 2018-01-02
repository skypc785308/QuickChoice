package com.mis.ncyu.quickchoice.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.StoreInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    String[] end_date;
    private Long[] end_day;
    private Integer[] ids;
    SimpleAdapter adapter;
    private EditText filterText = null;
    String username;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        username =((new_home2)getActivity()).put_login_name();
        HashMap<String , String> hashMap = new HashMap<>();
        hashMap.put("title" , "沒有銀行資料");
        hashMap.put("text" , "趕快新增卡片");
        default_list_data.add(hashMap);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coperation,container,false);
        list = (ListView)view.findViewById(R.id.cop_list);
        filterText = (EditText) view.findViewById(R.id.search);
        adapter = new SimpleAdapter(getActivity(),default_list_data, android.R.layout.simple_list_item_2 ,new String[]{"title" , "text"} ,
                new int[]{android.R.id.text1 , android.R.id.text2});
        list.setAdapter(adapter);
        filterText.addTextChangedListener(filterTextWatcher);
        list.setTextFilterEnabled(true);

        http();
        return view;
    }
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getFilter().filter(s);
        }

    };


    private void http(){
        String url = "http://35.194.203.57/connectdb/get_coperation.php";
        HashMap postData = new HashMap();
        postData.put("userid",username);
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(),postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("取得信用卡的銀行", s);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String date=sdf.format(new java.util.Date());
                Date date_now= new Date();
                try {
                    date_now = sdf.parse(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long data_now_time = date_now.getTime();
                if(s.equals("{\"data\":null}")){
                    list_data = new ArrayList<>();
                    HashMap<String , String> hashMap = new HashMap<>();
                    hashMap.put("title" ,"沒有信用卡");
                    hashMap.put("text" , "請新增卡片");
                    list_data.add(hashMap);
                    adapter = new SimpleAdapter(getActivity(),list_data, android.R.layout.simple_list_item_2 ,new String[]{"title" , "text"} ,
                            new int[]{android.R.id.text1 , android.R.id.text2});
                    list.setAdapter(adapter);
                }
                else {
                    try{
                        default_list_data = new ArrayList<>();
                        list_data = new ArrayList<>();
                        JSONObject init_title = new JSONObject(s);
                        JSONArray data = init_title.getJSONArray("data");
                        storedata = new String[data.length()];
                        detaildata = new String[data.length()];
                        end_day  = new Long[data.length()];
                        end_date  = new String[data.length()];
                        ids = new Integer[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jasondata = data.getJSONObject(i);
                            storedata[i] = jasondata.getString("used_card_name");
                            detaildata[i] = jasondata.getString("used_card_detail");
                            end_date[i] = jasondata.getString("end_date");
                            Long day = Long.valueOf(0);
                                try {
                                    Date get_date=sdf.parse(end_date[i]);
                                    Long date_get=get_date.getTime();
                                    Long timeP = date_get - data_now_time;
                                    day = timeP/(1000*60*60*24);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            end_day[i] = day;
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }


                    for(int i=0; i<end_day.length;i++){
                        for(int j=0;j<end_day.length-1;j++){
                            if (end_day[j] < end_day[j+1]){
                                Long T = end_day[j];
                                end_day[j] = end_day[j+1];
                                end_day[j+1] = T;
                                String T_name = storedata[j];
                                storedata[j] = storedata[j+1];
                                storedata[j+1] = T_name;
                                String D_tial = detaildata[j];
                                detaildata[j] = detaildata[j+1];
                                detaildata[j+1] = D_tial;
                                String E_data = end_date[j];
                                end_date[j] = end_date[j+1];
                                end_date[j+1] = E_data;
                            }
                        }
                    }
                    for(int i=0; i<end_day.length;i++){
                        HashMap<String , String> hashMap = new HashMap<>();
                        hashMap.put("title" , storedata[i]);
                        hashMap.put("text" , "距離結束還有： "+end_day[i].toString() + " 天");
                        list_data.add(hashMap);
                    }
                    adapter = new SimpleAdapter(getActivity(),list_data, android.R.layout.simple_list_item_2 ,new String[]{"title" , "text"} ,
                            new int[]{android.R.id.text1 , android.R.id.text2});
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent3 = new Intent();
                            intent3.setClass(getActivity(), context_coperation.class);
                            Bundle context = new Bundle();
                            context.putString("get_data", end_date[position]);
                            context.putString("get_pos", storedata[position]);
                            context.putString("get_detial", detaildata[position]);
                            intent3.putExtras(context);
                            startActivity(intent3);
                        }
                    });

                }

            }
        });
        readTask.execute(url);
    }

}
