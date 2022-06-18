package com.example.withwheel;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.SearchView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class search_charger_map extends FragmentActivity implements OnMapReadyCallback {

    private static String TAG = "charger";

    private static final String TAG_JSON = "charger";
    String place_address;

    ArrayList<LocationData> mArrayList;

    public String mJsonString;

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_charger_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        searchView = (SearchView) findViewById(R.id.search_view);

        mArrayList = new ArrayList<>();

        Intent intent = getIntent();
        String address = intent.getStringExtra("searchQuery");

        //넘어온 주소 값이 없다면
        if(address == null){

        }
        // 주소 값이 넘어왔다면
        else{
            searchView.setQuery(address, true);
            search_charger_map.GetData task = new search_charger_map.GetData();
            task.execute("http://10.0.2.2/charger.php", searchView.getQuery().toString());
        }

        // 서치뷰 검색 버튼 눌렸을 때
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();

                // 검색한 지역이 제대로 입력 되었으면
                if (location != null || !location.equals("")) {
                    mArrayList.clear();// 검색 결과 담을 배열 비우고 새롭게 준비

                    search_charger_map.GetData task = new search_charger_map.GetData();
                    task.execute("http://10.0.2.2/charger.php", location);
                }

                else{
                    Toast.makeText(getApplicationContext(), "주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);

        ImageButton btn_toList = (ImageButton) findViewById(R.id.toList);
        btn_toList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = searchView.getQuery().toString();
                Intent intent = new Intent(getApplicationContext(), search_charger_list.class);
                if(!location.equals("")){
                    intent.putExtra("searchQuery", location);
                }
                startActivity(intent);
            }
        });

    }


    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(search_charger_map.this,
                    "잠시만 기다려주세요.", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null) {

                System.out.println(errorString);
            } else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String location = (String) params[1];

            String serverURL = (String) params[0];//"http://10.0.2.2/charger.php";
            String postParameters = "location=" + location;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }

    private void showResult() {

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            //JSONArray jsonArray = new JSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String lat = item.getString("lat");
                String lng = item.getString("lng");
                String place_name = item.getString("place_name");
                place_address = item.getString("place_address");

                LocationData locationData = new LocationData();

                locationData.setLat(lat);
                locationData.setLng(lng);
                locationData.setName(place_name);
                locationData.setAddress(place_address);

                mArrayList.add(locationData);
            }
            // 맵에 있는 마커 모두 삭제
            map.clear();
            // 검색 결과 장소 모두 지도에 마커 추가
            for (int i = 0; i < mArrayList.size(); i++) {
                // 배열에서 값 받아오기
                String st_lat = mArrayList.get(i).getLat();
                String st_lng = mArrayList.get(i).getLng();
                String place_name = mArrayList.get(i).getName();
                String place_address = mArrayList.get(i).getAddress();

                // 문자열 타입으로 받은 위도와 경도를 더블형으로 바꾸기
                Double lat = Double.parseDouble(st_lat);
                Double lng = Double.parseDouble(st_lng);

                LatLng latLng = new LatLng(lat, lng);

                map.addMarker(new MarkerOptions().position(latLng).title(place_name).snippet(place_address));

            }

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(mArrayList.get(0).getLat()), Double.parseDouble(mArrayList.get(0).getLng())))      // Sets the center of the map to Mountain View
                    .zoom(14)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            return;

        } catch (JSONException e) {
            Toast.makeText(search_charger_map.this, mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }


    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        UiSettings mUiSettings = map.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        LatLng cityhall = new LatLng(37.566826, 126.9786567);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityhall, 11));

        map.setOnInfoWindowClickListener(infoWindowClickListener);

        // 현재위치
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            checkLocationPermissionWithRationale();
        }


    }

    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String name = marker.getTitle();
            String address = marker.getSnippet();

            Intent intent = new Intent(search_charger_map.this, detailPage.class);
            intent.putExtra("place_address", address);
            intent.putExtra("place_name", name);
            intent.putExtra("theme", "충전소");

            startActivity(intent);
        }

    };

    //현재위치
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermissionWithRationale() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("위치정보")
                        .setMessage("이 앱을 사용하기 위해서는 위치정보에 접근이 필요합니다. 위치정보 접근을 허용하여 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(search_charger_map.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}