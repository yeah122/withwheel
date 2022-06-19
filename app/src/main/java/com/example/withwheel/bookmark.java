package com.example.withwheel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

public class bookmark extends AppCompatActivity {

    private static String TAG = "bookmark";
    public String mJsonString;
    ArrayList<LocationData> mArrayList;
    ListView listView;
    bookmark_customview adapter;

    Button btnCharger, btnAttr, btnRes, btnHotel;

    String btnWhat = "식당"; // 무슨 버튼이 눌렸는지 == theme, 초기화는 식당으로

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        mArrayList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.like_listView);
        //상세페이지로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlaceName = mArrayList.get(i).getName();
                String selectedPlaceAddress = mArrayList.get(i).getAddress();

                //상세페이지로 이동
                Intent intent = new Intent(bookmark.this, detailPage.class);

                intent.putExtra("place_address", selectedPlaceAddress);
                intent.putExtra("place_name", selectedPlaceName);
                intent.putExtra("theme", btnWhat);

                startActivity(intent);
            }
        });

        SharedPreferences preference = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String userid = preference.getString("id", "");

        //맨처음에는 식당 정보 가져오기
        bookmark.GetData task = new bookmark.GetData();
        task.execute("http://192.168.219.102/bookmark_search.php", userid, btnWhat);
        
        btnCharger = (Button) findViewById(R.id.btnCharger);
        btnCharger.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                btnWhat = "충전소";
                btnCharger.setEnabled(false);
                btnCharger.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btnCharger.setTextColor(Color.parseColor("#E7ECEF"));
                btnAttr.setEnabled(true);
                btnAttr.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnAttr.setTextColor(Color.parseColor("#515354"));
                btnRes.setEnabled(true);
                btnRes.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnRes.setTextColor(Color.parseColor("#515354"));
                btnHotel.setEnabled(true);
                btnHotel.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnHotel.setTextColor(Color.parseColor("#515354"));

                bookmark.GetData task = new bookmark.GetData();
                task.execute("http://192.168.219.102/bookmark_search.php", userid, btnWhat);
            }
        });

        btnAttr = (Button) findViewById(R.id.btnAttr);
        btnAttr.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                btnWhat = "관광지";
                btnAttr.setEnabled(false);
                btnAttr.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btnAttr.setTextColor(Color.parseColor("#E7ECEF"));
                btnCharger.setEnabled(true);
                btnCharger.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnCharger.setTextColor(Color.parseColor("#515354"));
                btnRes.setEnabled(true);
                btnRes.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnRes.setTextColor(Color.parseColor("#515354"));
                btnHotel.setEnabled(true);
                btnHotel.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnHotel.setTextColor(Color.parseColor("#515354"));

                bookmark.GetData task = new bookmark.GetData();
                task.execute("http://192.168.219.102/bookmark_search.php", userid, btnWhat);
            }
        });

        btnRes = (Button) findViewById(R.id.btnRes);
        btnRes.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                btnWhat = "식당";
                btnRes.setEnabled(false);
                btnRes.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btnRes.setTextColor(Color.parseColor("#E7ECEF"));
                btnCharger.setEnabled(true);
                btnCharger.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnCharger.setTextColor(Color.parseColor("#515354"));
                btnAttr.setEnabled(true);
                btnAttr.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnAttr.setTextColor(Color.parseColor("#515354"));
                btnHotel.setEnabled(true);
                btnHotel.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnHotel.setTextColor(Color.parseColor("#515354"));

                bookmark.GetData task = new bookmark.GetData();
                task.execute("http://192.168.219.102/bookmark_search.php", userid, btnWhat);
            }
        });

        btnHotel = (Button) findViewById(R.id.btnHotel);
        btnHotel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                btnWhat = "숙박";
                btnHotel.setEnabled(false);
                btnHotel.setBackground(getResources().getDrawable(R.drawable.btn_after));
                btnHotel.setTextColor(Color.parseColor("#E7ECEF"));
                btnCharger.setEnabled(true);
                btnCharger.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnCharger.setTextColor(Color.parseColor("#515354"));
                btnAttr.setEnabled(true);
                btnAttr.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnAttr.setTextColor(Color.parseColor("#515354"));
                btnRes.setEnabled(true);
                btnRes.setBackground(getResources().getDrawable(R.drawable.btn_before));
                btnRes.setTextColor(Color.parseColor("#515354"));

                bookmark.GetData task = new bookmark.GetData();
                task.execute("http://192.168.219.102/bookmark_search.php", userid, btnWhat);
            }
        });
    }


    class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(bookmark.this,
                    "잠시만 기다려주세요.", null, true, false);
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

            String userid = (String)params[1];
            String btnState = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "userid=" + userid + "&btnState=" + btnState;


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
    private void showResult() {
        mArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("bookmark");
            //JSONArray jsonArray = new JSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String place_name = item.getString("place_name");
                String place_address = item.getString("place_address");
                String bookmark = item.getString("bookmark");

                LocationData locationData = new LocationData();

                locationData.setName(place_name);
                locationData.setAddress(place_address);

                mArrayList.add(locationData);
            }
            //정보갖고와서 띄울거
            adapter = new bookmark_customview(mArrayList, mArrayList.size());
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            if(mJsonString.equals("즐겨찾기 목록이 없습니다.")){
                adapter = new bookmark_customview(mArrayList, mArrayList.size());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Toast.makeText(bookmark.this, btnWhat + "의 " + mJsonString, Toast.LENGTH_SHORT).show();
                //listView.setOn;
            }
            else{
                //Toast.makeText(like_it.this, mJsonString, Toast.LENGTH_LONG).show();
                System.out.println(mJsonString);
                Log.d(TAG, "showResult: ", e);
            }

        }

    }


}