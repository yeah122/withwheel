package com.example.withwheel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class route_result extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "route_re";

    private static final String TAG_JSON = "route_re";

    public String mJsonString;

    GoogleMap map;

    Button restart, oneMore, other;
    ListView listView;

    route_result_CustomListView adapter;
    ArrayList<LocationData> mArrayList;
    ArrayList<LocationData> newAttrList = new ArrayList<>();
    ArrayList<String> deleteAttr = new ArrayList<>();
    JSONArray jArray;

    int cnt = 3, click = 0;

    String keyword;
    String newArr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_result);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(route_result.this);

        Intent intent = new Intent();
        mArrayList = (ArrayList<LocationData>)getIntent().getSerializableExtra("mArrayList");

        Intent getIntent = getIntent();
        keyword = getIntent.getStringExtra("keyword");

        listView =(ListView) findViewById(R.id.listView);

        setListView(mArrayList);
        TextView selectedKeyword = (TextView) findViewById(R.id.selectedKeyword);
        selectedKeyword.setText("선택한 키워드 " + keyword);

        // 관광지 추가
        oneMore = (Button) findViewById(R.id.oneMore);
        oneMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click == 0){
                    addListView(mArrayList);
                }
                else{
                    addListView(newAttrList);
                }
            }
        });

        // 다른 장소 추천 받기(키워드 그대로, 1등한 장소 삭제)
        other = (Button) findViewById(R.id.other);
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cnt = 3;
                if(click == 0){
                    addDeleteAttr(mArrayList);
                }
                else if(click < 10){
                    addDeleteAttr(newAttrList);
                }
                else{
                    other.setClickable(false);
                    other.setTextColor(Color.parseColor("#E7ECEF"));
                    Toast.makeText(getApplicationContext(),"다른 장소 추천 받기는 10번까지 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                click++;
            }
        });

        //다시하기 버튼(키워드 변경)
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mArrayList.clear();
                newAttrList.clear();
                deleteAttr.clear();
                //Toast.makeText(getApplicationContext(), String.valueOf(mArrayList.size()), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void addDeleteAttr(ArrayList<LocationData> list){
        for (int i = 0; i < list.size();i++){
            if(Double.parseDouble(list.get(i).distFromFirst) <= 0.2){
                deleteAttr.add(list.get(i).getName());
            }
        }

        newArr = deleteAttr.toString();
        newAttrList.clear();

        route_result.GetData task = new route_result.GetData();
        task.execute("http://192.168.219.104/route_re.php", keyword, newArr);
    }

    private void addNewMarker(ArrayList<LocationData> list){
        if(list.size() >= 3){ // List가 3개 이상일 때
            for (int i=0; i<3; i++) {
                // 배열에서 값 받아오기
                String st_lat = list.get(i).getLat();
                String st_lng = list.get(i).getLng();
                String place_name = list.get(i).getName();
                String place_address = list.get(i).getAddress();

                // 문자열 타입으로 받은 위도와 경도를 더블형으로 바꾸기
                Double lat = Double.parseDouble(st_lat);
                Double lng = Double.parseDouble(st_lng);

                LatLng latLng = new LatLng(lat, lng);

                map.addMarker(new MarkerOptions().position(latLng).title(place_name).snippet(place_address));

            }
        }

        else { // mArrayList가 3개 미만일 때
            for (int i=0; i<list.size(); i++) {
                // 배열에서 값 받아오기
                String st_lat = list.get(i).getLat();
                String st_lng = list.get(i).getLng();
                String place_name = list.get(i).getName();
                String place_address = list.get(i).getAddress();

                // 문자열 타입으로 받은 위도와 경도를 더블형으로 바꾸기
                Double lat = Double.parseDouble(st_lat);
                Double lng = Double.parseDouble(st_lng);

                LatLng latLng = new LatLng(lat, lng);

                map.addMarker(new MarkerOptions().position(latLng).title(place_name).snippet(place_address));
            }
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(list.get(0).getLat()), Double.parseDouble(list.get(0).getLng())) )      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addListView(ArrayList<LocationData> list) {
        if (cnt < list.size()){

            adapter = new route_result_CustomListView(list, cnt+1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            String st_lat = list.get(cnt).getLat();
            String st_lng = list.get(cnt).getLng();
            String place_name = list.get(cnt).getName();
            String place_address = list.get(cnt).getAddress();

            // 문자열 타입으로 받은 위도와 경도를 더블형으로 바꾸기
            Double lat = Double.parseDouble(st_lat);
            Double lng = Double.parseDouble(st_lng);

            LatLng latLng = new LatLng(lat, lng);

            map.addMarker(new MarkerOptions().position(latLng).title(place_name).snippet(place_address));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(list.get(cnt).getLat()), Double.parseDouble(list.get(cnt).getLng())) )      // Sets the center of the map to Mountain View
                    .zoom(15)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
        else{
            Toast.makeText(getApplicationContext(), list.get(0).getName() + "에서 1.5km 안에 있는 모든 장소 추천 완료", Toast.LENGTH_SHORT).show();
        }
        cnt++;

    }

    //리스트뷰에 추가하고, 리스트뷰 누르면 해당 마커로 이동
    private void setListView(ArrayList<LocationData> list) {

        if(list.size() < cnt){
            adapter = new route_result_CustomListView(list, list.size());
            listView.setAdapter(adapter);
        }
        else {
            adapter = new route_result_CustomListView(list, cnt);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String selectedAttrName = list.get(arg2).getName();
                String selectedAttrAddress = list.get(arg2).getAddress();

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(Double.parseDouble(list.get(arg2).getLat()), Double.parseDouble(list.get(arg2).getLng())) )      // Sets the center of the map to Mountain View
                        .zoom(15)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder

                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                SystemClock.sleep(300); // 해당 마커로 이동하는 것과 인텐트 넘어가는 것 사이에 여유 줌

                // 해당 상세페이지로 이동
                Intent intent = new Intent(route_result.this, detailPage.class);
                intent.putExtra("place_address", selectedAttrAddress);
                intent.putExtra("place_name", selectedAttrName);
                intent.putExtra("theme", "관광지");
                startActivity(intent);
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        addNewMarker(mArrayList);

        UiSettings mUiSettings = map.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        map.setOnInfoWindowClickListener(infoWindowClickListener);
    }

    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String name = marker.getTitle();
            String address = marker.getSnippet();

            // 해당 상세페이지로 이동
            Intent intent = new Intent(route_result.this, detailPage.class);
            intent.putExtra("place_address", address);
            intent.putExtra("place_name", name);
            intent.putExtra("theme", "관광지");
            startActivity(intent);
        }
    };

    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(route_result.this,
                    "잠시만 기다려주세요.", null, true, false);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            //Log.d(TAG, "response - " + result);
            //System.out.println(result);

            if (result == null){

                Log.d(TAG, "error - " + errorString);
            }
            else {
                mJsonString = result;
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String keyword = (String)params[1];
            String deleteAttr = (String)params[2];

            String serverURL = (String)params[0];//"http://10.0.2.2/charger.php";
            String postParameters = "keyword=" + keyword + "&deleteAttr=" + deleteAttr;

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

                return sb.toString().trim();// trim(): 왼쪽 오른쪽 공백 제거

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
            ArrayList<LocationData> newLocal = new ArrayList<>();

            newAttrList.clear();
            newLocal.clear();


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String lat = item.getString("lat");
                String lng = item.getString("lng");
                String place_name = item.getString("place_name");
                String place_address = item.getString("place_address");
                String place_call = item.getString("place_call");
                String homepage = item.getString("homepage");
                String guide = item.getString("guide");
                String entrance = item.getString("entrance");
                String elevator = item.getString("elevator");
                String toilet = item.getString("toilet");
                String parking = item.getString("parking");
                String introdution = item.getString("introdution");
                String outside = item.getString("야외");
                String inside = item.getString("실내");
                String history = item.getString("역사유적");
                String nature = item.getString("자연경관");
                String shopping = item.getString("쇼핑");
                String art = item.getString("문화예술");
                String themepark = item.getString("테마파크");
                String city = item.getString("도시경관");
                String food = item.getString("식도락");
                String google_rating = item.getString("google_rating");
                String google_ratings_total = item.getString("google_ratings_total");
                String kakao_rating = item.getString("kakao_rating");
                String kakao_ratings_total = item.getString("kakao_ratings_total");

                String distFromFirst = item.getString("1등과의_거리");
                String totalScore = item.getString("합계_점수");

                LocationData locationData = new LocationData();

                locationData.setLat(lat);
                locationData.setLng(lng);
                locationData.setName(place_name);
                locationData.setAddress(place_address);
                locationData.setPlace_call(place_call);
                locationData.setHomepage(homepage);
                locationData.setGuide(guide);
                locationData.setEntrance(entrance);
                locationData.setElevator(elevator);
                locationData.setToilet(toilet);
                locationData.setParking(parking);
                locationData.setIntrodution(introdution);
                locationData.setOutside(outside);
                locationData.setInside(inside);
                locationData.setHistory(history);
                locationData.setNature(nature);
                locationData.setShopping(shopping);
                locationData.setArt(art);
                locationData.setThemepark(themepark);
                locationData.setCity(city);
                locationData.setFood(food);
                locationData.setGoogle_rating(google_rating);
                locationData.setGoogle_ratings_total(google_ratings_total);
                locationData.setKakao_rating(kakao_rating);
                locationData.setKakao_ratings_total(kakao_ratings_total);
                locationData.distFromFirst = distFromFirst;
                locationData.totalScore = totalScore;

                newLocal.add(locationData);
            }
            // 데이터 가져온 이후에 할 코드
            //가져온 장소가 5개 이상이면 장소 추천하기

            // 맵에 있는 마커 모두 삭제
            map.clear();
            // 검색 결과 장소 모두 지도에 마커 추가
            addNewMarker(newLocal);
            setListView(newLocal);
            newAttrList = newLocal;

            return;

        }
        catch (JSONException e) {
            Toast.makeText(route_result.this, mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }
}
