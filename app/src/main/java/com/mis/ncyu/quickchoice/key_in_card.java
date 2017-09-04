package com.mis.ncyu.quickchoice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class key_in_card extends AppCompatActivity implements View.OnClickListener {
    private EditText etBirthday;
    private EditText card_number;
    private EditText expir;
    private Calendar m_Calendar = Calendar.getInstance();
    private String card;
    private String username;
    private String state;
    int MY_SCAN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_in_card);
        card = get_context();
        etBirthday = (EditText) findViewById(R.id.type);
        card_number = (EditText) findViewById(R.id.card_number);
        expir = (EditText) findViewById(R.id.expir);
        etBirthday.setOnClickListener(this);

        Button ok = (Button)findViewById(R.id.okbtn);
        ok.setOnClickListener(signuplisten);
        onScanPress();
    }
    View.OnClickListener signuplisten = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText maxcost = (EditText) findViewById(R.id.key_word);
            OkHttpClient mOkHttpClient=new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("maxcost",maxcost.getText().toString())
                    .add("userid",username)
                    .add("cardid",card)
                    .add("date",etBirthday.getText().toString())
                    .add("card_number",card_number.getText().toString())
                    .add("expir",expir.getText().toString())
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("http://35.194.203.57/connectdb/add_new_card.php")
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
                            Intent intent = new Intent(key_in_card.this, new_home2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    };

    public void onScanPress() {
        Intent scanIntent = new Intent(key_in_card.this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE,"zh-Hant_TW");
        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            Bitmap cardTypeImage = null;
            String resultDisplayStr;
            String expirdate="";
            String getcard_number="";
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                getcard_number = scanResult.getRedactedCardNumber();
                CardType cardType = scanResult.getCardType();
                cardTypeImage = cardType.imageBitmap(this);

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    expirdate= scanResult.expiryMonth + "/" + scanResult.expiryYear;

                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            expir.setText(expirdate);
            card_number.setText(getcard_number);
        }
        // else handle other activity results
    }


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
        return card;
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
