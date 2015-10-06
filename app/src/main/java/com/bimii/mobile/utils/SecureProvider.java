package com.bimii.mobile.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.provider.Settings.Secure;

import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;

import java.io.File;
import java.io.IOException;

public final class SecureProvider {

    public static String getUniqueAndroidId(final Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    private static File getMountAppsDirectoryFile(final Context context) {
        final File moundSD = new File(context.getExternalCacheDir(), "games");

        if (!moundSD.exists()) {
            if (moundSD.mkdir()) return moundSD;
        } else return moundSD;

        return null;
    }

    public static File getGameDirectoryFile(final Context context, final String gameNameWithExtension, final boolean createEmpty) throws IOException {
        final File moundSD = getMountAppsDirectoryFile(context);
        if (moundSD == null) return null;
        final File apkFile = new File(moundSD, gameNameWithExtension);

        if (apkFile.exists())
            apkFile.delete();

        if (createEmpty)
            apkFile.createNewFile();

        return apkFile;
    }


    public static void setCurrentLauncher(Context context, boolean bimiiLauncher){
        final KeyguardManager keyguardManager   = (KeyguardManager) context.getSystemService(Activity.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(Context.KEYGUARD_SERVICE);

        if (bimiiLauncher) {
            KioskMode.on();
            lock.disableKeyguard();
        }
        else {
            KioskMode.off();
            lock.reenableKeyguard();
        }
    }

}
