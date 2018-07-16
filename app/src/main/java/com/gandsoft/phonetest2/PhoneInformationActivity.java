package com.gandsoft.phonetest2;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhoneInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_information);
        TelephonyManager mTelephonyManager;
        ListView lvPhoneInformation;
        ListView listview = (ListView) findViewById(R.id.lvPhoneInformation);
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //Read Rootstate
        String rootstate="Not Allowed";
        String[] paths = {
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su",
                "/su/bin/su"
        };
        for (String path : paths) {
            if (new File(path).exists()){
                rootstate="Allowed";
            }
        }

        //Read Android Name
        String [] AndroidNameList= {
                "","Petit Four", "Cupcake", "Donut", "Eclair",
                "Eclair","Eclair", "Froyo", "Gingerbread", "Gingerbread",
                "Honeycomb","Honeycomb","Honeycomb", "Ice Cream Sandwich","Ice Cream Sandwich",
                "Jellybean","Jellybean","Jellybean", "Kitkat","Kitkat",
                "Lolipop","Lolipop", "Marshmallow","Nougat","Nougat",
                "Oreo","Oreo","Android P"};
        String androidname = AndroidNameList[Build.VERSION.SDK_INT-1];

        // Create SOC Vendor
        String Hardware = Build.HARDWARE;
        if(Hardware.equals("qcom")){
            Hardware = "Qualcomm Snapdragon";
        }else{
            Hardware = "Mediatek";
        }

        //Read Mobile Data State
        int datastate_int = mTelephonyManager.getDataState();
        String datastate = null;
        if(datastate_int==2){
            datastate="Connected";
        }
        else if(datastate_int==0){
            datastate="Not Connected";
        }

        //Read Screen Details
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        float xdpi = metrics.xdpi;
        float ydpi = metrics.ydpi;
        float x = (float) Math.pow(xdpi,2);
        float y = (float) Math.pow(ydpi,2);
        float screensize = (float) Math.sqrt(x+y)/100;

        // Read Kernel Version
        String kernelver=null;
        try {
            Process p = Runtime.getRuntime().exec("uname -r");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            kernelver = br.readLine();
            br.close();
        } catch (Exception ex) {}

        // Read Core Amount
        int cores=0;
        String coreamount[] = {"",
                "Single Core", "Dual Core", "Triple Core", "Quad Core","Penta Core",
                "Hexa Core", "Hepta Core", "Octa Core", "Nona Core", "Deca Core"
        };
        String coreamounttext=null;
        try {
            int core=0;
            Process p = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (br.readLine() != null) core++;
            cores=(core-15)/2;
            coreamounttext=coreamount[cores];
            br.close();
        } catch (Exception ex) {}

        // Read SOC Clockspeed
        String clockspeed=null;
        try {
            Process p = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            clockspeed = String.format("%.2f", Float.valueOf(br.readLine())/1000000)+ " GHz";
            br.close();
        } catch (Exception ex) {}

        String[] values = new String[] {
                "Device Model : "+ Build.MANUFACTURER  + " " +  Build.MODEL,
                "Android Version  : " + androidname + ", " +Build.VERSION.RELEASE,
                "Current Security Patch : " + Build.VERSION.SECURITY_PATCH,
                "SOC Boards: " + Hardware + " " + Build.BOARD,
                "Processor Details : " +coreamounttext +", " +clockspeed ,
                "Serial Number : " + Build.SERIAL,
                "Kernel Version : " + kernelver,
                "Builder : " + Build.USER + "@" + Build.HOST,
                "Bootloader Version : " +Build.BOOTLOADER,
                "IMEI Number : " + mTelephonyManager.getDeviceId(),
                "IMEI SV : " + mTelephonyManager.getDeviceSoftwareVersion(),
                "Operator Name : " + mTelephonyManager.getNetworkOperatorName(),
                "Mobile Network : " + datastate,
                "Root Permission : " + rootstate,
                "Screen Resolution : " + heightPixels + "x"+ widthPixels + " pixels",
                "Screen Size : " + String.format("%.2f", screensize) + " inch"
        };


        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
