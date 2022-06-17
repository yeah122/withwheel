package com.example.withwheel;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class sangpe extends FragmentActivity {

    private static String TAG = "sangpe";

    private static final String TAG_JSON = "charger";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String place_lat, place_lng;

    String place_address, place_name, place_info, place_start, place_close, place_sat_start, place_sat_close;
    String place_sun_start, place_sun_close, place_sametime, place_air, place_phone, place_call;

    TextView Tplace_name, Tplace_address, Tplace_call, Tplace_info, Tplace_time;
    TextView Tplace_sat_time, Tplace_sun_time, Tplace_phone , Tplace_air;

    private TextView mTextViewResult;
    ArrayList<locationData> mArrayList;
    public String mJsonString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preference = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String userid = preference.getString("id", "");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sangpe);
        Intent intent = getIntent();
        String location_address = intent.getStringExtra("place_address");
        String location_name = intent.getStringExtra("place_name");

        LinearLayout Conmenu = (LinearLayout)findViewById(R.id.contextmenu);
        registerForContextMenu(Conmenu);

        mArrayList = new ArrayList<>();
        Tplace_name = (TextView) findViewById(R.id.place_name);
        Tplace_address = (TextView) findViewById(R.id.place_address);
        Tplace_call = (TextView) findViewById(R.id.place_call);
        Tplace_info = (TextView) findViewById(R.id.place_info);
        Tplace_time = (TextView) findViewById(R.id.place_time);
        Tplace_sat_time = (TextView) findViewById(R.id.place_sat_time);
        Tplace_sun_time  = (TextView) findViewById(R.id.place_sun_time);
        Tplace_air = (TextView) findViewById(R.id.place_air);
        Tplace_phone = (TextView) findViewById(R.id.place_phone);

        // 서치뷰 검색 버튼 눌렸을 때
        // 검색한 지역이 제대로 입력 되었으면
        if (location_address != null) {
            mArrayList.clear();// 검색 결과 담을 배열 비우고 새롭게 준비

            sangpe.GetData task = new sangpe.GetData();
            task.execute("http://10.0.2.2/charger.php", location_address, location_name);

            return;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmark, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {

        SharedPreferences preference = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String userid = preference.getString("id", "");

        switch (item.getItemId()) {
            case R.id.bookmark:
                sangpe.GetData task = new sangpe.GetData();
                task.execute("http://10.0.2.2/bookmark_onoff.php", userid, place_name, place_address);
        }
        return true;
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

            String serverURL;
            String postParameters;

            if(params.length <= 3) {//상세페이지 정보 가져오기
                String location_address = (String) params[1];
                String location_name = (String) params[2];

                serverURL = (String) params[0];
                postParameters = "place_address=" + location_address + "&place_name=" + location_name;
            }
            else{//즐겨찾기 등록/해제 하기
                String userid = (String) params[1];
                String place_name = (String) params[2];
                String place_address = (String) params[3];

                serverURL = (String) params[0];
                postParameters = "userid=" + userid + "&place_name=" + place_name + "&place_address" + place_address + "&btnState=" + "충전소";
            }


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
            JSONArray jsonArray = jsonObject.getJSONArray("charger");
            //JSONArray jsonArray = new JSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                place_name = item.getString("place_name");
                place_address = item.getString("place_address");
                place_info = item.getString("place_info");
                place_start = item.getString("place_start");
                place_close = item.getString("place_close");
                place_sat_start = item.getString("place_sat_start");
                place_sat_close = item.getString("place_sat_close");
                place_sun_start = item.getString("place_sun_start");
                place_sun_close = item.getString("place_sun_close");
                place_sametime = item.getString("place_sametime");
                place_air = item.getString("place_air");
                place_phone = item.getString("place_phone");
                place_call = item.getString("place_call");
                place_lat = item.getString("lat");
                place_lng = item.getString("lng");
            }
            Toast.makeText(getApplicationContext(), String.valueOf(jsonArray.length()), Toast.LENGTH_SHORT).show();

            Tplace_name.setText("시설명 : " + place_name);
            Tplace_address.setText(place_address);
            Tplace_call.setText(place_call);
            Tplace_info.setText(place_info);
            Tplace_time.setText("평일: " + place_start +" ~ "+ place_close);
            Tplace_sat_time.setText("토요일: " + place_sat_start +" ~ "+ place_sat_close);
            Tplace_sun_time.setText("공휴일: " + place_sun_start +" ~ "+ place_sun_close);
            Tplace_air.setText("공기 주입 가능 여부 : " + place_air);
            Tplace_phone.setText("휴대전화 충전 가능 여부 : " + place_phone);

            Tplace_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("tel:" + Tplace_call.getText().toString());
                    Intent intentCAll = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intentCAll);
                }
            });


        } catch (JSONException e) {
            Toast.makeText(sangpe.this, mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }

}