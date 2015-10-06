package com.bimii.mobile;

import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;

import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.games.base.BaseHelperFactory;
import com.bimii.mobile.utils.KioskMode;
import com.bimii.mobile.utils.SecureProvider;

public class BimiiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseHelperFactory.setHelper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        BaseHelperFactory.releaseHelper();
        super.onTerminate();
    }

}