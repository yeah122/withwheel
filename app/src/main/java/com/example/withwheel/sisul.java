package com.example.withwheel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class sisul extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sisul);
    }

    public void tomap(View view) {
        Intent intent = new Intent(this, map_search.class);
        startActivity(intent);
    }
    public void tolist(View view) {
        Intent intent = new Intent(this, scrollview.class);
        startActivity(intent);
    }
}