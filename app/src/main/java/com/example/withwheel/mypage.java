package com.example.withwheel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    Button startLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            setContentView(R.layout.activity_mypage);
            textID = (TextView) findViewById(R.id.textID);

            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            textID.setText(email);

        }
        else{
            setContentView(R.layout.activity_mypage_logout_ver);

            startLogin = (Button) findViewById(R.id.startLogin);

            Toast.makeText(mypage.this, "로그인이 필요합니다", Toast.LENGTH_SHORT);

            startLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mypage.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }



}