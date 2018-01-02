package com.mis.ncyu.quickchoice.recommend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.RecycleHistoryAdapter;
import com.mis.ncyu.quickchoice.Total_data;
import com.mis.ncyu.quickchoice.card_datatype;
import com.mis.ncyu.quickchoice.home.target_set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by UserMe on 2017/9/10.
 */

public class compute_history extends Fragment {

    List<card_datatype> history;
    RecyclerView mList;
    private final OkHttpClient client = new OkHttpClient();

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
                if (s.equals("{\"data\":null}")){

                }
                else {
                    try{
                        JSONObject init_title = new JSONObject(s);
                        JSONArray data = init_title.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jasondata = data.getJSONObject(i);
                            String card = jasondata.getString("card_id");
                            String sum = jasondata.getString("total");
                            int red_value = jasondata.getInt("red");
                            if (sum.equals("null")){
                                sum="0.0";
                            }
                            card_datatype T = new card_datatype("",card,"",Double.valueOf(sum));
                            T.setRed_value(red_value);
                            history.add(T);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                RecycleHistoryAdapter myAdapter = new RecycleHistoryAdapter(history);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mList.setLayoutManager(layoutManager);
                mList.setAdapter(myAdapter);
                get_target();
            }
        });
        readTask.execute(url);
    }

    private void get_target(){
        String url = "http://35.194.203.57/connectdb/update_target.php";
        HashMap postData = new HashMap();
        postData.put("userid",((compute_recommend)getActivity()).put_user_name());
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("data_history",s);
                if (s.equals("null")){

                }
                else {
                    try{
                        JSONObject init_title = new JSONObject(s);
                        String card=init_title.getString("card_id");
                        String type = init_title.getString("target_type");
                        int value = init_title.getInt("target_value");
                        for(int i=0;i<history.size();i++){
                            if (history.get(i).getname().equals(card)){

                                card_datatype T = history.get(i);
                                if(type.equals("紅利")){
                                    int red_now = T.getRed_value();

                                    int card_red_value = 0;
                                    List<Total_data> data = ((compute_recommend)getActivity()).put_data();
                                    for (int ik=0;ik<data.size();i++) {
                                        Total_data row = data.get(ik);
                                        if (row.getCard_name().equals(card)){
                                            String red = row.getRed();
                                            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                                            Matcher m = p.matcher(red);
                                            String clean = m.replaceAll("");
                                            String red_value = clean.replaceAll("元／點", "");
                                            if (red_value.equals("null")) {
                                                red_value = "0.0";
                                            }
                                            card_red_value = Integer.parseInt(red_value);
                                            int SUM = compute_recommend.money/card_red_value;
                                            if(red_now + SUM > value){
                                                showAboutDialog(card,type,value,SUM);
                                                Toast.makeText(getActivity(), "偵測到目標優惠，紅利", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        }
                                    }
                                }
                                else if(type.equals("刷卡金額")){
                                    int total_cost = T.getValue().intValue();
                                    if(total_cost + compute_recommend.money > value){
                                        showAboutDialog(card,type,value,compute_recommend.money);
                                        Toast.makeText(getActivity(), "偵測到目標優惠，刷卡金額", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        readTask.execute(url);
    }

    private void showAboutDialog(String card_name,String target_name,int value, int get_value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        StringBuilder b = new StringBuilder("如果再使用");
        b.append(card_name);
        b.append("\n");
        b.append("可以獲得"+String.valueOf(get_value)+"目標值");
        b.append("\n");
        b.append("就可以達成目標：");
        b.append(target_name+"到"+String.valueOf(value));
        b.append("\n");
        builder.setTitle("目標提醒");
        builder.setMessage(b.toString());
        builder.setPositiveButton("確定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }
        );
        builder.show();
    }

}
