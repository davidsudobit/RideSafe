package com.calldetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    
    private static final String IS_PERMISSION_GRANTED= Manifest.permission.READ_PHONE_STATE;
    private static final int PERMISSION_READ_STATE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        IntentFilter intentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
//        BroadcastReceiver br = new PhoneStateReceiver();
//        registerReceiver(br, intentFilter);

        checkForPermission();

        System.out.println("Started");

    }

    private void checkForPermission(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // We do not have this permission. Let's ask the user

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_READ_STATE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_READ_STATE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("PERMISSION_GRANTED");
            } else {
                System.out.println("PERMISSION_DENIED");
            }
        }

    }

}