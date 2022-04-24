package com.example.withwheel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

public class res_search extends AppCompatActivity {

    Button restaurant, hotel, attractive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_search);

        restaurant = (Button) findViewById(R.id.restaurant);
        hotel = (Button) findViewById(R.id.hotel);
        attractive = (Button) findViewById(R.id.attractive);

        //식당 버튼 누르면 식당 버튼만 활성화X
        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurant.setEnabled(false);
                hotel.setEnabled(true);
                attractive.setEnabled(true);
            }
       });

        //호텔 버튼 누르면 호텔 버튼만 활성화X
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurant.setEnabled(true);
                hotel.setEnabled(false);
                attractive.setEnabled(true);
            }
        });

        //관광지 버튼 누르면 관광지 버튼만 활성화X
        attractive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurant.setEnabled(true);
                hotel.setEnabled(true);
                attractive.setEnabled(false);
            }
        });
    }
}