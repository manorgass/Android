package com.example.korhacker.gpstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    GPSTracker gpsTracker;
    private static final int PERMISSION_CODE_ACCESS_FINE_LOCATION = 479;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsTracker = new GPSTracker(this);
    }

    public void onGPSShow(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "위치 권한이 없어 실행이 불가능합니다.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_ACCESS_FINE_LOCATION);
            return;
        }

        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lon = gpsTracker.getLongitude();

            Toast.makeText(getApplicationContext(), "GPS info : " + lat + ", " + lon, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "GPS를 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE_ACCESS_FINE_LOCATION:
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "위치 권환 승인", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}