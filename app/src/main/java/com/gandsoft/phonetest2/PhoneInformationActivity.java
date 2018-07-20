package com.gandsoft.phonetest2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Executable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        String rootstate = "Not Allowed";
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
            if (new File(path).exists()) {
                rootstate = "Allowed";
            }
        }

        //Read Axndroid Name
        String[] AndroidNameList = {
                "", "Petit Four", "Cupcake", "Donut", "Eclair",
                "Eclair", "Eclair", "Froyo", "Gingerbread", "Gingerbread",
                "Honeycomb", "Honeycomb", "Honeycomb", "Ice Cream Sandwich", "Ice Cream Sandwich",
                "Jellybean", "Jellybean", "Jellybean", "Kitkat", "Kitkat",
                "Lolipop", "Lolipop", "Marshmallow", "Nougat", "Nougat",
                "Oreo", "Oreo", "Android P"};
        String androidcompleteversion = AndroidNameList[Build.VERSION.SDK_INT - 1] + ", " + Build.VERSION.RELEASE;

        //Create SOC Vendor
        String devicesoc = null;
        if (Build.HARDWARE.equals("qcom")) {
            devicesoc = "Qualcomm " + Build.BOARD;
        } else {
            devicesoc = Build.BOARD;
        }

        //Read Mobile Data State
        String datastate = null;
        if (mTelephonyManager.getDataState() == 2) {
            datastate = "Connected";
        } else if (mTelephonyManager.getDataState() == 0) {
            datastate = "Not Connected";
        }

        //Read Screen Details
        //For Resolution, Size, DPI
        String screenresolution,screensize,screendpi=null;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        float xdpi = widthPixels / metrics.xdpi;
        float ydpi = heightPixels / metrics.ydpi;
        float x = (float) Math.pow(xdpi, 2);
        float y = (float) Math.pow(ydpi, 2);
        float screensizenofullscreen = (float) Math.sqrt(x + y);
        screenresolution = String.valueOf(heightPixels) + "x" + String.valueOf(widthPixels) + " pixels";
        screensize = String.format("%.1f",Float.valueOf(screensizenofullscreen)) + " inch";
        screendpi = String.valueOf(metrics.densityDpi)+ " DPI";

        // Read Kernel Version
        String kernelversion = null;
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/version");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            kernelversion = br.readLine();
            br.close();
        } catch (Exception ex) {
        }

        // Read Core Amount

        String coreamountarray[] = {
                "Single Core", "Dual Core", "Triple Core", "Quad Core", "Penta Core",
                "Hexa Core", "Hepta Core", "Octa Core", "Nona Core", "Deca Core"
        };
        String coreamount = null;
        try {
            Integer core = 0;
            Process p = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/present");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            core = Integer.valueOf(br.readLine().replace("0-", ""));
            coreamount = coreamountarray[core];
            br.close();
        } catch (Exception ex) {
        }

        // Read SOC Clockspeed
        String clockspeed = null;
        try {
            Process p = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            clockspeed = String.format("%.2f", Float.valueOf(br.readLine()) / 1000000) + " GHz";
            br.close();
        } catch (Exception ex) {
        }

        // Read Size of RAM
        String totalram = null;
        String totalramkb = null;
        String totalramgb = null;
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/meminfo");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            totalramkb = br.readLine().replace("MemTotal:      ", "");
            totalramgb = String.valueOf(Float.valueOf(totalramkb.replace(" kB", "")) / 1048576.0);
            totalram = String.format("%.1f",Float.valueOf(totalramgb)) + " GB";
            br.close();
        } catch (Exception ex) {
        }

        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(this);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }



        //File file = new File("/data/system/inputmethod/subtypes.xml");
        //File file = new File("/vendor/app/");
        //File file = new File("/data/mediadrm/");
        //File file = new File("/data/data/com.google.android.onetimeinitializer/shared_prefs/");

        String firstcache="/cache/";
        File file = new File(firstcache);
        Date initializedate = new Date(file.lastModified());

        File file2 = new File("/root/");
        Date firmwaredate = new Date(file2.lastModified());

        Process p = null;
        String phone_model = "";
        if(Build.MANUFACTURER.equals("Sony")){
            try {
                p = new ProcessBuilder("/system/bin/getprop", "ro.semc.product.name").redirectErrorStream(true).start();
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line=br.readLine()) != null){
                    phone_model = line;
            }
            p.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            phone_model = Build.MODEL;
        }

        String imei    = mTelephonyManager.getImei(0)
                + ", " + mTelephonyManager.getImei(1)
                + ", " + mTelephonyManager.getImei(2)
                + ", " + mTelephonyManager.getImei(3)
                + ", " + mTelephonyManager.getImei(4)
                + ", " + mTelephonyManager.getImei(5)
                + ", " + mTelephonyManager.getImei(6);
        String imeifinal = imei.replace(", null", "");

        String[] values = new String[]{
                "Device Model : " + Build.MANUFACTURER + " " + phone_model,
                "Android Version  : " + androidcompleteversion,
                "SOC Boards : " + devicesoc,
                "Processor Details : " + coreamount + ", " + clockspeed,
                "Serial Number : " + Build.SERIAL,
                "Kernel : " + kernelversion,
                "IMEI Number : " + imeifinal,
                "Operator Name : " + mTelephonyManager.getNetworkOperatorName(),
                "Mobile Network : " + datastate,
                "Root Permission : " + rootstate,
                "Screen Resolution : " + screenresolution,
                "Screen DPI : " + screendpi,
                "Screen Size : " + screensize,
                "Total Ram : " + totalram,
//                "Storage Size : " + disksize,
                "Batere : " + String.format("%.0f", batteryCapacity) + " mAh ",
                "First Initialize Date : " + initializedate,
                "Firmware Build Date : " + firmwaredate,
                "Rear Camera Resolution : "+ getCameraResolutionInMp("rear")+" Megapixels",
                "Front Camera Resolution : "+ getCameraResolutionInMp("front")+" Megapixels"

        };


        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
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

    public float getCameraResolutionInMp(String a) {

        int cameraid=100;
        int cameraman=100;
        float maxResolution = -1;
        long pixelCount = -1;

        if(a.equals("rear")){
             cameraid=0;
             cameraman = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        else if(a.equals("front")){
             cameraid=1;
             cameraman = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraid, cameraInfo);
        if (cameraInfo.facing == cameraman ) {
            try {
                Camera.Parameters cameraParams = Camera.open(cameraid).getParameters();
                for (int j = 0; j < cameraParams.getSupportedPictureSizes().size(); j++) {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                    if (pixelCountTemp > pixelCount) {
                        pixelCount = pixelCountTemp;
                        maxResolution = Math.round(((float) pixelCountTemp) / 1000000);
                    }
                }
                Camera.open(cameraid).release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return maxResolution;
    }
}

