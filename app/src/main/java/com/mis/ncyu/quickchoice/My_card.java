package com.mis.ncyu.quickchoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class My_card extends AppCompatActivity {
    private  RequestQueue RequestQueue;
    private Context Context;
    private String[] listviewdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        VolleyJsonArrayPost();
    }
    private void VolleyJsonArrayPost() {
        Context = this; // Context = getActivity();
        RequestQueue = Volley.newRequestQueue(Context);
        String url = "http://10.3.204.7/connectdb/get_card.php"; //Server URL

        JsonObjectRequest JsonArrayPost = new JsonObjectRequest (Request.Method.POST,url,
                new Response.Listener<JSONObject >() {
                    @Override
                    public void onResponse(JSONObject  response) {
                        try{
                            JSONArray data = response.getJSONArray("data");
                            listviewdata = new String[data.length()];
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jasondata = data.getJSONObject(i);
                                listviewdata[i] = jasondata.getString("name");
                                Log.e("data",listviewdata[i]);
                            }
                            show_in_list_view();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("err","errrrrrrrrrrrrrrrr");
            }
        });
        RequestQueue.add(JsonArrayPost);
    }

    public void show_in_list_view(){
        ListView showcards = (ListView) findViewById(R.id.showcards);

        String[] values = listviewdata;
        ListAdapter adapter = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1 ,values);

        showcards.setAdapter(adapter);
    }
}
