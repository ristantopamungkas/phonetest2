package com.gandsoft.phonetest2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class GpsActivity extends AppCompatActivity {
    GpsUtil gps;

    @SuppressLint("GpsManagerLeak")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        final TextView tvPassed,tvPassedNot,tvTestGpsTitle;
        final Button bNext, bBack;
        final ProgressBar pbTestGps;

        bBack = (Button)findViewById(R.id.bBack);
        bNext = (Button)findViewById(R.id.bNext);

        pbTestGps = (ProgressBar) findViewById(R.id.pbTestGps);

        bBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(new Intent(GpsActivity.this,ProximityActivity.class));
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

        tvTestGpsTitle = (TextView)findViewById(R.id.tvTestGpsTitle);
        tvPassed = (TextView)findViewById(R.id.tvPassed);
        tvPassedNot = (TextView)findViewById(R.id.tvPassedNot);

        gps = new GpsUtil(GpsActivity.this);

        if (gps.canGetLocation()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    pbTestGps.setVisibility(View.GONE);
                    tvPassed.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            bNext.setEnabled(true);
                            bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.red50));
                            bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_enabled));
                            ReportHelper.writeToFile("GPS detected<br>");
                        }
                    }, 1000);
                }
            }, 3000);
        } else if (!gps.canGetLocation){
            gps.showSettingsAlert();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    tvPassedNot.setVisibility(View.VISIBLE);
            }
            }, 10000);   //5 seconds
        }
    }
}

