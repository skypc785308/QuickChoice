package com.mis.ncyu.quickchoice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class key_in_card extends AppCompatActivity implements View.OnClickListener {
    private EditText etBirthday = null;
    private Calendar m_Calendar = Calendar.getInstance();
    private String card;
    private String username;
    private String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_in_card);
        card = get_context();
        etBirthday = (EditText) findViewById(R.id.date);
        etBirthday.setOnClickListener(this);
        Button ok = (Button)findViewById(R.id.okbtn);
        ok.setOnClickListener(signuplisten);
    }
    View.OnClickListener signuplisten = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText maxcost = (EditText) findViewById(R.id.max_cost);
            TextView output = (TextView) findViewById(R.id.textView8);
            OkHttpClient mOkHttpClient=new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("maxcost",maxcost.getText().toString())
                    .add("userid",username)
                    .add("cardid",card)
                    .add("date",etBirthday.getText().toString())
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("http://192.168.1.143/connectdb/add_new_card.php")
                    .post(formBody)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("data","GGGGG");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    state= response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (state=="success"){
                                Toast.makeText(key_in_card.this, "新增成功", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(key_in_card.this, Logined_menu.class);
                            Bundle context = new Bundle();
                            context.putString("user_name", username);
                            intent.putExtras(context);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
    };

    public String get_context(){
        String card_id = null;
        String bank_name = null;
        String card = null;
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            card = context.getString("card");
            card_id = context.getString("id");
            bank_name = context.getString("bankname");
            username = context.getString("user_name");
        }
        EditText setbankid = (EditText) findViewById(R.id.bank_name);
        EditText setbankname = (EditText) findViewById(R.id.bank);
        setbankid.setText(card);
        setbankname.setText(bank_name);
        return card_id;
    }

    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener()
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            m_Calendar.set(Calendar.YEAR, year);
            m_Calendar.set(Calendar.MONTH, monthOfYear);
            m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            etBirthday.setText(sdf.format(m_Calendar.getTime()));
        }
    };
    @Override
    public void onClick(View view) {
        if (view.getId() == etBirthday.getId())
        {
            DatePickerDialog dialog =
                    new DatePickerDialog(key_in_card.this,
                            datepicker,
                            m_Calendar.get(Calendar.YEAR),
                            m_Calendar.get(Calendar.MONTH),
                            m_Calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }
}
