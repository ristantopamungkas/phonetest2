package com.gandsoft.phonetest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReportHelper.removeFile();

        Button bPhoneInformation, bStartTest;

        bPhoneInformation = (Button)findViewById(R.id.bPhoneInformation);
        bStartTest = (Button)findViewById(R.id.bStartTest);

        bPhoneInformation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PhoneInformationActivity.class));
            }
        });

        bStartTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WifiActivity.class));
            }
        });

    }
}
