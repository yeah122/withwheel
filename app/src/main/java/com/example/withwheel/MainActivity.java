package com.example.withwheel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";

    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ivMenu=findViewById(R.id.iv_menu);
        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);

        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type
        setSupportActionBar(toolbar);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: 클릭됨"); //이게 뭘까
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
    }

    Button button = (Button) findViewById(R.id.button);
    button.

    public void onButton1clicked (View v){
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }
    public void onButton2Clicked(View v) {
        Toast.makeText(this, "교통편 안내", Toast.LENGTH_SHORT).show();
    }
    public void onButton3Clicked(View v) {
        Toast.makeText(this, "급속충전기 위치", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://data.seoul.go.kr/dataList/OA-15813/S/1/datasetView.do"));
        startActivity(intent);
    }

}