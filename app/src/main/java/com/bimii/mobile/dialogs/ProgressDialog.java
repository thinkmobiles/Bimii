package com.bimii.mobile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.bimii.mobile.R;

/**
 * Created by Asus_Dev on 9/1/2015.
 */
public final class ProgressDialog extends Dialog{

    private View progressRotate;

    public ProgressDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
        setCancelable(false);

        progressRotate = findViewById(R.id.vProgress_PD);
        updateAnimation();
    }

    private RotateAnimation getAnimationRotate(){
        final RotateAnimation ra = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        ra.setDuration(2500);
        ra.setInterpolator(new LinearInterpolator());
        ra.setRepeatMode(Animation.INFINITE);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setStartOffset(0);
        return ra;
    }

    @Override
    public void show() {
        updateAnimation();
        super.show();
    }

    public void updateAnimation(){
        if (progressRotate == null) return;
        progressRotate.clearAnimation();
        progressRotate.startAnimation(getAnimationRotate());
    }
}
