package com.gandsoft.phonetest2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.*;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PedometerActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    boolean activityRunning;
    private TextView tvPassed,tvPassedNot,tvTestPedometerTitle;
    private Button bNext, bBack;
    private ProgressBar pbTestPedometer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);



        bBack = (Button)findViewById(R.id.bBack);
        bNext = (Button)findViewById(R.id.bNext);

        pbTestPedometer = (ProgressBar) findViewById(R.id.pbTestPedometer);

        bBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(new Intent(PedometerActivity.this,GyroscopeActivity.class));
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

        tvTestPedometerTitle = (TextView)findViewById(R.id.tvTestPedometerTitle);
        tvPassed = (TextView)findViewById(R.id.tvPassed);
        tvPassedNot = (TextView)findViewById(R.id.tvPassedNot);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        activityRunning = true;

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    tvPassedNot.setVisibility(View.VISIBLE);
                }
            }, 5000);   //5 seconds
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    pbTestPedometer.setVisibility(View.GONE);
                    tvPassed.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            bNext.setEnabled(true);
                            bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.red50));
                            bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_enabled));
                            //ReportHelper.writeToFile("Pedometer sensor detected<br>");
                        }
                    }, 1000);
                }
            }, 3000);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
