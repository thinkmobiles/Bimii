package com.bimii.mobile.cache;

import android.content.Context;
import android.content.SharedPreferences;

public final class CacheHelper {

    private static final String BIMII_CACHE_SLOT = "bimii_slot";

    public static boolean saveValue(final Context context, final String key, final String value){
        SharedPreferences shp = context.getSharedPreferences(BIMII_CACHE_SLOT, Context.MODE_PRIVATE);
        SharedPreferences.Editor shpEditor = shp.edit();
        shpEditor.putString(key, value);
        return shpEditor.commit();
    }

    public static boolean saveValueBool(final Context context, final String key, final boolean value){
        SharedPreferences shp = context.getSharedPreferences(BIMII_CACHE_SLOT, Context.MODE_PRIVATE);
        SharedPreferences.Editor shpEditor = shp.edit();
        shpEditor.putBoolean(key, value);
        return shpEditor.commit();
    }

    public static String getValue(final Context context, final String key){
        SharedPreferences shp = context.getSharedPreferences(BIMII_CACHE_SLOT, Context.MODE_PRIVATE);
        return shp.getString(key, null);
    }

    public static boolean getValueBool(final Context context, final String key){
        SharedPreferences shp = context.getSharedPreferences(BIMII_CACHE_SLOT, Context.MODE_PRIVATE);
        return shp.getBoolean(key, false); // Default Launcher "Android" <With UI components>
    }

}
