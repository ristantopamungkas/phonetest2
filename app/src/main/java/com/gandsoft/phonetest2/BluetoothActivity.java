package com.gandsoft.phonetest2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BluetoothActivity extends AppCompatActivity {
    BluetoothAdapter btAdapter;

    @SuppressLint("BluetoothManagerLeak")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermissions(new String[]{Manifest.permission.BLUETOOTH}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        final TextView tvPassed,tvPassedNot,tvTestBluetoothTitle;
        final Button bNext, bBack;
        final ProgressBar pbTestBluetooth;

        bBack = (Button)findViewById(R.id.bBack);
        bNext = (Button)findViewById(R.id.bNext);
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        pbTestBluetooth = (ProgressBar) findViewById(R.id.pbTestBluetooth);

        bBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btAdapter.disable();
                finish();
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(new Intent(BluetoothActivity.this,GpsActivity.class));
            }
        });

        if (bNext.isEnabled()) {
            bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.red50));
            bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_enabled));
        }
        else {
            bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.mono50));
            bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_disabled));
        }

        tvTestBluetoothTitle = (TextView)findViewById(R.id.tvTestBluetoothTitle);
        tvPassed = (TextView)findViewById(R.id.tvPassed);
        tvPassedNot = (TextView)findViewById(R.id.tvPassedNot);

        if (btAdapter.disable()) {
            btAdapter.enable();
        }

        if(btAdapter.enable()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    pbTestBluetooth.setVisibility(View.GONE);
                    tvPassed.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            bNext.setEnabled(true);
                            bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.red50));
                            bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_enabled));
                            btAdapter.disable();
                            ReportHelper.writeToFile("Bluetooth detected<br>");
                        }
                    }, 1000);
                }
            }, 3000);
        }

        else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    tvPassedNot.setVisibility(View.VISIBLE);
                }
            }, 5000);   //5 seconds
        }
    }
}
