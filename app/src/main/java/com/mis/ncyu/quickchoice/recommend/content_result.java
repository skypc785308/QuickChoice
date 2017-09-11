package com.mis.ncyu.quickchoice.recommend;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mis.ncyu.quickchoice.MyDBHelper;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.Total_data;

import java.util.List;

public class content_result extends AppCompatActivity {

    List<Total_data> mTotal_data;
    TextView card,bank,type,key_word;
    String card_name,bank_name,key;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_result);
        mTotal_data = compute_recommend.mTotal_data;
        card = (TextView)findViewById(R.id.card_name);
        bank = (TextView)findViewById(R.id.bank_name);
        type = (TextView)findViewById(R.id.type);
        key_word = (TextView)findViewById(R.id.key_word);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginame = "";
                MyDBHelper helper = MyDBHelper.getInstance(content_result.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM login",null);
                if (c.getCount()>0){
                    c.moveToFirst();
                    loginame = c.getString(1);
                    c.close();
                    db.close();
                }
                else {
                    finish();
                }
                Intent intent = new Intent(content_result.this, key_in_recommend.class);
                Bundle context = new Bundle();
                context.putString("card_name",card_name);
                context.putString("pos",compute_recommend.pos);
                context.putString("user_name",loginame);
                intent.putExtras(context);
                v.getContext().startActivity(intent);
            }
        });
        getcontent();


    }
    public void compute_times(){
        for (int i=0;i<mTotal_data.size();i++){
            Total_data row = mTotal_data.get(i);
            if(row.getCard_name().equals(card_name)){
                row.setTimes(row.getTimes()+1);
            }
        }
    }

    public void getcontent(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            card_name = context.getString("card");
            bank_name = context.getString("bank");
            key = context.getString("key_word");
        }
        String type ="";
        switch (compute_recommend.fragment_pos){
            case 0:
                type="綜合優惠";
                break;
            case 1:
                type="現金回饋";
                break;
            case 2:
                type="加油";
                break;
            case 3:
                type="紅利積點";
                break;
            case 4:
                type="旅遊";
                break;
            case 5:
                type="電影";
                break;
        }
        this.type.setText(type);
        bank.setText(bank_name);
        card.setText(card_name);
        key_word.setText(key);
    }
}
