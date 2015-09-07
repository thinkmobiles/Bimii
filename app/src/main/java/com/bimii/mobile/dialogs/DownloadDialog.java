package com.bimii.mobile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.bimii.mobile.R;
import com.bimii.mobile.SettingsActivity;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.settings.downloader.AsyncApkLoader;
import com.bimii.mobile.settings.downloader.ProgressListener;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DownloadDialog extends Dialog implements ProgressListener{

    public static final int REQUEST_INSTALL_CODE = 27819;

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
        new AsyncApkLoader(getContext(), this).execute(mGame);
    }

    @Override
    public void transferred(int percent) {
//        Loh.e(String.valueOf(percent));
    }

    @Override
    public void onResult(File file) {

//        getContext().startActivity(getContext().getPackageManager().getLaunchIntentForPackage(pi.packageName));
        if (file != null)
            installApplication(file);
        dismiss();
    }

    private void installApplication(final File file){
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        ((SettingsActivity) getContext()).startActivityForResult(intent, REQUEST_INSTALL_CODE);

    }
}
