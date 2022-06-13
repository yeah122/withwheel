package com.example.withwheel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class scrollview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
    }


    public void btn_res(View view){

    }
    public void btn_hotel(View view) {

    }
    public void btn_place(View view) {

    }

    public void toomap(View view) {
        Intent intent = new Intent(this, map_search.class);
        startActivity(intent);
    }
}