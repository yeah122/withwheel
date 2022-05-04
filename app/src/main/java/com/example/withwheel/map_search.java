package com.example.withwheel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.CompassView;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ScaleBarView;
import com.naver.maps.map.widget.ZoomControlView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class map_search extends AppCompatActivity implements OnMapReadyCallback, Overlay.OnClickListener {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mDatabase.child("DATA");

    List<Marker> markerArr = new ArrayList<>();
    Marker marker = new Marker();


    //Executor excutor = new SomeExcutor();
    //Handler handler = new Handler(Looper.getMainLooper());


    private static final String TAG = "MainActivity";

    public static final int PERMISSION_REQUEST_CODE = 100;
    public static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public FusedLocationSource mLocationSource;
    private static NaverMap mNaverMap;

    public InfoWindow mInfoWindow;

    TextView textView;
    String name1, name2, name, address, call;

    //final Double[] latArr = new Double[5];
    //final Double[] lngArr = new Double[5];
    public static Double lat, lng; // lat = 37.5670135, lng = 126.9783740

    Callback callback;

    public interface Callback{
        void success(String data);
        void fail(String errorMessage);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);


        // 지도 객체 생성
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        // getMapAsync를 호출하여 비동기로 onMapReady 콜백 메서드 호출
        // onMapReady에서 NaverMap 객체를 받음
        mapFragment.getMapAsync(this);

        // 위치를 반환하는 구현체인 FusedLocationSource 생성
        mLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);

        textView = (TextView) findViewById(R.id.title) ;
        //for (Integer i = 0; i <5; i++){
        //Integer index = i;
        mConditionRef.child("0").child("longitude").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//위도
                name1 = dataSnapshot.getValue(String.class);
                lng = Double.parseDouble(name1);
                //lngArr[index] = lng;
                //textView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.fail(error.getMessage());
            }
        });

        mConditionRef.child("0").child("latitude").addValueEventListener(new ValueEventListener() {//경도

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name2 = dataSnapshot.getValue(String.class);
                lat = Double.parseDouble(name2);
                //latArr[index] = lat;
                //textView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.fail(error.getMessage());
            }
        });

        mConditionRef.child("0").child("fcltynm").addValueEventListener(new ValueEventListener() { //시설명

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.fail(error.getMessage());
            }
        });

        mConditionRef.child("0").child("rdnmadr").addValueEventListener(new ValueEventListener() {//도로명주소

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.fail(error.getMessage());

            }
        });

        mConditionRef.child("0").child("institutionphonenumber").addValueEventListener(new ValueEventListener() {//전화번호

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                call = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.fail(error.getMessage());

            }
        });
        //}

    }


    @NonNull
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Log.d(TAG, "onMapReady");
        // 지도상에 마커 표시
        marker.setPosition(new LatLng(lat, lng));
        marker.setMap(naverMap);

        marker.setWidth(100); //마커 크기 지정
        marker.setHeight(100);// 마커 크기지정
        marker.setIcon(OverlayImage.fromResource(R.drawable.location_pin)); // 마커 아이콘 설정
        marker.setOnClickListener(this); // 인포윈도우 실행
        //markerArr.add(marker); // 마커 리스트에 마커 추가
        marker.setMap(naverMap); // 마커 지도에 띄우기

        // NaverMap 객체 받아서 NaverMap 객체에 위치 소스 지정
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(mLocationSource);

        // UI 컨트롤 재배치
        UiSettings uiSettings = mNaverMap.getUiSettings();
        uiSettings.setCompassEnabled(false); // 기본값 : true
        uiSettings.setScaleBarEnabled(false); // 기본값 : true
        //uiSettings.setZoomControlEnabled(false); // 기본값 : true
        uiSettings.setLocationButtonEnabled(false); // 기본값 : false
        uiSettings.setLogoGravity(Gravity.RIGHT | Gravity.BOTTOM);

        // 권한확인. 결과는 onRequestPermissionsResult 콜백 매서드 호출
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        mInfoWindow = new InfoWindow();
        mInfoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @NonNull
            @Override
            public View getContentView(@NonNull InfoWindow infoWindow) {
                Marker marker = infoWindow.getMarker();
                //PlaceInfo info = (PlaceInfo) marker.getTag();
                View view = View.inflate(map_search.this, R.layout.mapsearch_infowindow, null);
                ((TextView) view.findViewById(R.id.title)).setText(name);
                ((TextView) view.findViewById(R.id.address)).setText("도로명 주소: " + address);
                ((TextView) view.findViewById(R.id.call)).setText("전화번호: " + call);
                return view;
            }
        });
    }

    /*
    // 현재위치로 이동
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // request code와 권한획득 여부 확인
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }*/

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;
            if (marker.getInfoWindow() != null) { // 마커 재클릭시 인포윈도우 없애기
                mInfoWindow.close();
                //Toast.makeText(this.getApplicationContext(), "InfoWindow Close.", Toast.LENGTH_LONG).show();
                Toast.makeText(this.getApplicationContext(), "InfoWindow Close", Toast.LENGTH_SHORT).show();
            }
            else { // 마커 클릭시 인포윈도우 띄우기
                mInfoWindow.open(marker);
                Toast.makeText(this.getApplicationContext(), name, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }


}