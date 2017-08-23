package com.mis.ncyu.quickchoice;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mis.ncyu.quickchoice.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private GoogleMap mMap;
    private String pos,user_name;
    // Google API用戶端物件
    private GoogleApiClient mGoogleApiClient;

    // Location請求物件
    private LocationRequest mLocationRequest;

    // 記錄目前最新的位置
    private Location mLastLocation;

    private Marker mCurrLocationMarker;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        get_position();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

    }

    public void get_position(){
        Bundle context = this.getIntent().getExtras();
        if (context != null) {
            pos=context.getString("pos");
            user_name = context.getString("user_name");
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void httpall(String position){
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        sb.append("query=" + position+ "+" + "嘉義");
        sb.append("&key=AIzaSyBmQhDajl0S9NJtvaidNY_nxNOp0sbe-EQ");
        String url = sb.toString();
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(MapsActivity.this, new AsyncResponse() {
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
                        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        readTask.execute(url);
    }

    public void httpd(){
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        sb.append("query=" + pos+ "+" + "嘉義");
        sb.append("&key=AIzaSyBmJhSyVJdknK7UTTe8jtfEd5SA_b8m9e8");
        String url = sb.toString();
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(MapsActivity.this, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("seeeeee", String.valueOf(Thread.currentThread().getId()));
                MarkerOptions markerOptions = new MarkerOptions();
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
                        markerOptions.position(latLng);
                        markerOptions.title(name);
                        mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        readTask.execute(url);
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("seeeeee2222", String.valueOf(Thread.currentThread().getId()));
        String [] position = {"家樂福","新光三越","大遠百","秀泰廣場","陶板屋","UNIQLO","NET","星巴克","野宴","逐鹿炭火"};
        mMap = googleMap;
        if (pos == null){
            for(int i=0;i<position.length;i++){
                httpall(position[i]);
            }
        }
        else{
            httpall(pos);
        }


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this, Choice_recommend_type.class);
                Bundle context = new Bundle();
                context.putString("user_name", user_name);
                context.putString("pos",marker.getTitle());
                intent.putExtras(context);
                startActivity(intent);
                finish();
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);  // 右下角的放大縮小功能
        mMap.getUiSettings().setCompassEnabled(true);       // 左上角的指南針，要兩指旋轉才會出現
        mMap.getUiSettings().setMapToolbarEnabled(true);    // 右下角的導覽及開啟 Google Map功能
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // 已經連線到Google Services
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Google Services連線中斷
        // int參數是連線中斷的代號
        Log.e("GGGGGGGGGGGGGGGGGGGGGGG", String.valueOf(Thread.currentThread().getId()));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Google Services連線失敗
        // ConnectionResult參數是連線失敗的資訊
        Log.e("GGGGGGGGGGGGGGGGGGGGGGG", String.valueOf(Thread.currentThread().getId()));
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

}


