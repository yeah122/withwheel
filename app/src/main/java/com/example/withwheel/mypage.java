package com.example.withwheel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class mypage extends AppCompatActivity {

    TextView textID;
    Button startLogin, startRegister, btnlogout;
    SharedPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preference = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String user = preference.getString("id", "");

        if (user != "") {
            setContentView(R.layout.activity_mypage);
            textID = (TextView) findViewById(R.id.textID);
            btnlogout = (Button) findViewById(R.id.btnLogout);

            String name = user;

            textID.setText(name);

            btnlogout.setOnClickListener(new View.OnClickListener() {
                SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                @Override
                public void onClick(View view) {

                    editor.remove("id");
                    editor.commit();

                    Toast.makeText(mypage.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mypage.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        }
        else{
            setContentView(R.layout.activity_mypage_logout_ver);

            startLogin = (Button) findViewById(R.id.startLogin);
            startRegister = (Button) findViewById(R.id.startRegister);

            Toast.makeText(mypage.this, "로그인이 필요합니다", Toast.LENGTH_SHORT);

            startLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mypage.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            startRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mypage.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }



}