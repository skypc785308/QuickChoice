package com.mis.ncyu.quickchoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Logined_menu extends AppCompatActivity {

    private GridView gridView;
    private int[] image = {
            R.drawable.mycard, R.drawable.news, R.drawable.history,
            R.drawable.recommend, R.drawable.mushroom
    };
    private String[] imgText = {
            "我的信用卡", "最新優惠", "歷史紀錄" , "立即推薦", "測試用"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logined_menu);
        getlogin_name();

        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < image.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", image[i]);
            item.put("text", imgText[i]);
            items.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this,
                items, R.layout.grid_item_logined_menu, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});
        gridView = (GridView)findViewById(R.id.main_page_gridview);
        gridView.setNumColumns(3);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Logined_menu.this, "你選擇了" + imgText[position], Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        Intent intent = new Intent();
                        intent.setClass(Logined_menu.this, My_card.class);
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        Intent intent2 = new Intent();
                        intent2.setClass(Logined_menu.this, connectdb.class);
                        startActivity(intent2);
                        break;
                }

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, m);
        return super.onCreateOptionsMenu(m);
    }

    public  void getlogin_name(){
        String userid;
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            userid = context.getString("帳號");
            TextView output = (TextView) findViewById(R.id.textView8);
            output.setText(userid);
        }
    }
}
