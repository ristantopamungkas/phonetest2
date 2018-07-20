package com.gandsoft.phonetest2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProximityActivity extends AppCompatActivity {
    SensorManager mySensorManager;
    Sensor myProximitySensor;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        final TextView tvPassed,tvPassedNot,tvTestProximityTitle,tvReadProximity;
        final Button bNext, bBack;

        bBack = (Button)findViewById(R.id.bBack);
        bNext = (Button)findViewById(R.id.bNext);

        bBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProximityActivity.this,PedometerActivity.class));
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

        tvTestProximityTitle = (TextView)findViewById(R.id.tvTestWifiTitle);
        tvPassed = (TextView)findViewById(R.id.tvPassed);
        tvPassedNot = (TextView)findViewById(R.id.tvPassedNot);
        tvReadProximity = (TextView)findViewById(R.id.tvReadProximity);


        mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        SensorEventListener proximitySensorEventListener = new SensorEventListener(){
            int a=0,b=0;
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event){
                if(event.sensor.getType()== Sensor.TYPE_PROXIMITY){
                    if(String.valueOf(event.values[0]).equals("0.0")){
                            a++;
                    }
                    else{
                        b++;
                    }
                    if(a>2 && b>2){
                        tvReadProximity.setVisibility(View.GONE);
                        tvPassed.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                bNext.setEnabled(true);
                                bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.red50));
                                bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_enabled));
                                ReportHelper.writeToFile("Proximity sensor detected<br>");
                            }
                        }, 1000);
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            public void run() {
                                tvPassedNot.setVisibility(View.GONE);
                                a=1000000;
                            }
                        }, 9001);
                    }
                    else{
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                            tvReadProximity.setVisibility(View.GONE);
                            tvPassedNot.setVisibility(View.VISIBLE);
                            tvPassed.setVisibility(View.GONE);
                            a=-100000;
                            }
                        }, 10000);
                    }
                }
            }
        };
        mySensorManager.registerListener(proximitySensorEventListener, myProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
