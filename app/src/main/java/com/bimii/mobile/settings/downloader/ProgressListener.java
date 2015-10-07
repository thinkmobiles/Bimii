package com.bimii.mobile.settings.downloader;

import java.io.File;

public interface ProgressListener {
    void transferred(int percent);
    void onResult(File file, String imagePath);

}