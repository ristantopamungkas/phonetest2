package com.gandsoft.phonetest2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {
    private TextView tvPassed, tvPassedNot, tvTestGyroscopeTitle;
    private ProgressBar pbTestGyroscope;
    private SensorManager sManager;
    private Button bBack, bNext;
    public String report = "- <strong>Giroscopio:</strong> <font color='#cc0000'>No se detectó actividad</font><br>\n";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        bBack = (Button) findViewById(R.id.bBack);
        bNext = (Button) findViewById(R.id.bNext);

        pbTestGyroscope = (ProgressBar) findViewById(R.id.pbTestGyroscope);

        bBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(new Intent(GyroscopeActivity.this, CameraActivity.class));
            }
        });

        if (bNext.isEnabled()) {
            bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.red50));
            bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_enabled));
        } else {
            bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.mono50));
            bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_disabled));
        }

        tvTestGyroscopeTitle = (TextView) findViewById(R.id.tvTestGyroscopeTitle);
        tvPassed = (TextView) findViewById(R.id.tvPassed);
        tvPassedNot = (TextView) findViewById(R.id.tvPassedNot);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        finish();
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }


    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    tvPassedNot.setVisibility(View.VISIBLE);
                }
            }, 5000);   //5 seconds
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pbTestGyroscope.setVisibility(View.GONE);
                tvPassed.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        bNext.setEnabled(true);
                        bNext.setTextColor(getApplicationContext().getResources().getColor(R.color.red50));
                        bNext.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_next_enabled));
                    }
                }, 1000);
            }
        }, 3000);

    }
}
