package com.bimii.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.utils.FontHelper;
import com.bimii.mobile.utils.Loh;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

}
