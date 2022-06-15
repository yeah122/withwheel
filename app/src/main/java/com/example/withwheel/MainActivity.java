package com.example.withwheel;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends TabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host);


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
        intent = new Intent().setClass(this, sisul.class);
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