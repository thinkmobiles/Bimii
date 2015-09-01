package com.bimii.mobile.utils;

import android.content.Context;
import android.provider.Settings.Secure;

public final class SecureProvider {

    public static String getUniqueAndroidId(final Context context){
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }
}
