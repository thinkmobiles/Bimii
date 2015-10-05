package com.bimii.mobile;

import android.app.Application;
import android.util.Log;

import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.games.base.BaseHelperFactory;
import com.bimii.mobile.utils.KioskMode;

public class BimiiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseHelperFactory.setHelper(getApplicationContext());
        checkLauncher();
    }
    @Override
    public void onTerminate() {
        BaseHelperFactory.releaseHelper();
        super.onTerminate();
    }

    private void checkLauncher(){
        boolean isBimiiLauncherNow = CacheHelper.getValueBool(getApplicationContext(), CacheConstants.CACHE_LAUNCHER_BIMII);
        if (isBimiiLauncherNow)
            KioskMode.on();
        else KioskMode.off();
    }
}