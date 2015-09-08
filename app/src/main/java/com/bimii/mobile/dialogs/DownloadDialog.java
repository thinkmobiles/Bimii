package com.bimii.mobile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bimii.mobile.R;
import com.bimii.mobile.SettingsActivity;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.settings.downloader.AsyncApkLoader;
import com.bimii.mobile.settings.downloader.ProgressListener;
import com.bimii.mobile.utils.TextCropper;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadDialog extends Dialog implements ProgressListener{

    public static final int REQUEST_INSTALL_CODE = 27819;
    public static final int REQUEST_UNINSTALL_CODE = 27821;

    private SettingsActivity mSettingsActivity;
    private Game mGame;
    private AsyncApkLoader loader;

    @Bind(R.id.ivGameIcon_DD)
    protected ImageView imageGame;

    @Bind(R.id.tvNameGameDownloading_DD)
    protected TextView nameGame;

    @Bind(R.id.tvValueProgress_DD)
    protected TextView progressValue;

    @Bind(R.id.pbProgressDownloadGame_DD)
    protected ProgressBar progressLoading;

    public DownloadDialog(Context context, Game game) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setCancelable(false);
        this.mSettingsActivity = (SettingsActivity) context;
        this.mGame = game;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloading_dialog);
        ButterKnife.bind(this);

        nameGame.setText(TextCropper.getNameIgnoreApk(mGame.getFilename()));
        Picasso.with(mSettingsActivity).load(mGame.getThumbnail_img_url()).into(imageGame);

        loader = new AsyncApkLoader(mSettingsActivity, this);
        loader.execute(mGame);
    }

    @OnClick(R.id.btnCancel_DD)
    protected void cancelDownload(){
        loader.cancel(true);
        dismiss();
    }

    @Override
    public void transferred(int percent) {
        progressValue.setText(String.valueOf(percent) + "%");
        progressLoading.setProgress(percent);
    }

    @Override
    public void onResult(File file) {
        if (file != null)
            installApplication(file);
        dismiss();
    }

    public void installApplication(final File file){
        Intent installIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        installIntent.setData(Uri.fromFile(file));
        installIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        installIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        mSettingsActivity.startActivityForResult(installIntent, REQUEST_INSTALL_CODE);
    }

    public static void uninstallApplication(SettingsActivity sa, String packageName){
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
        sa.startActivityForResult(intent, REQUEST_UNINSTALL_CODE);
    }
}
