package com.mis.ncyu.quickchoice.recommend;

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

import com.mis.ncyu.quickchoice.Levenshtein;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.RecycleAdapter;
import com.mis.ncyu.quickchoice.StoreInfo;
import com.mis.ncyu.quickchoice.Total_data;
import com.mis.ncyu.quickchoice.card_datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mis.ncyu.quickchoice.recommend.activity_recommend.jccard_user;

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
    int [] jaacrd_user_count = new int[6];
    Double jccard_sum = 0.0;
    List<card_datatype> card_list,ranking;
    private List<Total_data> data;
    int[] typetimes = new int[]{};

    private List<StoreInfo> store_info;

    public compute_all() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username =((compute_recommend)getActivity()).put_user_name();
        pos = ((compute_recommend)getActivity()).put_pos();
        data = ((compute_recommend)getActivity()).put_data();
        store_info =  ((compute_recommend)getActivity()).send_store_info();
        get_wanted_data();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compute_all,container,false);


        RecycleAdapter myAdapter = new RecycleAdapter(ranking);
        RecyclerView mList = (RecyclerView) view.findViewById(R.id.list_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(myAdapter);
        return view;
    }
    public Double compute(int x){
        Double compute = 0.0;
        Double y = Double.valueOf(x);
        compute =1/(1+300*Math.exp(-0.3*y));
//        if (x<=20){
//            compute = y/10;
//        }
//        else if (x<=60){
//            compute = y/8;
//        }
//        else if (x<=100){
//            compute = y/7;
//        }
//        else if (x>100){
//            compute = y/6;
//        }
        Log.e("公式計算結果",String.valueOf(compute));
        return compute;
    }
    public void get_wanted_data(){
        card_list = new ArrayList<>();
        ranking = new ArrayList<>();
        for (int i=0;i<data.size();i++) {
            Total_data row = data.get(i);
            String card = row.getCard_name();
            String bank = row.getCard_bank();
            String buy = row.getBuy();
            String movie = row.getMovie();
            String oil = row.getCard_offer();
            String red = row.getRed();
            String plane = row.getPlane();
            String store = row.getStore();
            if (buy.equals("null")) {
                buy = "0.0";
            } else {
                buy = buy.replaceAll("%", "");
            }
            if (movie.equals("null")) {
                movie = "0.0";
            } else {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(movie);
                String clean = m.replaceAll("");
                String value = clean.replaceAll("折", "");
                movie = value;
            }
            if (oil.equals("null")) {
                oil = "0.0";
            } else {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(oil);
                String clean = m.replaceAll("");
                String value = clean.replaceAll("元/公升", "");
                oil = value;
            }
            if (red.equals("null")) {
                red = "0.0";
            } else {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(red);
                String clean = m.replaceAll("");
                String value = clean.replaceAll("元／點", "");
                red = value;
            }
            if (plane.equals("null")) {
                plane = "0.0";
            } else {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(plane);
                String clean = m.replaceAll("");
                String value = clean.replaceAll("元/哩", "");
                plane = value;
            }
            Double compute_sum = 0.0;
            int[] typetimes = new int[compute_recommend.timess.length];
            for (int j = 0; j < compute_recommend.timess.length; j++) {
                typetimes[j] = compute_recommend.timess[j];
                compute_sum += typetimes[j];
            }
            jccard_sum = 0.0;
            for (int k=0;k<6;k++){
                jaacrd_user_count[k] =jccard_user[0][k] + jccard_user[1][k] + jccard_user[2][k];
                jccard_sum += jaacrd_user_count[k];
            }
            // 算出來的都是分類的權重，但是排序卻是卡片，所以這個權重只能用在顯示所有項目的排序。

            Double sum = Double.valueOf(buy) * compute(typetimes[0])*jaacrd_user_count[1] +
                    Double.valueOf(oil) * compute(typetimes[1])*jaacrd_user_count[2] +
                    Double.valueOf(red) * compute(typetimes[2])*jaacrd_user_count[3] +
                    Double.valueOf(plane) * compute(typetimes[3])*jaacrd_user_count[4] +
                    Double.valueOf(movie) * compute(typetimes[4])*jaacrd_user_count[5];
            Log.e("times_sum", String.valueOf(compute_sum));
            sum = sum / compute_sum / jccard_sum ;
            card_datatype record = new card_datatype(bank, card, "", sum);
            if (!store.equals("null")) {
                record.setStore(store);
            }
            card_list.add(record);
        }

        for(StoreInfo json : store_info) {
            Levenshtein lt = new Levenshtein();
            if(lt.getSimilarityRatio(pos, json.getStoreName())>0.3){
                for(int i=0;i<card_list.size();i++){
                    card_datatype row = card_list.get(i);
                    if(row.getBank().equals(json.getBank_name())){
                        int temp = row.getCoperation_num();
                        row.setCoperation_num(++temp);
                        StringBuilder context = new StringBuilder();
                        context.append(row.getCoperation_text());
                        context.append("\n");
                        context.append(json.getStoreName());
                        context.append("\n");
                        context.append(json.getStoreDetial());
                        row.setCoperation_text(context.toString());
                    }
                }
            }

        }
        int new_size = card_list.size();
        for(int i=0;i<new_size;i++){
            card_datatype row = card_list.get(i);
            if (row.getCoperation_num()>0){
                ranking.add(row);
                card_list.remove(i);
                i--;
                new_size--;
            }
        }


        // 進行排序
        for(int i=0;i<card_list.size();i++){
            for(int j=0;j<card_list.size()-1;j++){
                if (card_list.get(j).getValue() < card_list.get(j+1).getValue()){
                    Collections.swap(card_list,j,j+1);
                }
            }
        }
        // 將排序好的結果加到ranking_list
        ranking.addAll(card_list);
        activity_recommend.ranking_data = ranking;
    }
}
