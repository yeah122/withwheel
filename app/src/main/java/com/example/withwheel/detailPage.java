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

public class detailPage extends FragmentActivity{

    private static String TAG = "detailPage";

    private static final String TAG_JSON = "map";
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

    String place_address, place_name, place_info, place_start, place_close, place_sat_start, place_sat_close;
    String place_sun_start, place_sun_close, place_sametime, place_air, place_phone, place_call;

    TextView Tplace_name, Tplace_address, Tplace_call, homepage, enter, parking, elevator, toilet, introduction, place_room, rental_wheel, help;

    ArrayList<LocationData> mArrayList;

    public String mJsonString, theme;

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_attractive);

        Intent intent = getIntent();
        String address = intent.getStringExtra("place_address");
        String name = intent.getStringExtra("place_name");
        theme = intent.getStringExtra("theme");

        Tplace_call = (TextView) findViewById(R.id.place_call);
        homepage = (TextView) findViewById(R.id.homepage);

        mArrayList = new ArrayList<>();

        SharedPreferences preference = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userid = preference.getString("id", "");

        // 주소와 이름이 잘 넘어왔으면
        if (address != null || !address.equals("") && name != null || !name.equals("")) {
            mArrayList.clear();// 검색 결과 담을 배열 비우고 새롭게 준비

            if(theme==null) {
                System.out.println("테마값 오류");
            }
            else if(theme.equals("관광지")){
                setContentView(R.layout.detailpage_attractive);

                detailPage.GetData task = new detailPage.GetData();
                task.execute("http://10.0.2.2/attractive.php", address, name);
            }
            else  if(theme.equals("숙박")){
                setContentView(R.layout.detailpage_hotel);

                detailPage.GetData task = new detailPage.GetData();
                task.execute("http://10.0.2.2/hotel.php", address, name);
            }
            else if(theme.equals("식당")) {
                setContentView(R.layout.detailpage_restaurant);

                detailPage.GetData task = new detailPage.GetData();
                task.execute("http://10.0.2.2/restaurant.php", address, name);
            }
            else if(theme.equals("충전소")) {
                setContentView(R.layout.detailpage_charger);

                detailPage.GetData task = new detailPage.GetData();
                task.execute("http://10.0.2.2/charger.php", address, name);
            }
            else{ // 테마값이 빈 값으로 넘겨졌을 때
                Toast.makeText(getApplicationContext(), "테마 값이 비었습니다.", Toast.LENGTH_SHORT).show();
            }

            return;

        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmark, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.bookmark:
                detailPage.GetData task = new detailPage.GetData();
                task.execute("http://10.0.2.2/bookmark_onoff.php",
                        mArrayList.get(0).getAddress(), mArrayList.get(0).getName(), theme, userid);
        }
        return true;
    }


    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(detailPage.this,
                    "잠시만 기다려주세요.", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
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
            String postParameters;

            String address = (String) params[1];
            String name = (String) params[2];

            String serverURL = (String) params[0];//"http://10.0.2.2/charger.php";

            if(params.length > 3){ // 즐겨찾기
                String btnState = (String) params[3];
                String id = (String) params[4];
                postParameters = "place_address=" + address + "&place_name=" + name + "&btnState=" + btnState+ "&userid=" + id;
            }
            else{//상세페이지 띄우기
                postParameters = "place_address=" + address + "&place_name=" + name;
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

        //즐겨찾기 컨텍스트 메뉴 초기화
        LinearLayout Conmenu = (LinearLayout)findViewById(R.id.contextmenu);
        registerForContextMenu(Conmenu);

        // textView 초기화
        Tplace_name = (TextView) findViewById(R.id.place_name);
        Tplace_address = (TextView) findViewById(R.id.place_address);
        Tplace_call = (TextView) findViewById(R.id.place_call);
        homepage = (TextView) findViewById(R.id.homepage);
        help = (TextView) findViewById(R.id.help);
        enter = (TextView) findViewById(R.id.enter);
        parking = (TextView) findViewById(R.id.parking);
        elevator = (TextView) findViewById(R.id.elevator);
        toilet = (TextView) findViewById(R.id.toilet);
        introduction = (TextView) findViewById(R.id.introduction);
        place_room = (TextView) findViewById(R.id.room);
        rental_wheel = (TextView) findViewById(R.id.rental_wheel);

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray;
            if(theme.equals("충전소")){
                jsonArray = jsonObject.getJSONArray("charger");
            }
            else {
                jsonArray = jsonObject.getJSONArray("map");
            }

            //JSONArray jsonArray = new JSONArray(TAG_JSON);
            if(theme.equals("숙박")){
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);
                    LocationData locationData = new LocationData();

                    locationData.setName(item.getString(TAG_PLACE_NAME));
                    locationData.setAddress(item.getString(TAG_PLACE_ADDRESS));
                    locationData.setPlace_call(item.getString(TAG_PLACE_NUMBER));
                    locationData.setHomepage(item.getString(TAG_PLACE_HOMEPAGE));
                    locationData.setGuide(item.getString("place_info")); // 안내서비스
                    locationData.setEntrance(item.getString(TAG_PLACE_ENTER));
                    locationData.setParking(item.getString(TAG_PLACE_PARKING));
                    locationData.setElevator(item.getString(TAG_PLACE_ELEVATOR));
                    locationData.setToilet(item.getString(TAG_PLACE_REST));
                    locationData.setIntrodution(item.getString(TAG_PLACE_INFO));
                    locationData.setRoom(item.getString(TAG_PLACE_ROOM));
                    locationData.setRentalWheel(item.getString(TAG_PLACE_WHEEL));

                    mArrayList.add(locationData);
                }
                Tplace_name.setText(mArrayList.get(0).getName());
                Tplace_address.setText(mArrayList.get(0).getAddress());
                Tplace_call.setText(mArrayList.get(0).getPlace_call());
                homepage.setText(mArrayList.get(0).getHomepage());
                help.setText("안내 서비스: " + mArrayList.get(0).getGuide());
                enter.setText("주출입구 접근로: " + mArrayList.get(0).getEntrance());
                elevator.setText("장애인 엘리베이터: " + mArrayList.get(0).getElevator());
                toilet.setText("장애인 화장실: " + mArrayList.get(0).getToilet());
                parking.setText("장애인 전용 주차장: " + mArrayList.get(0).getParking());
                place_room.setText("장애인용 객실: " + mArrayList.get(0).getRoom());
                rental_wheel.setText("휠체어 대여: " + mArrayList.get(0).getRentalWheel());

                Tplace_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("tel:" + Tplace_call.getText().toString());
                        Intent intentCAll = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(intentCAll);
                    }
                });

                homepage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(homepage.getText().toString());
                        Intent intentInternet = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intentInternet);
                    }
                });

            }
            else if(theme.equals("관광지")){
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);
                    LocationData locationData = new LocationData();

                    locationData.setName(item.getString(TAG_PLACE_NAME));
                    locationData.setAddress(item.getString(TAG_PLACE_ADDRESS));
                    locationData.setPlace_call(item.getString(TAG_PLACE_NUMBER));
                    locationData.setHomepage(item.getString(TAG_PLACE_HOMEPAGE));
                    locationData.setGuide(item.getString("place_info")); // 안내서비스
                    locationData.setEntrance(item.getString(TAG_PLACE_ENTER));
                    locationData.setParking(item.getString(TAG_PLACE_PARKING));
                    locationData.setElevator(item.getString(TAG_PLACE_ELEVATOR));
                    locationData.setToilet(item.getString(TAG_PLACE_REST));
                    locationData.setIntrodution(item.getString("place_intro"));// 소개

                    mArrayList.add(locationData);
                }

                Tplace_name.setText(mArrayList.get(0).getName());
                Tplace_address.setText(mArrayList.get(0).getAddress());
                Tplace_call.setText(mArrayList.get(0).getPlace_call());
                homepage.setText(mArrayList.get(0).getHomepage());
                help.setText("안내 서비스: " + mArrayList.get(0).getGuide());
                enter.setText("주출입구 접근로: " + mArrayList.get(0).getEntrance());
                elevator.setText("장애인 엘리베이터: " + mArrayList.get(0).getElevator());
                toilet.setText("장애인 화장실: " + mArrayList.get(0).getToilet());
                parking.setText("장애인 전용 주차장: " + mArrayList.get(0).getParking());
                introduction.setText(mArrayList.get(0).getIntrodution());

                Tplace_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("tel:" + Tplace_call.getText().toString());
                        Intent intentCAll = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(intentCAll);
                    }
                });

                homepage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(homepage.getText().toString());
                        Intent intentInternet = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intentInternet);
                    }
                });
            }
            else if(theme.equals("식당")){
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);
                    LocationData locationData = new LocationData();

                    locationData.setName(item.getString(TAG_PLACE_NAME));
                    locationData.setAddress(item.getString(TAG_PLACE_ADDRESS));
                    locationData.setPlace_call(item.getString(TAG_PLACE_NUMBER));
                    locationData.setHomepage(item.getString(TAG_PLACE_HOMEPAGE));
                    locationData.setEntrance(item.getString(TAG_PLACE_ENTER));
                    locationData.setParking(item.getString(TAG_PLACE_PARKING));
                    locationData.setElevator(item.getString(TAG_PLACE_ELEVATOR));
                    locationData.setToilet(item.getString(TAG_PLACE_REST));
                    locationData.setGuide(item.getString(TAG_PLACE_INFO));

                    mArrayList.add(locationData);
                }
                Tplace_name.setText(mArrayList.get(0).getName());
                Tplace_address.setText(mArrayList.get(0).getAddress());
                Tplace_call.setText(mArrayList.get(0).getPlace_call());
                homepage.setText(mArrayList.get(0).getHomepage());
                help.setText("안내 서비스: " + mArrayList.get(0).getGuide());
                enter.setText("주출입구 접근로: " + mArrayList.get(0).getEntrance());
                elevator.setText("장애인 엘리베이터: " + mArrayList.get(0).getElevator());
                toilet.setText("장애인 화장실: " + mArrayList.get(0).getToilet());
                parking.setText("장애인 전용 주차장: " + mArrayList.get(0).getParking());

                Tplace_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("tel:" + Tplace_call.getText().toString());
                        Intent intentCAll = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(intentCAll);
                    }
                });

                homepage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(homepage.getText().toString());
                        Intent intentInternet = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intentInternet);
                    }
                });
            }
            else if(theme.equals("충전소")) {

                TextView Tplace_name, Tplace_address, Tplace_call, Tplace_info, Tplace_time;
                TextView Tplace_sat_time, Tplace_sun_time, Tplace_phone , Tplace_air, Tplace_sametime;

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

                    LocationData locationData = new LocationData();

                    locationData.setName(item.getString(TAG_PLACE_NAME));
                    locationData.setAddress(item.getString(TAG_PLACE_ADDRESS));

                    mArrayList.add(locationData);
                }
                Tplace_name = (TextView) findViewById(R.id.place_name);
                Tplace_address = (TextView) findViewById(R.id.place_address);
                Tplace_call = (TextView) findViewById(R.id.place_call);
                Tplace_info = (TextView) findViewById(R.id.place_info);
                Tplace_time = (TextView) findViewById(R.id.place_time);
                Tplace_sat_time = (TextView) findViewById(R.id.place_sat_time);
                Tplace_sun_time  = (TextView) findViewById(R.id.place_sun_time);
                Tplace_air = (TextView) findViewById(R.id.place_air);
                Tplace_phone = (TextView) findViewById(R.id.place_phone);
                Tplace_sametime = (TextView) findViewById(R.id.place_sametime);

                Tplace_name.setText(place_name);
                Tplace_address.setText(place_address);
                Tplace_call.setText(place_call);
                Tplace_info.setText(place_info);
                Tplace_time.setText("평일: " + place_start +" ~ "+ place_close);
                Tplace_sat_time.setText("토요일: " + place_sat_start +" ~ "+ place_sat_close);
                Tplace_sun_time.setText("공휴일: " + place_sun_start +" ~ "+ place_sun_close);
                Tplace_sametime.setText("동시 사용 가능 대수: " + place_sametime + "대");
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
            }
            else{
                Toast.makeText(detailPage.this, "버튼을 누르고 검색하세요", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            if(mJsonString.equals("true")){
                Toast.makeText(detailPage.this, "즐겨찾기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(mJsonString.equals("false")){
                Toast.makeText(detailPage.this, "즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(mJsonString.equals("logout")){
                Toast.makeText(detailPage.this, "즐겨찾기는 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "showResult: ", e);
        }

    }

}