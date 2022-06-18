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
import android.widget.ProgressBar;
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

    public String mJsonString;
    ArrayList<LocationData> mArrayList = new ArrayList<>();

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
                    task.execute("http://192.168.219.104/route.php", size, keyword_list.toString());

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
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_after));
                    btn.setTextColor(Color.parseColor("#E7ECEF"));
                }
                else{
                    keyword_list.remove("야외");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_before));
                    btn.setTextColor(Color.parseColor("#515354"));
                }
                break;
            case R.id.btn_inside:
                if (keyword_list.contains("실내") == false){
                    keyword_list.add("실내");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_after));
                    btn.setTextColor(Color.parseColor("#E7ECEF"));
                }
                else{
                    keyword_list.remove("실내");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_before));
                    btn.setTextColor(Color.parseColor("#515354"));
                }
                break;
            case R.id.btn_shopping:
                if (keyword_list.contains("쇼핑") == false){
                    keyword_list.add("쇼핑");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_after));
                    btn.setTextColor(Color.parseColor("#E7ECEF"));
                }
                else{
                    keyword_list.remove("쇼핑");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_before));
                    btn.setTextColor(Color.parseColor("#515354"));
                }
                break;
            case R.id.btn_history:
                if (keyword_list.contains("역사유적") == false){
                    keyword_list.add("역사유적");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_after));
                    btn.setTextColor(Color.parseColor("#E7ECEF"));
                }
                else{
                    keyword_list.remove("역사유적");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_before));
                    btn.setTextColor(Color.parseColor("#515354"));
                }
                break;
            case R.id.btn_nature:
                if (keyword_list.contains("자연경관") == false){
                    keyword_list.add("자연경관");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_after));
                    btn.setTextColor(Color.parseColor("#E7ECEF"));
                }
                else{
                    keyword_list.remove("자연경관");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_before));
                    btn.setTextColor(Color.parseColor("#515354"));
                }
                break;
            case R.id.btn_city:
                if (keyword_list.contains("도시경관") == false){
                    keyword_list.add("도시경관");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_after));
                    btn.setTextColor(Color.parseColor("#E7ECEF"));
                }
                else{
                    keyword_list.remove("도시경관");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_before));
                    btn.setTextColor(Color.parseColor("#515354"));
                }
                break;
            case R.id.btn_themepark:
                if (keyword_list.contains("테마파크") == false){
                    keyword_list.add("테마파크");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_after));
                    btn.setTextColor(Color.parseColor("#E7ECEF"));
                }
                else{
                    keyword_list.remove("테마파크");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_before));
                    btn.setTextColor(Color.parseColor("#515354"));
                }
                break;
            case R.id.btn_art:
                if (keyword_list.contains("문화예술") == false){
                    keyword_list.add("문화예술");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_after));
                    btn.setTextColor(Color.parseColor("#E7ECEF"));
                }
                else{
                    keyword_list.remove("문화예술");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_before));
                    btn.setTextColor(Color.parseColor("#515354"));
                }
                break;
            case R.id.btn_food:
                if (keyword_list.contains("식도락") == false){
                    keyword_list.add("식도락");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_after));
                    btn.setTextColor(Color.parseColor("#E7ECEF"));
                }
                else{
                    keyword_list.remove("식도락");
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_before));
                    btn.setTextColor(Color.parseColor("#515354"));
                }
                break;
        }
    }

    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(route.this,
                    "잠시만 기다려주세요.", null, true, false);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){

                System.out.println(errorString);
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

                mArrayList.add(locationData);
            }
            // 데이터 가져온 이후에 할 코드

            Intent intent = new Intent(getApplicationContext(), route_result.class);
            intent.putExtra("keyword", keyword_list.toString());
            intent.putExtra("mArrayList", mArrayList);
            startActivityForResult(intent, 0);

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