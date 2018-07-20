package com.gandsoft.phonetest2;

import android.Manifest;
import android.annotation.TargetApi;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    public TextView tvPassed;
    public Camera camera;
    public SurfaceView surfaceView;
    public SurfaceHolder surfaceHolder;
    public Button btnRear,btnFront,bBack,bNext;
    public ImageView imageView1,imageView2;
    //  public Button btnNext;
    private String report;
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        bBack =(Button) findViewById(R.id.bBack);
        bNext =(Button) findViewById(R.id.bNext);
        btnRear = (Button) findViewById(R.id.btnRearCam);
        btnFront = (Button) findViewById(R.id.btnFrontCam);
        imageView1 = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        tvPassed = (TextView) findViewById(R.id.tvPassed);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.VISIBLE);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    public void rearCamera(View v) {
        btnRear.setVisibility(View.GONE);
        btnFront.setVisibility(View.VISIBLE);
        imageView1.setVisibility(View.GONE);

        camera = Camera.open(0);
        camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void frontCamera(View v) {
        camera.stopPreview();
        camera.release();
        camera = Camera.open(1);
        camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            btnFront.setVisibility(View.GONE);

            tvPassed.setVisibility(View.VISIBLE);
            report = "<br><font color='green'>Camera can be activated</font><br>";
            ReportHelper.writeToFile(report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceCreated(SurfaceHolder holder) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }

    @Override
    public void onBackPressed() {
        camera.release();
        finish();
    }
}