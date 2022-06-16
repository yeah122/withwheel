package com.example.withwheel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.withwheel.locationData;
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

public class res_sangpe extends FragmentActivity{

    private static String TAG = "map_search";

    private static final String TAG_JSON = "map_search";
    private static final String TAG_PLACE_NAME = "place_name";
    private static final String TAG_PLACE_ADDRESS = "place_address";
    private static final String TAG_PLACE_NUMBER = "place_number";
    private static final String TAG_PLACE_HOMEPAGE = "place_homepage";
    private static final String TAG_PLACE_ENTER = "place_enter";
    private static final String TAG_PLACE_PARKING = "place_parking";
    private static final String TAG_PLACE_ELEVATOR = "place_elevator";
    private static final String TAG_PLACE_REST = "place_rest";
    private static final String TAG_PLACE_INFO = "place_info";
    private static final String TAG_PLACE_ROOM = "place_room";
    private static final String TAG_PLACE_WHEEL = "place_wheel";
    private static final String TAG_PLACE_LIKES = "place_likes";
    private static final String TAG_PLACE_INTRO = "place_intro";

    String place_address;
    String place_name;
    String place_number;
    String place_homepage;
    String place_enter ;
    String place_parking;
    String place_elevator ;
    String place_rest;
    String place_info;
    String place_room;
    String place_likes;
    String place_wheel;
    String place_intro;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    TextView t5;
    TextView t6;
    TextView t7;
    TextView t8;
    TextView t9;
    TextView t10;
    TextView t11;
    TextView t12;


    private AlertDialog dialog;

    private TextView mTextViewResult;
    ArrayList<locationData> mArrayList;

