package com.example.withwheel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

public class route extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "route";

    private static final String TAG_JSON = "route";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lng";
    private static final String TAG_PLACE_NAME = "place_name";
    private static final String TAG_PLACE_ADDRESS = "place_address";
    private static final String TAG_PLACE_CALL = "place_call";
    private static final String TAG_HOMEPAGE = "homepage";
    private static final String TAG_GUIDE = "guide";
    private static final String TAG_ENTRANCE = "entrance";
    private static final String TAG_ELEVATOR = "elevator";
    private static final String TAG_TOILET = "toilet";
    private static final String TAG_PARKING = "parking";
    private static final String TAG_INTRODUTION = "introdution";
    private static final String TAG_OUTSIDE = "야외";
    private static final String TAG_INSIDE = "실내";
    private static final String TAG_HISTORY = "역사유적";
    private static final String TAG_NATURE = "자연경관";
    private static final String TAG_SHOPPING = "쇼핑";
    private static final String TAG_ART = "문화예술";
    private static final String TAG_THEMEPARK = "테마파크";
    private static final String TAG_CITY = "도시경관";
    private static final String TAG_FOOD = "식도락";
    private static final String TAG_GOOGLE_RATING = "google_rating";
    private static final String TAG_GOOGLE_TOTAL = "google_ratings_total";
    private static final String TAG_KAKAO_RATING = "kakao_rating";
    private static final String TAG_KAKAO_TOTAL = "kakao_ratings_total";

    private TextView mTextViewResult;
    public String mJsonString;
    ArrayList<locationData> mArrayList = new ArrayList<>();

    List<String> keyword_list = new ArrayList<String>();

    Button send_keyword;
    Button[] btn_keyword = new Button[9];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route);

        Integer btnId[] = {R.id.btn_outside, R.id.btn_inside, R.id.btn_shopping, R.id.btn_history, R.id.btn_nature, R.id.btn_city,
                R.id.btn_themepark, R.id.btn_art, R.id.btn_food};

        //버튼 배열에 id 연결
        for (int i = 0; i < 9; i++){
            btn_keyword[i] = (Button) findViewById(btnId[i]);
        }

        //버튼 클릭 이벤트 확인
        for (int i = 0; i < 9; i++){
            //btn_keyword[i].setTag(i); //버튼의 인덱스를 태그로 저장
            btn_keyword[i].setOnClickListener(this);
        }

        send_keyword = (Button) findViewById(R.id.send_keyword);
        send_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(keyword_list.size() < 2 || keyword_list.size() >= 5){
                    Toast.makeText(getApplicationContext(), "키워드는 2~4개 사이로 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String size = String.valueOf(keyword_list.size());

                    route.GetData task = new route.GetData();
                    task.execute("http://10.0.2.2/route.php", size, keyword_list.toString());

                    //setContentView(R.layout.activity_route_result);

                    //Toast.makeText(getApplicationContext(),keyword_list.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    @Override
    public void onClick(View v) {
        Button btn = (Button) v; //클릭된 뷰를 버튼으로 받아옴
        //Toast.makeText(this, btn_select[Integer.parseInt(btn.getTag().toString())], Toast.LENGTH_SHORT).show();

        switch(btn.getId()){
            case R.id.btn_outside:
                if (keyword_list.contains("야외") == false){
                    keyword_list.add("야외");
                    btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
                else{
                    keyword_list.remove("야외");
                    btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.btn_inside:
                if (keyword_list.contains("실내") == false){
                    keyword_list.add("실내");
                    btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
                else{
                    keyword_list.remove("실내");
                    btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.btn_shopping:
                if (keyword_list.contains("쇼핑") == false){
                    keyword_list.add("쇼핑");
                    btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
                else{
                    keyword_list.remove("쇼핑");
                    btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.btn_history:
                if (keyword_list.contains("역사유적") == false){
                    keyword_list.add("역사유적");
                    btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
                else{
                    keyword_list.remove("역사유적");
                    btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.btn_nature:
                if (keyword_list.contains("자연경관") == false){
                    keyword_list.add("자연경관");
                    btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
                else{
                    keyword_list.remove("자연경관");
                    btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.btn_city:
                if (keyword_list.contains("도시경관") == false){
                    keyword_list.add("도시경관");
                    btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
                else{
                    keyword_list.remove("도시경관");
                    btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.btn_themepark:
                if (keyword_list.contains("테마파크") == false){
                    keyword_list.add("테마파크");
                    btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
                else{
                    keyword_list.remove("테마파크");
                    btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.btn_art:
                if (keyword_list.contains("문화예술") == false){
                    keyword_list.add("문화예술");
                    btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
                else{
                    keyword_list.remove("문화예술");
                    btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.btn_food:
                if (keyword_list.contains("식도락") == false){
                    keyword_list.add("식도락");
                    btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
                else{
                    keyword_list.remove("식도락");
                    btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;

        }
        //Toast.makeText(this, btn.getTag().toString(), Toast.LENGTH_SHORT).show();
        //btn_select[Integer.parseInt(btn.getTag().toString())]
        /*
        for(int i=0; i<9; i++) {
            //클릭된 버튼을 찾았으면
            if (tempBtn == btn) {
                btn_select[i] = 1;
                Toast.makeText(this, btn_select[i], Toast.LENGTH_SHORT).show();
            }
        }*/


    }

    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(route.this,
                    "추천 중입니다. 잠시만 기다려주세요.", null, true, true);
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
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String size = (String)params[1];
            String list = (String)params[2];

            String serverURL = (String)params[0];//"http://10.0.2.2/charger.php";
            String postParameters = "size=" + size + "&list=" + list;

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
            mArrayList.clear();

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            //JSONArray jsonArray = new JSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String lat = item.getString("lat");
                String lng = item.getString("lng");
                String place_name = item.getString(TAG_PLACE_NAME);
                String place_address = item.getString(TAG_PLACE_ADDRESS);
                String place_call = item.getString(TAG_PLACE_CALL);
                String homepage = item.getString(TAG_HOMEPAGE);
                String guide = item.getString(TAG_GUIDE);
                String entrance = item.getString(TAG_ENTRANCE);
                String elevator = item.getString(TAG_ELEVATOR);
                String toilet = item.getString(TAG_TOILET);
                String parking = item.getString(TAG_PARKING);
                String introdution = item.getString(TAG_INTRODUTION);
                String outside = item.getString(TAG_OUTSIDE);
                String inside = item.getString(TAG_INSIDE);
                String history = item.getString(TAG_HISTORY);
                String nature = item.getString(TAG_NATURE);
                String shopping = item.getString(TAG_SHOPPING);
                String art = item.getString(TAG_ART);
                String themepark = item.getString(TAG_THEMEPARK);
                String city = item.getString(TAG_CITY);
                String food = item.getString(TAG_FOOD);
                String google_rating = item.getString(TAG_GOOGLE_RATING);
                String google_ratings_total = item.getString(TAG_GOOGLE_TOTAL);
                String kakao_rating = item.getString(TAG_KAKAO_RATING);
                String kakao_ratings_total = item.getString(TAG_KAKAO_TOTAL);
                String distFromFirst = item.getString("1등과의_거리");
                String totalScore = item.getString("합계_점수");

                locationData locationData = new locationData();

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

                mArrayList.add(locationData);
            }
            // 데이터 가져온 이후에 할 코드
            // Toast.makeText(route.this, "정보 가져오기 성공", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), route_result.class);
            intent.putExtra("keyword", keyword_list.toString());
            intent.putExtra("mArrayList", mArrayList);
            startActivityForResult(intent, 0);

            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(rental_charge.this);
            for (int j=0; j<mArrayList.size(); j++) {

                builder.setMessage(mArrayList.get(j).getLng() + "\n" + mArrayList.get(j).getLat() +
                        "\n" + mArrayList.get(j).getName());
                builder.setNegativeButton("확인", null);
                builder.show();
            }*/

            return;

        }
        catch (JSONException e) {
            Toast.makeText(route.this, mJsonString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "showResult: ", e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mArrayList.clear();
        }
    }
}