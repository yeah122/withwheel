package com.example.withwheel;

import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

public class search_map extends FragmentActivity implements OnMapReadyCallback {

    private static String TAG = "map";

    private static final String TAG_JSON = "map";
    String place_address;

    ArrayList<LocationData> mArrayList;

    public String mJsonString;

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    Button btn_restaurant, btn_hotel, btn_attractive;
    String theme = "식당";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(search_map.this);
        searchView = (SearchView) findViewById(R.id.search_view);

        ImageButton tolist = (ImageButton) findViewById(R.id.toList);
        btn_restaurant = (Button) findViewById(R.id.btn_restaurant);
        btn_hotel = (Button) findViewById(R.id.btn_hotel);
        btn_attractive = (Button) findViewById(R.id.btn_attractive);

        mArrayList = new ArrayList<>();

        Intent intent = getIntent();
        String address = intent.getStringExtra("searchQuery");
        String btnWhat = intent.getStringExtra("theme");

        // 주소와 테마가 넘어오지 않았다면
        if(address == null && btnWhat == null){
            btn_restaurant.setEnabled(false);
        }
        // 주소와 테마값이 모두 들어왔다면
        else if(address != null && btnWhat != null ){
            searchView.setQuery(address, true);
            theme = btnWhat;
            if(theme.equals("식당")){
                btn_restaurant.setEnabled(false);
                btn_restaurant.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btn_restaurant.setTextColor(Color.parseColor("#E7ECEF"));
                btn_hotel.setEnabled(true);
                btn_hotel.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_hotel.setTextColor(Color.parseColor("#515354"));
                btn_attractive.setEnabled(true);
                btn_attractive.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_attractive.setTextColor(Color.parseColor("#515354"));
            }
            else if(theme.equals("관광지")){
                btn_restaurant.setEnabled(true);
                btn_restaurant.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_restaurant.setTextColor(Color.parseColor("#515354"));
                btn_hotel.setEnabled(true);
                btn_hotel.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_hotel.setTextColor(Color.parseColor("#515354"));
                btn_attractive.setEnabled(false);
                btn_attractive.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btn_attractive.setTextColor(Color.parseColor("#E7ECEF"));
            }
            else { //숙박일 때
                btn_restaurant.setEnabled(true);
                btn_restaurant.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_restaurant.setTextColor(Color.parseColor("#515354"));
                btn_hotel.setEnabled(false);
                btn_hotel.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btn_hotel.setTextColor(Color.parseColor("#E7ECEF"));
                btn_attractive.setEnabled(true);
                btn_attractive.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_attractive.setTextColor(Color.parseColor("#515354"));
            }
            search_map.GetData task = new search_map.GetData();
            task.execute(searchView.getQuery().toString());
        }
        // 테마값만 넘어왔다면
        else{
            theme = btnWhat;
            if(theme.equals("식당")){
                btn_restaurant.setEnabled(false);
                btn_restaurant.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btn_restaurant.setTextColor(Color.parseColor("#E7ECEF"));
                btn_hotel.setEnabled(true);
                btn_hotel.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_hotel.setTextColor(Color.parseColor("#515354"));
                btn_attractive.setEnabled(true);
                btn_attractive.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_attractive.setTextColor(Color.parseColor("#515354"));
            }
            else if(theme.equals("관광지")){
                btn_restaurant.setEnabled(true);
                btn_restaurant.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_restaurant.setTextColor(Color.parseColor("#515354"));
                btn_hotel.setEnabled(true);
                btn_hotel.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_hotel.setTextColor(Color.parseColor("#515354"));
                btn_attractive.setEnabled(false);
                btn_attractive.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btn_attractive.setTextColor(Color.parseColor("#E7ECEF"));
            }
            else { //숙박일 때
                btn_restaurant.setEnabled(true);
                btn_restaurant.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_restaurant.setTextColor(Color.parseColor("#515354"));
                btn_hotel.setEnabled(false);
                btn_hotel.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btn_hotel.setTextColor(Color.parseColor("#E7ECEF"));
                btn_attractive.setEnabled(true);
                btn_attractive.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_attractive.setTextColor(Color.parseColor("#515354"));
            }
        }

        tolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = searchView.getQuery().toString();
                Intent intent = new Intent(getApplicationContext(), search_list.class);
                if(!location.equals("")){
                    intent.putExtra("searchQuery", location);
                }
                intent.putExtra("theme", theme);
                startActivity(intent);
            }
        });

        btn_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_restaurant.setEnabled(false);
                btn_restaurant.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btn_restaurant.setTextColor(Color.parseColor("#E7ECEF"));
                btn_hotel.setEnabled(true);
                btn_hotel.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_hotel.setTextColor(Color.parseColor("#515354"));
                btn_attractive.setEnabled(true);
                btn_attractive.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_attractive.setTextColor(Color.parseColor("#515354"));

                theme = "식당";

                String searchViewQuery = searchView.getQuery().toString();
                if (searchViewQuery != null || !searchViewQuery.equals("")) {
                    search_map.GetData task = new search_map.GetData();
                    task.execute(searchViewQuery);
                }
                mapFragment.getMapAsync(search_map.this);
            }
        });

        btn_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_restaurant.setEnabled(true);
                btn_restaurant.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_restaurant.setTextColor(Color.parseColor("#515354"));
                btn_hotel.setEnabled(false);
                btn_hotel.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btn_hotel.setTextColor(Color.parseColor("#E7ECEF"));
                btn_attractive.setEnabled(true);
                btn_attractive.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_attractive.setTextColor(Color.parseColor("#515354"));

                theme = "숙박";

                String searchViewQuery = searchView.getQuery().toString();
                if(searchViewQuery != null || !searchViewQuery.equals(""))
                {
                    search_map.GetData task = new search_map.GetData();
                    task.execute(searchViewQuery);
                }
                mapFragment.getMapAsync(search_map.this);
            }
        });

        btn_attractive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_restaurant.setEnabled(true);
                btn_restaurant.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_restaurant.setTextColor(Color.parseColor("#515354"));
                btn_hotel.setEnabled(true);
                btn_hotel.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btn_hotel.setTextColor(Color.parseColor("#515354"));
                btn_attractive.setEnabled(false);
                btn_attractive.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btn_attractive.setTextColor(Color.parseColor("#E7ECEF"));

                theme = "관광지";

                String searchViewQuery = searchView.getQuery().toString();
                if(searchViewQuery != null || !searchViewQuery.equals(""))
                {
                    search_map.GetData task = new search_map.GetData();
                    task.execute(searchViewQuery);
                }
                mapFragment.getMapAsync(search_map.this);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String address = searchView.getQuery().toString();

                // 검색한 지역이 제대로 입력 되었으면
                if (address != null || !address.equals("")) {
                    mArrayList.clear();// 검색 결과 담을 배열 비우고 새롭게 준비
                    if(map != null){
                        map.clear();
                    }

                    if(!theme.equals("") || theme != null) {
                        search_map.GetData task = new search_map.GetData();
                        task.execute(address);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "식당, 관광지, 숙박 중 하나를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(getApplicationContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                return false;
            };

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


    }

    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(search_map.this,
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
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String address = (String) params[0];
            String serverURL;

            if(theme.equals("식당")){
                serverURL = "http://10.0.2.2/restaurant.php";
            }
            else if(theme.equals("관광지")){
                serverURL = "http://10.0.2.2/attractive.php";
            }
            else {//숙박일 때
                serverURL = "http://10.0.2.2/hotel.php";
            }

            String postParameters = "place_address=" + address;

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
                }
                else {
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

        mArrayList.clear();

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
            Toast.makeText(search_map.this, mJsonString, Toast.LENGTH_LONG).show();
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
    }

    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String name = marker.getTitle();
            String address = marker.getSnippet();

            Intent intent = new Intent(search_map.this, detailPage.class);
            intent.putExtra("place_address", address);
            intent.putExtra("place_name", name);
            intent.putExtra("theme", theme);

            startActivity(intent);
        }
    };
}