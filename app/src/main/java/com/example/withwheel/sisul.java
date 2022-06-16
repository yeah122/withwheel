package com.example.withwheel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sisul extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sisul);

        Button btn_toMap = (Button) findViewById(R.id.tomap);
        Button btn_toList = (Button) findViewById(R.id.tolist);

        btn_toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), map_search.class);
                startActivity(intent);
            }
        });

        btn_toList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), list_search.class);
                startActivity(intent);
            }
        });
    }
}