package com.bimii.mobile.settings.downloader;

import android.content.pm.PackageInfo;

public interface ProgressListener {
    void transferred(int percent);
    void state(StateDownloading stateDownloading);
    void onResult(PackageInfo pi);

}