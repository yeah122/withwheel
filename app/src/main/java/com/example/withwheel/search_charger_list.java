package com.example.withwheel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

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

public class search_charger_list extends AppCompatActivity {

    ImageButton beforePage, nextPage, btn_toMap;
    SearchView searchView;

    private static String TAG = "charger_list";

    private static final String TAG_JSON = "charger";
    String place_address;

    ArrayList<LocationData> mArrayList;

    public String mJsonString;

    search_list_customview adapter;
    ListView listView;

    int clickCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_charger_list);

        mArrayList = new ArrayList<>();

        searchView = (SearchView) findViewById(R.id.search_view);
        listView = (ListView) findViewById(R.id.listView) ;

        beforePage = (ImageButton) findViewById(R.id.beforePage); // 이전 버튼 누르면 이전 페이지로,
        nextPage = (ImageButton) findViewById(R.id.nextPage); // 다음 버튼 누르면 다음 페이지로,
        btn_toMap = (ImageButton) findViewById(R.id.btn_toMap);

        Intent intent = getIntent();
        String address = intent.getStringExtra("searchQuery");

        //넘어온 주소 값이 없다면
        if(address == null){
            //아무 것도 안 함
        }
        // 주소 값이 넘어왔다면
        else{
            searchView.setQuery(address, true);
            search_charger_list.GetData task = new search_charger_list.GetData();
            task.execute("http://10.0.2.2/charger.php", searchView.getQuery().toString());
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String address = searchView.getQuery().toString();

                // 검색한 지역이 제대로 입력 되었으면
                if (address != null || !address.equals("")) {
                    mArrayList.clear();// 검색 결과 담을 배열 비우고 새롭게 준비

                    search_charger_list.GetData task = new search_charger_list.GetData();
                    task.execute("http://10.0.2.2/charger.php", address);
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

        //이전 페이지 버튼
        beforePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCnt -= 20;

                if(clickCnt < 0){
                    Toast.makeText(getApplicationContext(), "첫번째 페이지입니다.", Toast.LENGTH_SHORT).show();
                    clickCnt = 0;
                }
                else{
                    adapter = new search_list_customview(mArrayList, clickCnt);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }
        });

        //다음 페이지 버튼
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCnt += 20;

                if((mArrayList.size() - clickCnt) > 0){
                    adapter = new search_list_customview(mArrayList, clickCnt);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else{
                    clickCnt -= 20;
                    Toast.makeText(getApplicationContext(), "마지막 페이지입니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //리스트뷰 클릭시 상세페이지로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String selectedPlaceName = mArrayList.get(arg2 + clickCnt).getName();
                String selectedPlaceAddress = mArrayList.get(arg2 + clickCnt).getAddress();

                // 해당 상세페이지로 이동
                Intent intent = new Intent(search_charger_list.this, detailPage.class);
                intent.putExtra("place_address", selectedPlaceAddress);
                intent.putExtra("place_name", selectedPlaceName);
                intent.putExtra("theme", "충전소");
                startActivity(intent);
            }
        });

        //지도로 보기 버튼 누르면 지도로 넘어감
        btn_toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = searchView.getQuery().toString();
                Intent intent = new Intent(getApplicationContext(), search_charger_map.class);
                if(!address.equals("")){
                    intent.putExtra("searchQuery", address);
                }
                startActivity(intent);
                finish();
            }
        });

    }

    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(search_charger_list.this,
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

            String location = (String) params[1];

            String serverURL = (String) params[0];
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

        try {
            mArrayList.clear();
            clickCnt = 0;
            //listView.setAdapter(null);

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
            //데이터 가져온 후 진행할 코드

            adapter = new search_list_customview(mArrayList, clickCnt);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            return;

        }
        catch (JSONException e) {
            Toast.makeText(search_charger_list.this, mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }
}