package com.gandsoft.phonetest2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
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

import org.w3c.dom.Text;

import java.util.List;

public class WifiActivity extends AppCompatActivity {
    WifiManager wifi;

    @SuppressLint("WifiManagerLeak")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        final TextView tvPassed,tvPassedNot,tvTestWifiTitle;
        final Button bNext, bBack;
        final ProgressBar pbTestWifi;

        bBack = (Button)findViewById(R.id.bBack);
        bNext = (Button)findViewById(R.id.bNext);

        pbTestWifi = (ProgressBar) findViewById(R.id.pbTestWifi);

        bBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                wifi.setWifiEnabled(false);
                finish();
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(new Intent(WifiActivity.this,BluetoothActivity.class));
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

        tvTestWifiTitle = (TextView)findViewById(R.id.tvTestWifiTitle);
        tvPassed = (TextView)findViewById(R.id.tvPassed);
        tvPassedNot = (TextView)findViewById(R.id.tvPassedNot);

        wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);
        }


        if(wifi.setWifiEnabled(true)){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    pbTestWifi.setVisibility(View.GONE);
                    tvPassed.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            bNext.setEnabled(true);
                            bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.red50));
                            bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_enabled));
                            wifi.setWifiEnabled(false);
                            ReportHelper.writeToFile("Wifi detected<br>");
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
