package com.mis.ncyu.quickchoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class get_pos extends AppCompatActivity {

    private String pos;
    private MapView map;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pos);
        map = (MapView)findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.addMarker(new MarkerOptions()
                        .anchor(0.0f, 1.0f)
                        .position(new LatLng(55.854049, 13.661331)));
                mMap.getUiSettings().setZoomControlsEnabled(true);
                map.onResume();
            }
        });
        get_position();
    }
    public void get_position(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            pos=context.getString("pos");
        }
    }
}
