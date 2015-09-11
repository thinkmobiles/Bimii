package com.bimii.mobile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bimii.mobile.R;
import com.bimii.mobile.SettingsActivity;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.settings.downloader.AsyncApkLoader;
import com.bimii.mobile.settings.downloader.ProgressListener;
import com.bimii.mobile.utils.Loh;
import com.bimii.mobile.utils.TextCropper;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadDialog extends Dialog implements ProgressListener{

    public static final int REQUEST_INSTALL_CODE = 27819;
    public static final int REQUEST_UNINSTALL_CODE = 27821;
    public static final String PACKAGE_NAME_EXTRA = "PackageName";

    private SettingsActivity mSettingsActivity;
    private Game mGame;
    private AsyncApkLoader loader;
    private InstallGameEvent ige;

    @Bind(R.id.ivGameIcon_DD)
    protected ImageView imageGame;

    @Bind(R.id.tvNameGameDownloading_DD)
    protected TextView nameGame;

    @Bind(R.id.tvValueProgress_DD)
    protected TextView progressValue;

    @Bind(R.id.pbProgressDownloadGame_DD)
    protected ProgressBar progressLoading;

    public DownloadDialog(Context context, Game game, InstallGameEvent ige) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setCancelable(false);
        this.mSettingsActivity = (SettingsActivity) context;
        this.mGame = game;
        this.ige = ige;
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
        String packageName = getPackageNameByAPK(file.getAbsolutePath(), getContext());
        if (!TextUtils.isEmpty(packageName)) {
            ige.onStartedInstallPackage(packageName);
            Intent installIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            installIntent.setData(Uri.fromFile(file));
            mSettingsActivity.startActivityForResult(installIntent, REQUEST_INSTALL_CODE);
        }
    }

    private String getPackageNameByAPK(String _strAPKPath, Context _context){
        String strRetVal = "";
        PackageManager packMan = null;
        PackageInfo packInfo = null;
        try{

            if(_strAPKPath == null) return "";

            Loh.d("getPackageNameByAPK(): " + _strAPKPath);
            if(_context == null) {
                Loh.e("Context is null");
                return "";
            }

            packMan = _context.getPackageManager();
            packInfo = packMan.getPackageArchiveInfo(_strAPKPath, 0);
            strRetVal = packInfo.packageName;
            Loh.i("Installing package name: " + strRetVal);
        }catch(Exception e){
            Loh.e(e.toString() + "" );
        }

        Loh.d("RetVal: " + strRetVal);
        return strRetVal;
    }

    public static boolean isContainsGameOnDevice(Context context, String packageName){
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void uninstallApplication(SettingsActivity sa, String packageName){
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
        sa.startActivityForResult(intent, REQUEST_UNINSTALL_CODE);
    }

    public interface InstallGameEvent{
        void onStartedInstallPackage(String packageName);
    }
}
