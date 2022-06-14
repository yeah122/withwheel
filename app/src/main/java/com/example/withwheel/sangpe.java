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

public class sangpe extends FragmentActivity{

    private static String TAG = "sangpe";

    private static final String TAG_JSON = "charger";
    private static final String TAG_PLACE_NAME = "place_name";
    private static final String TAG_PLACE_ADDRESS = "place_address";
    private static final String TAG_PLACE_INFO = "place_info";
    private static final String TAG_PLACE_START = "place_start";
    private static final String TAG_PLACE_CLOSE = "place_close";
    private static final String TAG_PLACE_SAT_START = "place_sat_start";
    private static final String TAG_PLACE_SAT_CLOSE = "place_sat_close";
    private static final String TAG_PLACE_SUN_START = "place_sun_start";
    private static final String TAG_PLACE_SUN_CLOSE = "place_sun_close";
    private static final String TAG_PLACE_USE = "place_use";
    private static final String TAG_PLACE_AIR = "place_air";
    private static final String TAG_PLACE_PHONE = "place_phone";
    private static final String TAG_PLACE_NUMBER = "place_number";

    String place_address;
    String place_name;
    String place_info;
    String place_start;
    String place_close;
    String place_sat_start ;
    String place_sat_close;
    String place_sun_start ;
    String place_sun_close;
    String place_use;
    String place_air;
    String place_phone;
    String place_number;
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
    TextView t13;


    private AlertDialog dialog;

    private TextView mTextViewResult;
    ArrayList<locationData> mArrayList;

    public String mJsonString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sangpe);
        Intent intent = getIntent();
        String jooso = intent.getStringExtra("jooso");


        mArrayList = new ArrayList<>();

        // 서치뷰 검색 버튼 눌렸을 때
                // 검색한 지역이 제대로 입력 되었으면
        if (jooso != null || !jooso.equals("")) {
            mArrayList.clear();// 검색 결과 담을 배열 비우고 새롭게 준비

            sangpe.GetData task = new sangpe.GetData();
            task.execute("http://10.0.2.2/sangpe_charger.php", jooso);
            t1 = (TextView) findViewById(R.id.textview1);
            t1.setText(place_name);
            t1.setTextSize(30);
            t2 = (TextView) findViewById(R.id.textview2);
            t2.setText(place_address);
            t3 = (TextView) findViewById(R.id.textview3);
            t3.setText(place_info);
            t4 = (TextView) findViewById(R.id.textview4);
            t4.setText(place_start);
            t5 = (TextView) findViewById(R.id.textview5);
            t5.setText(place_close);
            t6 = (TextView) findViewById(R.id.textview6);
            t6.setText(place_sat_start);
            t7 = (TextView) findViewById(R.id.textview7);
            t7.setText(place_sat_close);
            t8 = (TextView) findViewById(R.id.textview8);
            t8.setText(place_sun_start);
            t9 = (TextView) findViewById(R.id.textview9);
            t9.setText(place_sun_close);
            t10 = (TextView) findViewById(R.id.textview10);
            t10.setText(place_use);
            t11 = (TextView) findViewById(R.id.textview11);
            t11.setText(place_air);
            t12 = (TextView) findViewById(R.id.textview12);
            t12.setText(place_phone);
            t13 = (TextView) findViewById(R.id.textview13);
            t13.setText(place_number);

            return;

        }

    }


    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(sangpe.this,
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

            String jooso = (String) params[1];

            String serverURL = (String) params[0];//"http://10.0.2.2/charger.php";
            String postParameters = "jooso=" + jooso;

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

                place_name = item.getString(TAG_PLACE_NAME);
                place_address = item.getString(TAG_PLACE_ADDRESS);
                place_info = item.getString(TAG_PLACE_INFO);
                place_start = item.getString(TAG_PLACE_START);
                place_close = item.getString(TAG_PLACE_CLOSE);
                place_sat_start = item.getString(TAG_PLACE_SAT_START);
                place_sat_close = item.getString(TAG_PLACE_SAT_CLOSE);
                place_sun_start = item.getString(TAG_PLACE_SUN_START);
                place_sun_close = item.getString(TAG_PLACE_SUN_CLOSE);
                place_use = item.getString(TAG_PLACE_USE);
                place_air = item.getString(TAG_PLACE_AIR);
                place_phone = item.getString(TAG_PLACE_PHONE);
                place_number = item.getString(TAG_PLACE_NUMBER);



                locationData locationData = new locationData();

                locationData.setName(place_name);
                locationData.setAddress(place_address);
                locationData.setAddress(place_info);
                locationData.setAddress(place_start);
                locationData.setAddress(place_close);
                locationData.setAddress(place_sat_start);
                locationData.setAddress(place_sat_close);
                locationData.setAddress(place_sun_start);
                locationData.setAddress(place_sun_close);
                locationData.setAddress(place_use);
                locationData.setAddress(place_air);
                locationData.setAddress(place_phone);
                locationData.setAddress(place_number);


                mArrayList.add(locationData);
            }
            t1 = (TextView) findViewById(R.id.textview1);
            t1.setText("시설명 : " + place_name);
            t1.setTextSize(20);
            t2 = (TextView) findViewById(R.id.textview2);
            t2.setText("소재지 도로명주소 : " +place_address);
            t2.setTextSize(20);
            t3 = (TextView) findViewById(R.id.textview3);
            t3.setText("설치 장소 설명 : " +place_info);
            t3.setTextSize(20);
            t4 = (TextView) findViewById(R.id.textview4);
            t4.setText("평일 운영 시작 시각 : " +place_start);
            t4.setTextSize(20);
            t5 = (TextView) findViewById(R.id.textview5);
            t5.setText("평일 운영 종료 시각 : " + place_close);
            t5.setTextSize(20);
            t6 = (TextView) findViewById(R.id.textview6);
            t6.setText("토요일 운영 시작 시각 : " + place_sat_start);
            t6.setTextSize(20);
            t7 = (TextView) findViewById(R.id.textview7);
            t7.setText("토요일 운영 종료 시각 : " + place_sat_close);
            t7.setTextSize(20);
            t8 = (TextView) findViewById(R.id.textview8);
            t8.setText("일요일 운영 시작 시각 : " + place_sun_start);
            t8.setTextSize(20);
            t9 = (TextView) findViewById(R.id.textview9);
            t9.setText("일요일 운영 종료 시각 : " + place_sun_close);
            t9.setTextSize(20);
            t10 = (TextView) findViewById(R.id.textview10);
            t10.setText("동시 사용 가능 대수 : " + place_use);
            t10.setTextSize(20);
            t11 = (TextView) findViewById(R.id.textview11);
            t11.setText("공기 주입 가능 여부 : " + place_air);
            t11.setTextSize(20);
            t12 = (TextView) findViewById(R.id.textview12);
            t12.setText("휴대전화 충전 가능 여부 : " + place_phone);
            t12.setTextSize(20);
            t13 = (TextView) findViewById(R.id.textview13);
            t13.setText("관리기관 전화번호 : " + place_number);
            t13.setTextSize(20);
            //정보갖고와서 띄울거


        } catch (JSONException e) {
            Toast.makeText(sangpe.this, mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }

}