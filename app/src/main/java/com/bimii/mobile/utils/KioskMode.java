package com.bimii.mobile.utils;

import android.os.Build;

/**
 * Created by Asus_Dev on 9/21/2015.
 */
public final class KioskMode {

    public static void on(){
        Process processOn = null;

        String ProcID = "79"; //HONEYCOMB AND OLDER

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            ProcID = "42"; //ICS AND NEWER
        }

        try {
            processOn = Runtime.getRuntime().exec(new String[] { "su", "-c", "service call activity "+ProcID+" s16 com.android.systemui" });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            processOn.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void off(){
        Process processOff = null;
        try {
            processOff = Runtime.getRuntime().exec(new String[] { "su", "-c", "am startservice -n com.android.systemui/.SystemUIService" });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            processOff.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
