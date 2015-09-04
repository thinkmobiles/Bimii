package com.bimii.mobile.settings.downloader;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;


import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.utils.Loh;
import com.bimii.mobile.utils.SecureProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import retrofit.client.Response;

public class AsyncApkLoader extends AsyncTask<Game, Integer, File> {

    private Context mContext;
    private ProgressListener mProgressListener;

    public AsyncApkLoader(Context context, ProgressListener progressListener) {
        this.mContext = context;
        this.mProgressListener = progressListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressListener.state(StateDownloading.DOWNLOADING);
    }

    @Override
    protected File doInBackground(Game... params) {
        final Game game = params[0];
        final Response response = ApiHelper.getInstance().getBimiiService().downloadGame(ApiHelper.getInstance().getToken().token, game.getId());
        try {
            InputStream is = response.getBody().in();
            return saveApkIntoFile(is, response.getBody().length(), SecureProvider.getGameDirectoryFile(mContext, game.getFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        mProgressListener.state(StateDownloading.INSTALLING);
        mProgressListener.onResult(null);
//        installApplication(file);
    }

    private void installApplication(final File file){
        try {
            ApplicationManager applicationManager = new ApplicationManager(mContext);
            applicationManager.setOnInstalledPackaged(new OnInstalledPackaged() {

                public void packageInstalled(String packageName, int returnCode) {
                    if (returnCode == ApplicationManager.INSTALL_SUCCEEDED) {
                        mProgressListener.state(StateDownloading.COMPLETE);
                        try {
                            mProgressListener.onResult(mContext.getPackageManager().getPackageInfo(packageName, 0));
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                            Loh.e("Error getting package info app");
                            mProgressListener.state(StateDownloading.ERROR);
                            mProgressListener.onResult(null);
                        }
                        Loh.i("Install apk succeeded");
                    } else {
                        mProgressListener.state(StateDownloading.ERROR);
                        Loh.e("Install apk failed: " + returnCode);
                    }
                }
            });

            applicationManager.installPackage(file);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            mProgressListener.state(StateDownloading.ERROR);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            mProgressListener.state(StateDownloading.ERROR);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            mProgressListener.state(StateDownloading.ERROR);
        }
    }

    private File saveApkIntoFile(final InputStream is, final long maxLength, final File file) throws IOException {
        OutputStream output = new FileOutputStream(file);
        byte[] buffer = new byte[4 * 1024];

        long total = 0;
        int read;
        while ((read = is.read(buffer)) != -1) {
            total += read;
            output.write(buffer, 0, read);
            Loh.e("" + total);
            publishProgress((int) (total * 100 / maxLength));
        }

        output.flush();
        is.close();
        return file;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgressListener.transferred(values[0]);
    }
}
