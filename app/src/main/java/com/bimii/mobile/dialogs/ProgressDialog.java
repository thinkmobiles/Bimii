package com.bimii.mobile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.bimii.mobile.R;

/**
 * Created by Asus_Dev on 9/1/2015.
 */
public final class ProgressDialog extends Dialog{

    public ProgressDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
        setCancelable(false);
    }
}
