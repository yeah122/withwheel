package com.example.withwheel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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

public class list_search extends AppCompatActivity {

    Button btn_restaurant, btn_hotel, btn_attractive;
    ImageButton beforePage, nextPage, btn_toMap;
    SearchView searchView;

    private static String TAG = "listView";

    private static final String TAG_JSON = "map";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lng";
    private static final String TAG_PLACE_NAME = "place_name";
    private static final String TAG_PLACE_ADDRESS = "place_address";
    String place_address;

    private AlertDialog dialog;

    private TextView mTextViewResult;
    ArrayList<locationData> mArrayList;

    public String mJsonString;

    list_search_customview adapter;
    ListView listView;
    String theme = "식당";

    int clickCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_search);

        mArrayList = new ArrayList<>();

        searchView = (SearchView) findViewById(R.id.search_view);
        listView = (ListView) findViewById(R.id.listView) ;

        btn_restaurant = (Button) findViewById(R.id.btn_restaurant); // 식당 정보
        btn_hotel = (Button) findViewById(R.id.btn_hotel); // 호텔 정보
        btn_attractive = (Button) findViewById(R.id.btn_attractive); // 관광지 정보
        beforePage = (ImageButton) findViewById(R.id.beforePage); // 이전 버튼 누르면 이전 페이지로,
        nextPage = (ImageButton) findViewById(R.id.nextPage); // 다음 버튼 누르면 다음 페이지로,
        btn_toMap = (ImageButton) findViewById(R.id.btn_toMap);


        btn_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_restaurant.setEnabled(false);
                btn_hotel.setEnabled(true);
                btn_attractive.setEnabled(true);

                theme = "식당";

                String searchViewQuery = searchView.getQuery().toString();
                if (searchViewQuery != null || !searchViewQuery.equals("")) {
                    list_search.GetData task = new list_search.GetData();
                    task.execute("http://10.0.2.2/res_location.php", searchViewQuery);
                }
            }
        });

        btn_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_restaurant.setEnabled(true);
                btn_hotel.setEnabled(false);
                btn_attractive.setEnabled(true);

                theme = "숙박";

                String searchViewQuery = searchView.getQuery().toString();
                if(searchViewQuery != null || !searchViewQuery.equals(""))
                {
                    list_search.GetData task = new list_search.GetData();
                    task.execute("http://10.0.2.2/hotel_location.php", searchViewQuery);
                }
            }
        });

        btn_attractive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_restaurant.setEnabled(true);
                btn_hotel.setEnabled(true);
                btn_attractive.setEnabled(false);

                theme = "관광지";

                //listView.removeAllViews();
                //listView.setAdapter(null);

                String searchViewQuery = searchView.getQuery().toString();
                if(searchViewQuery != null || !searchViewQuery.equals(""))
                {
                    list_search.GetData task = new list_search.GetData();
                    task.execute("http://10.0.2.2/place_location.php", searchViewQuery);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String address = searchView.getQuery().toString();

                // 검색한 지역이 제대로 입력 되었으면
                if (address != null || !address.equals("")) {
                    mArrayList.clear();// 검색 결과 담을 배열 비우고 새롭게 준비

                    if(theme.equals("식당")){
                        list_search.GetData task = new list_search.GetData();
                        task.execute("http://10.0.2.2/res_location.php", address);
                    }
                    else if (theme.equals("관광지")) {
                        list_search.GetData task = new list_search.GetData();
                        task.execute("http://10.0.2.2/place_location.php", address);
                    }
                    else if (theme.equals("숙박")) {
                        list_search.GetData task = new list_search.GetData();
                        task.execute("http://10.0.2.2/hotel_location.php", address);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "식당, 관광지, 숙박 중 하나를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(getApplicationContext(), "주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                return false;
            };

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
                    adapter = new list_search_customview(mArrayList, clickCnt);
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
                    adapter = new list_search_customview(mArrayList, clickCnt);
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
                Toast.makeText(getApplicationContext(), selectedPlaceName, Toast.LENGTH_SHORT).show();

                // 해당 상세페이지로 이동
                Intent intent = new Intent(list_search.this, detailPage.class);
                intent.putExtra("place_address", selectedPlaceAddress);
                intent.putExtra("place_name", selectedPlaceName);
                intent.putExtra("theme", theme);
                startActivity(intent);
            }
        });

        //지도로 보기 버튼 누르면 지도로 넘어감
        btn_toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), map_search.class);
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

            progressDialog = ProgressDialog.show(list_search.this,
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
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String address = (String) params[1];

            String serverURL = (String) params[0];//"http://10.0.2.2/charger.php";
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

        try {
            mArrayList.clear();
            clickCnt = 0;
            //listView.setAdapter(null);

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
            //데이터 가져온 후 진행할 코드
            Toast.makeText(list_search.this, "정보 가져오기 성공", Toast.LENGTH_SHORT).show();

            adapter = new list_search_customview(mArrayList, clickCnt);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            return;

        }
        catch (JSONException e) {
            Toast.makeText(list_search.this, mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }
}