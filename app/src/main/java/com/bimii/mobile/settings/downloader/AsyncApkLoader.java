package com.bimii.mobile.settings.downloader;

import android.content.Context;
import android.os.AsyncTask;

import com.bimii.mobile.api.ApiConstants;
import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.utils.SecureProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    }

    @Override
    protected File doInBackground(Game... params) {
        final Game game = params[0];
        try {
            final String urlParameters  = "token=" + ApiHelper.getInstance().getToken().token + "&game_id=" + game.getId();
            final URL url = new URL(ApiConstants.API_BASE_URL + "/library/games/download");

            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            connection.getOutputStream().write(urlParameters.getBytes());

            final File resultFile =  saveApkIntoFile(connection.getInputStream(), connection.getContentLength(), SecureProvider.getGameDirectoryFile(mContext, game.getFilename()));

            connection.disconnect();

            return resultFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        mProgressListener.onResult(file);
    }

    private File saveApkIntoFile(final InputStream is, final long maxLength, final File file) throws IOException {
        OutputStream output = new FileOutputStream(file);
        byte[] buffer = new byte[4 * 1024];

        long total = 0;
        int read;
        while ((read = is.read(buffer)) != -1) {
            total += read;
            output.write(buffer, 0, read);
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