    public String mJsonString, theme;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_sangpe);
        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        theme = intent.getStringExtra("theme");

        mArrayList = new ArrayList<>();

        // 서치뷰 검색 버튼 눌렸을 때
        // 검색한 지역이 제대로 입력 되었으면
        if (location != null || !location.equals("")) {
            mArrayList.clear();// 검색 결과 담을 배열 비우고 새롭게 준비

            if(theme.equals("식당")){
                res_sangpe.GetData task = new res_sangpe.GetData();
                task.execute("http://10.0.2.2/res_location.php", location);
            }
            else if(theme.equals("관광지")){
                res_sangpe.GetData task = new res_sangpe.GetData();
                task.execute("http://10.0.2.2/place_location.php", location);
            }
            else  if(theme.equals("숙박")){
                res_sangpe.GetData task = new res_sangpe.GetData();
                task.execute("http://10.0.2.2/hotel_location.php", location);
            }
            else{ // 테마값이 빈 값으로 넘겨졌을 때
                Toast.makeText(getApplicationContext(), "오류입니다.", Toast.LENGTH_SHORT).show();
            }


            return;

        }

    }


    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(res_sangpe.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null) {

                mTextViewResult.setText(errorString);
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
            if(theme.equals("숙박")){
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    place_name = item.getString(TAG_PLACE_NAME);
                    place_address = item.getString(TAG_PLACE_ADDRESS);
                    place_number = item.getString(TAG_PLACE_NUMBER);
                    place_homepage = item.getString(TAG_PLACE_HOMEPAGE);
                    place_enter = item.getString(TAG_PLACE_ENTER);
                    place_parking = item.getString(TAG_PLACE_PARKING);
                    place_elevator = item.getString(TAG_PLACE_ELEVATOR);
                    place_rest = item.getString(TAG_PLACE_REST);
                    place_room = item.getString(TAG_PLACE_ROOM);
                    place_info = item.getString(TAG_PLACE_INFO);
                    place_wheel = item.getString(TAG_PLACE_WHEEL);

                    locationData locationData = new locationData();

                    locationData.setName(place_name);
                    locationData.setAddress(place_address);
                    locationData.setAddress(place_number);
                    locationData.setAddress(place_homepage);
                    locationData.setAddress(place_enter);
                    locationData.setAddress(place_parking);
                    locationData.setAddress(place_elevator);
                    locationData.setAddress(place_rest);
                    locationData.setAddress(place_room);
                    locationData.setAddress(place_info);
                    locationData.setAddress(place_wheel);

                    mArrayList.add(locationData);
                }
                t1 = (TextView) findViewById(R.id.textview1);
                t1.setText("시설명 : " + place_name);
                t1.setTextSize(20);
                t2 = (TextView) findViewById(R.id.textview2);
                t2.setText("소재지 도로명주소 : " +place_address);
                t2.setTextSize(20);
                t3 = (TextView) findViewById(R.id.textview3);
                t3.setText("연락처 : " +place_number);
                t3.setTextSize(20);
                t4 = (TextView) findViewById(R.id.textview4);
                t4.setText("홈페이지 : " +place_homepage);
                t4.setTextSize(20);
                t5 = (TextView) findViewById(R.id.textview6);
                t5.setText("주출입구 접근로 여부 : " + place_enter);
                t5.setTextSize(20);
                t6 = (TextView) findViewById(R.id.textview7);
                t6.setText("장애인 전용 주차 구역 여부 : " + place_parking);
                t6.setTextSize(20);
                t7 = (TextView) findViewById(R.id.textview8);
                t7.setText("장애인 전용 승강기 여부 : " + place_elevator);
                t7.setTextSize(20);
                t8 = (TextView) findViewById(R.id.textview9);
                t8.setText("장애인 전용 화장실 여부 : " + place_rest);
                t8.setTextSize(20);
                t9 = (TextView) findViewById(R.id.textview10);
                t9.setText("장애인용 객실 이용가능 여부 : " + place_room);
                t9.setTextSize(20);
                t10 = (TextView) findViewById(R.id.textview11);
                t10.setText("안내 서비스 여부 : " + place_info);
                t10.setTextSize(20);
                t11 = (TextView) findViewById(R.id.textview12);
                t11.setText("휠체어 대여 여부 : " + place_wheel);
                t11.setTextSize(20);

            }
            else if(theme.equals("관광지")){
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    place_name = item.getString(TAG_PLACE_NAME);
                    place_address = item.getString(TAG_PLACE_ADDRESS);
                    place_number = item.getString(TAG_PLACE_NUMBER);
                    place_homepage = item.getString(TAG_PLACE_HOMEPAGE);
                    place_likes = item.getString(TAG_PLACE_LIKES);
                    place_enter = item.getString(TAG_PLACE_ENTER);
                    place_elevator = item.getString(TAG_PLACE_ELEVATOR);
                    place_rest = item.getString(TAG_PLACE_REST);
                    place_parking = item.getString(TAG_PLACE_PARKING);
                    place_intro = item.getString(TAG_PLACE_INTRO);

                    locationData locationData = new locationData();

                    locationData.setName(place_name);
                    locationData.setAddress(place_address);
                    locationData.setAddress(place_number);
                    locationData.setAddress(place_homepage);
                    locationData.setAddress(place_likes);
                    locationData.setAddress(place_enter);
                    locationData.setAddress(place_elevator);
                    locationData.setAddress(place_rest);
                    locationData.setAddress(place_parking);
                    locationData.setAddress(place_intro);


                    mArrayList.add(locationData);
                }
                t1 = (TextView) findViewById(R.id.textview1);
                t1.setText("시설명 : " + place_name);
                t1.setTextSize(20);
                t2 = (TextView) findViewById(R.id.textview2);
                t2.setText("소개 : " + place_intro);
                t2.setTextSize(20);
                t3 = (TextView) findViewById(R.id.textview3);
                t3.setText("소재지 도로명주소 : " +place_address);
                t3.setTextSize(20);
                t4 = (TextView) findViewById(R.id.textview4);
                t4.setText("연락처 : " +place_number);
                t4.setTextSize(20);
                t5 = (TextView) findViewById(R.id.textview5);
                t5.setText("홈페이지 : " +place_homepage);
                t5.setTextSize(20);
                t6 = (TextView) findViewById(R.id.textview6);
                t6.setText("안내서비스 여부: " + place_likes);
                t6.setTextSize(20);
                t7 = (TextView) findViewById(R.id.textview7);
                t7.setText("주출입구 접근로 여부 : " + place_enter);
                t7.setTextSize(20);
                t8 = (TextView) findViewById(R.id.textview8);
                t8.setText("장애인 전용 승강기 여부 : " + place_elevator);
                t8.setTextSize(20);
                t9 = (TextView) findViewById(R.id.textview9);
                t9.setText("장애인 전용 화장실 여부 : " + place_rest);
                t9.setTextSize(20);
                t10 = (TextView) findViewById(R.id.textview10);
                t10.setText("장애인 전용 주차 구역 여부 : " + place_parking);
                t10.setTextSize(20);
                }
            else if(theme.equals("식당")){
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    place_name = item.getString(TAG_PLACE_NAME);
                    place_address = item.getString(TAG_PLACE_ADDRESS);
                    place_number = item.getString(TAG_PLACE_NUMBER);
                    place_homepage = item.getString(TAG_PLACE_HOMEPAGE);
                    place_enter = item.getString(TAG_PLACE_ENTER);
                    place_parking = item.getString(TAG_PLACE_PARKING);
                    place_elevator = item.getString(TAG_PLACE_ELEVATOR);
                    place_rest = item.getString(TAG_PLACE_REST);
                    place_info = item.getString(TAG_PLACE_INFO);

                    locationData locationData = new locationData();

                    locationData.setName(place_name);
                    locationData.setAddress(place_address);
                    locationData.setAddress(place_number);
                    locationData.setAddress(place_homepage);
                    locationData.setAddress(place_enter);
                    locationData.setAddress(place_parking);
                    locationData.setAddress(place_elevator);
                    locationData.setAddress(place_rest);
                    locationData.setAddress(place_info);


                    mArrayList.add(locationData);
                }
                t1 = (TextView) findViewById(R.id.textview1);
                t1.setText("시설명 : " + place_name);
                t1.setTextSize(20);
                t2 = (TextView) findViewById(R.id.textview2);
                t2.setText("소재지 도로명주소 : " +place_address);
                t2.setTextSize(20);
                t3 = (TextView) findViewById(R.id.textview3);
                t3.setText("연락처 : " +place_number);
                t3.setTextSize(20);
                t4 = (TextView) findViewById(R.id.textview4);
                t4.setText("홈페이지 : " +place_homepage);
                t4.setTextSize(20);
                t5 = (TextView) findViewById(R.id.textview5);
                t5.setText("주출입구 접근로 여부 : " + place_enter);
                t5.setTextSize(20);
                t6 = (TextView) findViewById(R.id.textview6);
                t6.setText("장애인 전용 주차 구역 여부 : " + place_parking);
                t6.setTextSize(20);
                t7 = (TextView) findViewById(R.id.textview7);
                t7.setText("장애인 전용 승강기 여부 : " + place_elevator);
                t7.setTextSize(20);
                t8 = (TextView) findViewById(R.id.textview8);
                t8.setText("장애인 전용 화장실 여부 : " + place_rest);
                t8.setTextSize(20);
                t9 = (TextView) findViewById(R.id.textview9);
                t9.setText("안내서비스 : " + place_info);
                t9.setTextSize(20);
            }
            else{
                Toast.makeText(res_sangpe.this, "버튼을 누르고 검색하세요", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Toast.makeText(res_sangpe.this, mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }

}