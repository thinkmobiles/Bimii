package com.bimii.mobile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bimii.mobile.R;
import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.utils.SecureProvider;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WORK on 22.09.2015.
 */
public class LauncherSwitchDialog extends Dialog {

    public LauncherSwitchDialog(Context _context) {
        super(_context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_switch_dialog);
        ButterKnife.bind(this);
        setCancelable(false);
    }

    @OnClick(R.id.ivClose_LSD)
    protected void clickClose() {
        dismiss();
    }

    @OnClick(R.id.rlAndroidLauncher_LSD)
    protected void clickAndroidLauncher() {
        boolean isBimiiLauncherNow = CacheHelper.getValueBool(getContext().getApplicationContext(), CacheConstants.CACHE_LAUNCHER_BIMII);
        if (isBimiiLauncherNow){
            CacheHelper.saveValueBool(getContext().getApplicationContext(), CacheConstants.CACHE_LAUNCHER_BIMII, false);
            SecureProvider.setCurrentLauncher(getContext(), false);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            getContext().startActivity(intent);

//            int pid = android.os.Process.myPid();
//            android.os.Process.killProcess(pid);
        } else dismiss();
    }

    @OnClick(R.id.rlBimiiLauncher_LSD)
    protected void clickBimiiLauncher() {
        boolean isBimiiLauncherNow = CacheHelper.getValueBool(getContext().getApplicationContext(), CacheConstants.CACHE_LAUNCHER_BIMII);
        if (!isBimiiLauncherNow){
            CacheHelper.saveValueBool(getContext().getApplicationContext(), CacheConstants.CACHE_LAUNCHER_BIMII, true);
            SecureProvider.setCurrentLauncher(getContext(), true);
        }
        dismiss();
    }

}
