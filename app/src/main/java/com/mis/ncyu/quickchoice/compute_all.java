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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    List<card_datatype> card_list,ranking;
    result_type[] mresult_types;
    private List<Total_data> data;
    int[] typetimes = new int[]{9,18,25,36,14};
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
        RecycleAdapter myAdapter = new RecycleAdapter(ranking);
        RecyclerView mList = (RecyclerView) view.findViewById(R.id.list_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(myAdapter);
        return view;
    }
    public void get_wanted_data(){
        card_list = new ArrayList<>();
        ranking = new ArrayList<>();
        for (int i=0;i<data.size();i++){
            Total_data row = data.get(i);
            String card = row.getCard_name();
            String bank = row.getCard_bank();
            String buy = row.getBuy();
            String movie = row.getMovie();
            String oil = row.getCard_offer();
            String red = row.getRed();
            String plane = row.getPlane();
            String store = row.getStore();
            if (buy.equals("null")){
                buy="0.0";
            }
            else {
                buy = buy.replaceAll("%","");
            }
            if (movie.equals("null")){
                movie="0.0";
            }
            else {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(movie);
                String clean = m.replaceAll("");
                String value = clean.replaceAll("折","");
                movie = value;
            }
            if (oil.equals("null")){
                oil="0.0";
            }
            else {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(oil);
                String clean = m.replaceAll("");
                String value = clean.replaceAll("元 / 公升","");
                oil = value;
            }
            if (red.equals("null")){
                red="0.0";
            }
            else {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(red);
                String clean = m.replaceAll("");
                String value = clean.replaceAll("元／點","");
                red = value;
            }
            if (plane.equals("null")){
                plane="0.0";
            }
            else {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(plane);
                String clean = m.replaceAll("");
                String value = clean.replaceAll("元 / 哩","");
                plane = value;
            }
            int[] typetimes = new int[compute_recommend.timess.length];
            for(int j=0;j<compute_recommend.timess.length;j++){
                typetimes[j]=compute_recommend.timess[j]*compute_recommend.timess[j]/50;
            }
            Double sum = Double.valueOf(buy)*typetimes[0]+Double.valueOf(oil)*typetimes[1]+Double.valueOf(red)*typetimes[2]+
                    Double.valueOf(plane)*typetimes[3]+Double.valueOf(movie)*typetimes[4];
            card_datatype record=new card_datatype(bank,card,"計算結果",sum);
            if(!store.equals("null")){
                record.setStore(store);
            }
            card_list.add(record);
        }
        for(int i=0;i<card_list.size();i++){
            card_datatype row = card_list.get(i);
            if(row.getStore() !=null){
                ranking.add(row);
            }
        }
        for(int i=0;i<card_list.size();i++){
            for(int j=0;j<card_list.size()-1;j++){
                if (card_list.get(j).getValue() < card_list.get(j+1).getValue()){
                    Collections.swap(card_list,j,j+1);
                }
            }
        }
        for(int i=0;i<card_list.size();i++){
            card_datatype row = card_list.get(i);
            ranking.add(row);
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
