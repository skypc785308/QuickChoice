package com.mis.ncyu.quickchoice;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Recommend extends Fragment {


    public Recommend() {
        // Required empty public constructor
    }
    private String username;
    private Button btn;
    ArrayAdapter adapter;
    private EditText filterText = null;
    String [] pos = {"家樂福","新光三越","大遠百","秀泰廣場","陶板屋","UNIQUI","NET","星巴克","野宴","逐鹿"};


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        username =((new_home2)getActivity()).put_login_name();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend,container,false);
        ListView listpos = (ListView) view.findViewById(R.id.findpos);
        btn = (Button)view.findViewById(R.id.recommend_btn);
        filterText = (EditText) view.findViewById(R.id.searchbox);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 啟動地圖元件用的Intent物件
                Intent intentMap = new Intent(getActivity(), MapsActivity.class);
                // 啟動地圖元件
                startActivity(intentMap);
            }
        });

        filterText.addTextChangedListener(filterTextWatcher);
        listpos.setTextFilterEnabled(true);
        adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,pos);
        listpos.setAdapter(adapter);
        listpos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), pos[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Choice_recommend_type.class);
                Bundle context = new Bundle();
                context.putString("pos",pos[position]);
                context.putString("user_name", username);

                intent.putExtras(context);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        filterText.removeTextChangedListener(filterTextWatcher);
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

}
