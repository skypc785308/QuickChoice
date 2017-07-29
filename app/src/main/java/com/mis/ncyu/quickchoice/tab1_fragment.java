package com.mis.ncyu.quickchoice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by UserMe on 2017/7/25.
 */

public class tab1_fragment extends Fragment {

    private Button btntest;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);
        btntest = (Button)view.findViewById(R.id.btntst);
        btntest.setOnClickListener(btntestonclick);
        return view;
    }
    View.OnClickListener btntestonclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),"你已按下",Toast.LENGTH_SHORT).show();
        }
    };
}

