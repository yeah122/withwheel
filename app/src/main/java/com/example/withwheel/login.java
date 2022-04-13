package com.example.withwheel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void joinbutton(View view) {
        Intent intent = new Intent(this, login_main.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "회원가입 완료!", Toast.LENGTH_LONG).show();
    }
}