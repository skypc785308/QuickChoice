package com.mis.ncyu.quickchoice.home;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mis.ncyu.quickchoice.R;
import com.mis.ncyu.quickchoice.recommend.compute_recommend;
import com.mis.ncyu.quickchoice.news;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class map_fragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    public map_fragment() {}

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private FusedLocationProviderClient mFusedLocationClient;
    // Google API用戶端物件
    private GoogleApiClient mGoogleApiClient;

    // Location請求物件
    private LocationRequest mLocationRequest;

    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    Integer expush = 0;
    Integer pushtimes = 0;
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
    private String area;
    String[] pos = {"家樂福", "新光三越", "大遠百", "秀泰廣場", "陶板屋", "UNIQLO", "NET", "星巴克", "野宴", "逐鹿炭火"};

    public Boolean foreach_http(){
        for (int i = 0;i<pos.length;i++){
            httpall(pos[i]);
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        username = ((new_home2) getActivity()).put_login_name();
        LatLngpos = new ArrayList<LatLng>();
        pos_name = new ArrayList<String>();
        record_count = new ArrayList<>();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

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
        filterText.removeTextChangedListener(filterTextWatcher);
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        map = (MapView)view.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        final ListView listpos = (ListView) view.findViewById(R.id.findpos);
        btn = (Button)view.findViewById(R.id.recommend_btn);
        filterText = (EditText) view.findViewById(R.id.searchbox);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),news.class));
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
            }
        });
        return view;
    }
    public void httpall(String position){
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        sb.append("query=" + position+ "+in+" +area );
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



    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
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
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "沒有獲得位置權限，無法取得當前位置", Toast.LENGTH_SHORT).show();
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(getContext(),
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
        mLastLocation = location;
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
        Geocoder geocoder = new Geocoder(getContext(), Locale.TRADITIONAL_CHINESE);
        try {
            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            area = address.get(0).getSubAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), area, Toast.LENGTH_SHORT).show();
        foreach_http();

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

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(),
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
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), compute_recommend.class);
                Bundle context = new Bundle();
                context.putString("user_name", username);

                LatLng poss = marker.getPosition();
                int idx = LatLngpos.indexOf(poss);
                String now_pos = "";
                for (int i=0;i<record_count.size();i++){
                    if (i == 0){
                        if (idx<record_count.get(i)){
                            now_pos = pos[i];
                        }
                    }
                    else {
                        if (idx<record_count.get(i) && idx >=record_count.get(i-1)){
                            now_pos = pos[i];
                        }

                    }

                }
                context.putString("pos", now_pos);
                Toast.makeText(getActivity(), "所在地點："+now_pos, Toast.LENGTH_SHORT).show();
                intent.putExtras(context);
                startActivity(intent);
            }
        });
    }
    /**
     * 逆地理编码 得到地址
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    public static String getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
            Log.i("位置", "得到位置当前" + address + "'\n"
                    + "经度：" + String.valueOf(address.get(0).getLongitude()) + "\n"
                    + "纬度：" + String.valueOf(address.get(0).getLatitude()) + "\n"
                    + "纬度：" + "国家：" + address.get(0).getCountryName() + "\n"
                    + "城市：" + address.get(0).getLocality() + "\n"
                    + "名称：" + address.get(0).getAddressLine(1) + "\n"
                    + "街道：" + address.get(0).getAddressLine(0)
            );
            return address.get(0).getAddressLine(0) + "  " + address.get(0).getLocality() + " " + address.get(0).getCountryName();
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }
}
