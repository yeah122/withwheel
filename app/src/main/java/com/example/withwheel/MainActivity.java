package com.example.withwheel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends TabActivity {


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_host);

        Intent i = getIntent();
        int startTab = i.getIntExtra("startTab", 0);

        TabHost tabHost = getTabHost(); //탭 호스트 객체 생성

        // 탭스팩 선언하고, 탭의 내부 명칭, 탭에 출력될 글 작성
        TabHost.TabSpec spec;
        Intent intent; //객체

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, home.class);
        spec = tabHost.newTabSpec("home"); // 객체를 생성
        spec.setIndicator("메인"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, search_map.class);
        spec = tabHost.newTabSpec("map_search"); // 객체를 생성
        spec.setIndicator("시설"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, route.class);
        spec = tabHost.newTabSpec("route"); // 객체를 생성
        spec.setIndicator("경로\n추천"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, search_charger_map.class);
        spec = tabHost.newTabSpec("rental_charge"); // 객체를 생성
        spec.setIndicator("휠체어\n충전기"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, mypage.class);
        spec = tabHost.newTabSpec("mypage"); // 객체를 생성
        spec.setIndicator("마이\n페이지"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(startTab); //먼저 열릴 탭을 선택!(설정 순서대로 0부터 시작)

        // 현재위치
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            checkLocationPermissionWithRationale();
        }
    }

    //현재위치
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermissionWithRationale() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    }
                }
                else { }
                return;
            }
        }
    }

}