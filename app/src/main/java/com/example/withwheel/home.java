package com.example.withwheel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;//파이어베이스 인증
    private DatabaseReference mDatabaseRef;//실시간 데이터베이스
    private EditText mEtEmail, mEtPwd;//회원가입 입력필드

    private static final String TAG = "Main_Activity";
    Button btnTabHost;

    private LinearLayout linearLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTabHost = (Button) findViewById(R.id.btnTabHost);
        linearLayout = findViewById(R.id.linear);
        toolbar = findViewById(R.id.toolbar);

        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type
        setSupportActionBar(toolbar);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("withwheel");

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
                            Intent intent = new Intent(home.this, home.class);
                            startActivity(intent);
                            finish();//현재 액티비티 파괴
                        }else{
                            Toast.makeText(home.this, "로그인 실패..!", Toast.LENGTH_SHORT).show();

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

    public void map(View view) {
        Intent intent = new Intent(home.this, map_search.class);
        startActivity(intent);
    }


}