package com.mis.ncyu.quickchoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class My_card extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        show_in_list_view();
    }
    public void show_in_list_view(){
        ListView showcards = (ListView) findViewById(R.id.showcards);

        String[] values = new String[]{
                "Apple",
                "Banana",
                "Cat",
                "Dog"
        };
        ListAdapter adapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 ,values);

        showcards.setAdapter(adapter);
    }
}
