package com.example.withwheel;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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

public class res_main_search extends FragmentActivity implements OnMapReadyCallback {

    private static String TAG = "res_location";

    private static final String TAG_JSON = "res";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lng";
    private static final String TAG_PLACE_NAME = "place_name";
    private static final String TAG_PLACE_ADDRESS = "place_address";
    String place_address;

    private AlertDialog dialog;

    private TextView mTextViewResult;
    ArrayList<locationData> mArrayList;

    public String mJsonString;

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_main_search);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        searchView = (SearchView) findViewById(R.id.search_view);


        mArrayList = new ArrayList<>();

        // 서치뷰 검색 버튼 눌렸을 때
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String res_location = searchView.getQuery().toString();

                // 검색한 지역이 제대로 입력 되었으면
                if (res_location != null || !res_location.equals("")){
                    mArrayList.clear();// 검색 결과 담을 배열 비우고 새롭게 준비
                    res_main_search.GetData task = new res_main_search.GetData();
                    task.execute("http://10.0.2.2/res_location.php", res_location);
                }
                return false;
            };
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }
    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(res_main_search.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String location = (String)params[1];

            String serverURL = (String)params[0];//"http://10.0.2.2/charger.php";
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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
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

    private void showResult(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            //JSONArray jsonArray = new JSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String lat = item.getString(TAG_LAT);
                String lng = item.getString(TAG_LNG);
                String place_name = item.getString(TAG_PLACE_NAME);
                place_address = item.getString(TAG_PLACE_ADDRESS);


                locationData locationData = new locationData();

                locationData.setLat(lat);
                locationData.setLng(lng);
                locationData.setName(place_name);
                locationData.setAddress(place_address);

                mArrayList.add(locationData);
            }
            Toast.makeText(res_main_search.this, "정보 가져오기 성공", Toast.LENGTH_SHORT).show();

            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(rental_charge.this);
            for (int j=0; j<mArrayList.size(); j++) {

                builder.setMessage(mArrayList.get(j).getLng() + "\n" + mArrayList.get(j).getLat() +
                        "\n" + mArrayList.get(j).getName());
                builder.setNegativeButton("확인", null);
                builder.show();
            }*/

            // 맵에 있는 마커 모두 삭제
            map.clear();
            // 검색 결과 장소 모두 지도에 마커 추가
            for (int i=0; i<mArrayList.size(); i++) {
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
                    .target(new LatLng(Double.parseDouble(mArrayList.get(0).getLat()), Double.parseDouble(mArrayList.get(0).getLng())) )      // Sets the center of the map to Mountain View
                    .zoom(14)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            return;

        }
        catch (JSONException e) {
            Toast.makeText(res_main_search.this, mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }


    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        UiSettings mUiSettings = map.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(true);

        LatLng cityhall = new LatLng(37.566826, 126.9786567);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityhall, 14));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(cityhall)
                .title("서울시청");

        map.addMarker(markerOptions);
        map.setOnInfoWindowClickListener(infoWindowClickListener);

    }
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String name = marker.getTitle();
            String address = marker.getSnippet();

            Toast.makeText(res_main_search.this,name,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(res_main_search.this, detailPage.class);



            intent.putExtra("res_location" , address );


            startActivity(intent);
        }

        ;

    };}