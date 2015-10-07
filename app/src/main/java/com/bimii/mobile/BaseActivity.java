package com.bimii.mobile;

import android.app.Activity;
import android.os.Bundle;

import com.bimii.mobile.utils.SecureProvider;

/**
 * Created by Asus_Dev on 10/7/2015.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecureProvider.registerFlags(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SecureProvider.unRegisterFlags(this);
    }

    @Override
    public void onBackPressed() {
    }
}
