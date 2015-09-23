package com.bimii.mobile;

import android.app.Application;
import android.util.Log;

import com.bimii.mobile.games.base.BaseHelperFactory;

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