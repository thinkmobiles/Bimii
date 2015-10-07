package com.bimii.mobile;

import android.app.Application;

import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.games.base.BaseHelperFactory;
import com.bimii.mobile.utils.FontHelper;
import com.bimii.mobile.utils.SecureProvider;

public class BimiiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseHelperFactory.setHelper(getApplicationContext());
        FontHelper.init(getApplicationContext());
        SecureProvider.setCurrentLauncher(getApplicationContext(), CacheHelper.getValueBool(getApplicationContext(), CacheConstants.CACHE_LAUNCHER_BIMII));
    }

    @Override
    public void onTerminate() {
        BaseHelperFactory.releaseHelper();
        super.onTerminate();
    }

}