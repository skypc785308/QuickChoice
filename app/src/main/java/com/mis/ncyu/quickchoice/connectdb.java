package com.mis.ncyu.quickchoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class connectdb extends AppCompatActivity {

    private String showUri = "http://192.168.1.143/connectdb/connow.php";
    private TextView prs1 = (TextView) findViewById(R.id.showdata);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectdb);

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showUri, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray data = response.getJSONArray("data");
                    //這邊要和上面json的名稱一樣
                    //下邊是把全部資料都印出來
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                        String account = jasondata.getString("userID");
                        String pwd = jasondata.getString("userPW");
                        prs1.append(account + " " + pwd + " " + " \n");
                    }
                    prs1.append("===\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}

