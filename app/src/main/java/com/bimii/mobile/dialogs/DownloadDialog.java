package com.bimii.mobile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.bimii.mobile.R;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.settings.downloader.AsyncApkLoader;
import com.bimii.mobile.settings.downloader.ProgressListener;
import com.bimii.mobile.settings.downloader.StateDownloading;
import com.bimii.mobile.utils.Loh;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DownloadDialog extends Dialog implements ProgressListener{

    @Bind(R.id.tvStateLoading_DD)
    protected TextView progressInfo;

    private Game mGame;

    public DownloadDialog(Context context, Game game) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setCancelable(false);

        this.mGame = game;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloading_dialog);
        ButterKnife.bind(this);
        new AsyncApkLoader(getContext(), this).execute(mGame);
    }

    @Override
    public void transferred(int percent) {
        Loh.e(String.valueOf(percent));
    }

    @Override
    public void state(StateDownloading stateDownloading) {
        progressInfo.setText(stateDownloading.name());
    }

    @Override
    public void onResult(PackageInfo pi) {
        Loh.i(pi == null ? "O" : pi.packageName + " - COMPELETE INSTALLING");
        dismiss();
    }
}
