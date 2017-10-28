package com.mis.ncyu.quickchoice.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.RecycleAdapter;
import com.mis.ncyu.quickchoice.Total_data;
import com.mis.ncyu.quickchoice.card_datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by UserMe on 2017/9/4.
 */

public class compute_plane extends Fragment {
    public compute_plane(){}

    private String username;
    private String pos;
    List<card_datatype> card_list;
    private List<Total_data> data;
    private ListView listV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username =((compute_recommend)getActivity()).put_user_name();
        pos = ((compute_recommend)getActivity()).put_pos();
        data = ((compute_recommend)getActivity()).put_data();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compute_movie,container,false);
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
            String plane = row.getPlane();
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(plane);
            String clean = m.replaceAll("");
            String value = clean.replaceAll("元/哩","");
            if (value.equals("null")){
                value="0.0";
                clean="無";
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
