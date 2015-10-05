package com.bimii.mobile.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bimii.mobile.BimiiApplication;
import com.bimii.mobile.LoginActivity;
import com.bimii.mobile.R;
import com.bimii.mobile.SettingsActivity;

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
        //TODO app exit point. Turn off KIOSK mode
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        getContext().startActivity(intent);

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    @OnClick(R.id.rlBimiiLauncher_LSD)
    protected void clickBimiiLauncher() {
        dismiss();
    }

}
