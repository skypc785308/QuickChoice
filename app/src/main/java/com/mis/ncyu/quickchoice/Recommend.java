package com.mis.ncyu.quickchoice;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recommend extends Fragment {


    public Recommend() {
        // Required empty public constructor
    }
    Integer expush=0;
    Integer pushtimes=0;
    Integer count = 0;
    ArrayList<Integer> record_count;
    ArrayList<LatLng> LatLngpos;
    ArrayList<String> pos_name;
    private String username;
    private Button btn;
    ArrayAdapter adapter;
    private EditText filterText = null;
    private MapView map;
    private GoogleMap mMap;
    Boolean readjson_finish = false;
    String [] pos = {"家樂福","新光三越","大遠百","秀泰廣場","陶板屋","UNIQLO","NET","星巴克","野宴","逐鹿炭火"};


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        username =((new_home2)getActivity()).put_login_name();
        LatLngpos = new ArrayList<LatLng>();
        pos_name = new ArrayList<String>();
        record_count = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend,container,false);

        map = (MapView)view.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                for (int i = 0; i < pos.length; i++) {
                    httpall(pos[i]);
                    if (i == pos.length - 1) {
                        map.onResume();
                    }
                }
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(getActivity(), Choice_recommend_type.class);
                        Bundle context = new Bundle();
                        context.putString("user_name", username);
                        context.putString("pos", marker.getTitle());
                        intent.putExtras(context);
                        startActivity(intent);
                    }
                });
            }
        });


        final ListView listpos = (ListView) view.findViewById(R.id.findpos);
        btn = (Button)view.findViewById(R.id.recommend_btn);
        filterText = (EditText) view.findViewById(R.id.searchbox);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 啟動地圖元件用的Intent物件
                Intent intentMap = new Intent(getActivity(), MapsActivity.class);
                Bundle context = new Bundle();
                context.putString("user_name", username);
                intentMap.putExtras(context);
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
                if(expush==position){
                    if(position==0){
                        if(pushtimes>=record_count.get(0)){
                            pushtimes = 0;
                        }
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(LatLngpos.get(pushtimes))    // Sets the center of the map to Mountain View
                                .zoom(14)                   // Sets the zoom
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        pushtimes++;
                    }
                    else{
                        if(pushtimes>=(record_count.get(position)-record_count.get(position-1))){
                            pushtimes = 0;
                        }
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(LatLngpos.get(record_count.get(position-1)+pushtimes))    // Sets the center of the map to Mountain View
                                .zoom(14)                   // Sets the zoom
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        pushtimes++;
                    }
                }
                else {
                    if(position==0){
                        pushtimes = 0;
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(LatLngpos.get(0))    // Sets the center of the map to Mountain View
                                .zoom(14)                   // Sets the zoom
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        expush = 0;
                    }
                    else{
                        pushtimes = 0;
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(LatLngpos.get(record_count.get(position-1)))    // Sets the center of the map to Mountain View
                                .zoom(14)                   // Sets the zoom
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        expush = position;
                    }
                }

                /*Intent intent = new Intent(getActivity(), MapsActivity.class);
                Bundle context = new Bundle();
                context.putString("pos",pos[position]);
                context.putString("user_name", username);
                intent.putExtras(context);
                startActivity(intent);*/
            }
        });
        return view;
    }
    public void httpall(String position){
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        sb.append("query=" + position+ "+" + "嘉義");
        sb.append("&key=AIzaSyBmQhDajl0S9NJtvaidNY_nxNOp0sbe-EQ");
        String url = sb.toString();
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(getActivity(), new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("seeeeee", String.valueOf(Thread.currentThread().getId()));
                try {
                    JSONObject init_title = new JSONObject(s);
                    JSONArray data = init_title.getJSONArray("results");
                    for(int i=0;i<data.length();i++){
                        JSONObject geometry = (JSONObject)data.get(i);
                        JSONObject pos = geometry.getJSONObject("geometry").getJSONObject("location");
                        Double lat =(Double)pos.get("lat");
                        Double lng =(Double)pos.get("lng");
                        String iconurl =  geometry.getString("icon");
                        String name =  geometry.getString("name");
                        LatLng latLng = new LatLng(lat, lng);
                        LatLngpos.add(latLng);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                        if (i==0){
                            count+=data.length();
                            record_count.add(count);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        readTask.execute(url);
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
