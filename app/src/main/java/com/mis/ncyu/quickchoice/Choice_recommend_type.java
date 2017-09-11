package com.mis.ncyu.quickchoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mis.ncyu.quickchoice.recommend.compute_recommend;

public class Choice_recommend_type extends AppCompatActivity {

    private String username;
    private String pos;
    private Button btn,btn2,start;
    private AlertDialog dialog;
    private TextView show;
    EditText cash,dis,red,mile;

    public  void get_pos_user(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            username = context.getString("user_name");
            pos = context.getString("pos");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_recommend_type);
        get_pos_user();

        show = (TextView)findViewById(R.id.show_which);
        btn2 = (Button)findViewById(R.id.which);
        start = (Button) findViewById(R.id.startthing);
         cash = (EditText)findViewById(R.id.editText3);
         dis = (EditText)findViewById(R.id.editText2);
         red = (EditText)findViewById(R.id.editText6);
        mile = (EditText)findViewById(R.id.editText4);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                Intent intent = new Intent(Choice_recommend_type.this,compute_recommend.class);
                data.putString("user_name",username);
                data.putString("perfer",show.getText().toString());
                data.putString("pos",pos);
                data.putString("cash",cash.getText().toString());
                data.putString("dis",dis.getText().toString());
                data.putString("red",red.getText().toString());
                data.putString("mile",mile.getText().toString());
                intent.putExtras(data);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String [] options = {"現金優惠","折扣","紅利積點","刷卡里程"};
        builder.setNegativeButton("取消", null);
        builder.setTitle("選擇偏好優惠").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:show.setText("現金優惠");
                        break;
                    case 1:show.setText("折扣");
                        break;
                    case 2:show.setText("紅利積點");
                        break;
                    case 3:show.setText("刷卡里程");
                        break;
                }
            }
        });
        dialog = builder.create();
    }
}
