package com.example.withwheel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main_Activity";
    Button btnTabHost;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTabHost = (Button) findViewById(R.id.btnTabHost);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);

        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type
        setSupportActionBar(toolbar);

        //-----------------------------------
        //탭호스트 버튼
        btnTabHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, tabHost.class);
                startActivity(intent);
            }
        });

        //탭호스트 버튼
        //------------------------------------
    }

    public void onButton1clicked(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onButton2clicked(View view) {
        Intent intent = new Intent(this, res_search.class);
        startActivity(intent);
    }

    public void onButton3Clicked(View view) {
        Intent intent = new Intent(this, route.class);
        startActivity(intent);
    }

    public void onButton4clicked(View view) {
        Intent intent = new Intent(this, rental_charge.class);
        startActivity(intent);
    }

    public void onButton5clicked(View view) {
        Toast.makeText(this, "급속충전기 위치", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://data.seoul.go.kr/dataList/OA-15813/S/1/datasetView.do"));
        startActivity(intent);
    }

    public void map(View view) {
        Intent intent = new Intent(MainActivity.this, map_search.class);
        startActivity(intent);
    }
}