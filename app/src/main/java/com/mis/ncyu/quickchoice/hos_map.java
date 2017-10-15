package com.mis.ncyu.quickchoice;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class hos_map extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    // Google API用戶端物件
    private GoogleApiClient mGoogleApiClient;
    private MapView map;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private String area;
    private double [] lat={23.500706 ,
            23.475749 ,
            23.484122 ,
            23.466393 ,
            25.041620 ,
            23.464348 ,
            23.487250 ,
            24.190000 ,
            23.001602 ,
            22.996354 ,
            23.595964 ,
            23.463065 ,
            22.994849 ,
            22.976973 ,
            23.288919 ,
            23.020784 ,
            22.677537 ,
            22.701404 ,
            22.646002 ,
            22.649665 ,
            23.795646 ,
            23.589780 ,
            23.781549 ,
            23.702121};
    private double [] lng={120.451280 ,
            120.467254 ,
            120.461637 ,
            120.423195 ,
            121.517288 ,
            120.234628 ,
            120.506858 ,
            120.606364 ,
            120.217705 ,
            120.208802 ,
            120.457294 ,
            120.286326 ,
            120.198642 ,
            120.242438 ,
            120.325109 ,
            120.221943 ,
            120.322502 ,
            120.290417 ,
            120.309580 ,
            120.356144 ,
            120.218725 ,
            120.307395 ,
            120.441273 ,
            120.545451};
    private String [] hos_name={"嘉基" ,
            " 財團法人天主教聖馬爾定醫院大雅院區" ,
            "財團法人天主教聖馬爾定醫院民權院區" ,
            "台中榮總嘉義分院" ,
            "台大醫院神經部" ,
            "嘉義縣朴子市朴子醫" ,
            "台中榮總嘉義分院灣橋分院 精神科" ,
            "臺中榮民總醫院精神部" ,
            "成大醫院" ,
            "衛生福利部台南醫院" ,
            "佛教慈濟醫療財團法人大林慈濟醫院 失智症中心" ,
            "長庚醫療財團法人嘉義長庚紀念醫院 " ,
            "郭綜合醫院 神經內科" ,
            "衛生福利部嘉南療養院" ,
            "柳營奇美醫院 神經內科" ,
            "財團法人奇美醫院" ,
            "高雄榮民總醫院" ,
            "國軍高雄總醫院左營分院 神經內科" ,
            "財團法人私立高雄醫學大學附設中和紀念醫院" ,
            "財團法人長庚紀念醫院高雄分院" ,
            "雲林長庚紀念醫院 神經內科" ,
            "中國醫藥大學北港附設醫院" ,
            "財團法人彰化基督教醫院雲林分院 失智症整合門診" ,
            "國立成功大學醫學院附設醫院斗六分院 精神科",};

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }
    @Override
    public void onPause() {
        map.onPause();
        super.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
    @Override
    public void onDestroy() {
        map.onDestroy();
        if (mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_map);
        map = (MapView)findViewById(R.id.hos_map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(hos_map.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)) {

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
                                ActivityCompat.requestPermissions(hos_map.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(hos_map.this, "沒有獲得位置權限，無法取得當前位置", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("啟動當前位置");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        Geocoder geocoder = new Geocoder(this, Locale.TRADITIONAL_CHINESE);
        try {
            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            area = address.get(0).getSubAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, area, Toast.LENGTH_SHORT).show();

        //move map camera
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)    // Sets the center of the map to Mountain View
                .zoom(14)                   // Sets the zoom
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i=0;i<lat.length;i++){
            LatLng latLng = new LatLng(lat[i], lng[i]);
            mMap.addMarker(new MarkerOptions().position(latLng).title(hos_name[i]));
        }


        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }
}
