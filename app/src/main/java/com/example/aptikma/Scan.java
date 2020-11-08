package com.example.aptikma;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

    public class Scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {

        int MY_PERMISSIONS_REQUEST_CAMERA=0;
        ZXingScannerView ScannerView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ScannerView = new ZXingScannerView(this);
            setContentView(ScannerView);
        }


        @Override
        public void handleResult(Result result) {
            ScanQrBarcode.result.setText(result.getText());
            onBackPressed();
        }

        @Override
        protected void onPause() {
            super.onPause();
            ScannerView.stopCamera();
        }

        @Override
        protected void onResume() {
            super.onResume();
            ScannerView.setResultHandler(this);
            ScannerView.startCamera();

        }
        @Override
        protected void onPostResume() {
            super.onPostResume();
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
            ScannerView.setResultHandler(this);
            ScannerView.startCamera();
        }
    }



