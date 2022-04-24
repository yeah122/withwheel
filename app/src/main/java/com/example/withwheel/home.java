package com.example.withwheel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home extends AppCompatActivity {

    private static FirebaseAuth mFirebaseAuth;//파이어베이스 인증
    private DatabaseReference mDatabaseRef;//실시간 데이터베이스
    private EditText mEtEmail, mEtPwd;//회원가입 입력필드

    private static final String TAG = "Main_Activity";

    private LinearLayout linearLayout;
    private Toolbar toolbar;

    TextView textID;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("withwheel");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        linearLayout = findViewById(R.id.linear);
        toolbar = findViewById(R.id.toolbar);

        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type
        setSupportActionBar(toolbar);

        // 유저정보 없으면 로그인창
        if (user == null) {
            setContentView(R.layout.activity_main);

            mEtEmail = findViewById(R.id.et_email);
            mEtPwd = findViewById(R.id.et_pwd);


            Button btn_login = findViewById(R.id.btn_login);
            btn_login.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //로그인 요청
                    String strEmail = mEtEmail.getText().toString();
                    String strPwd = mEtPwd.getText().toString();

                    mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(home.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful()){
                                //  로그인 성공
                                //Intent intent = new Intent(home.this, after_login.class);
                                //startActivity(intent);
                                //finish();//현재 액티비티 파괴

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
                                textID.setText(email + " 님");
                            }
                            else{
                                Toast.makeText(home.this, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            Button btn_register = findViewById(R.id.btn_register);
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    //회원가입 화면으로 이동
                    Intent intent = new Intent(home.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }

        //유저정보 있으면 프로필창
        else {
            setContentView(R.layout.after_login);

            textID = (TextView) findViewById(R.id.textID);
            btnLogout = (Button) findViewById(R.id.btnLogout);

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
            textID.setText(email + " 님");

            //logout button event
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFirebaseAuth.signOut();
                    setContentView(R.layout.activity_main);
                }
            });
        }
    }
}