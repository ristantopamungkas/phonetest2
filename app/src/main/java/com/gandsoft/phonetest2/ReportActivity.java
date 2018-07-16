package com.gandsoft.phonetest2;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity {
    public TextView tvReport;
    public String uri = "";
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/report.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tvReport = (TextView)findViewById(R.id.tvReport);
        uri = Environment.getExternalStorageDirectory().getAbsolutePath() + "/report.txt";
        tvReport.setText(Html.fromHtml(ReportHelper.readFromFile(uri)));
    }
}
