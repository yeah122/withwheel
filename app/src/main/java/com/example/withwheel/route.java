package com.example.withwheel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class route extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;
        LatLng citihall = new LatLng(37.566826, 126.9786567);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.566826, 126.9786567),14));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(citihall)
                .title("서울시청이라능");
        googleMap.addMarker(markerOptions);

    }
}