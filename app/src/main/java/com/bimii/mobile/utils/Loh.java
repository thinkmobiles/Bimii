package com.bimii.mobile.utils;

import android.util.Log;

public final class Loh {

    private static final String TAG = "bimii_log";

    public static void d(final String msg){
        Log.d(TAG, msg);
    }

    public static void i(final String msg){
        Log.i(TAG, msg);
    }

    public static void e(final String msg){
        Log.e(TAG, msg);
    }
}
