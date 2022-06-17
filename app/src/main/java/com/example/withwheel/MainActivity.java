package com.example.withwheel;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_host);


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
        intent = new Intent().setClass(this, map_search.class);
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
        intent = new Intent().setClass(this, rental_charge.class);
        spec = tabHost.newTabSpec("rental_charge"); // 객체를 생성
        spec.setIndicator("휠체어 충전기"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);


        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, mypage.class);
        spec = tabHost.newTabSpec("mypage"); // 객체를 생성
        spec.setIndicator("마이\n페이지"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0); //먼저 열릴 탭을 선택!(설정 순서대로 0부터 시작)

    }

}