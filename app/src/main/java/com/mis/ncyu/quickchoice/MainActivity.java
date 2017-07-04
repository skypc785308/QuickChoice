package com.mis.ncyu.quickchoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Button btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(btnlisten);
        Button sign_button = (Button)findViewById(R.id.button4);
        sign_button.setOnClickListener(signlisten);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, m);
        return super.onCreateOptionsMenu(m);
    }

    View.OnClickListener btnlisten = new View.OnClickListener() {
        public void onClick(View v){
            Toast.makeText(MainActivity.this,"大家好",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Logined_menu.class);
            EditText login_user = (EditText)findViewById(R.id.edit_id);
            Bundle context = new Bundle();
            context.putString("帳號", login_user.getText().toString());
            intent.putExtras(context);
            startActivity(intent);
        }
    };

    View.OnClickListener signlisten = new View.OnClickListener(){
      public void onClick(View v){
          Intent intent = new Intent(MainActivity.this, sign_new.class);
          startActivity(intent);
      }
    };

}
