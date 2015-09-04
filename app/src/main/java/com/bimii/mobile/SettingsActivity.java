package com.bimii.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.dialogs.DownloadDialog;
import com.bimii.mobile.dialogs.ProgressDialog;
import com.bimii.mobile.games.base.BaseHelperFactory;
import com.bimii.mobile.games.base.DatabaseHelper;
import com.bimii.mobile.settings.GamesSettingsAdapter;
import com.bimii.mobile.settings.downloader.AsyncApkLoader;
import com.bimii.mobile.utils.FontHelper;
import com.bimii.mobile.utils.Loh;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class SettingsActivity extends Activity implements Callback<List<Game>> {

    private ProgressDialog pdProgressView;

    @Bind(R.id.lvGames_AS)
    protected ListView listGames;
    private GamesSettingsAdapter mGamesSettingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        pdProgressView = new ProgressDialog(this);
        loadGames();
    }

    @OnItemClick(R.id.lvGames_AS)
    protected void clickOnItemGames(AdapterView<?> parent, View view, int position, long id){
        new DownloadDialog(this, mGamesSettingsAdapter.getItem(position)).show();
    }


    private void loadGames() {
        pdProgressView.show();
        ApiHelper.getInstance().getBimiiService().gameLibrary(ApiHelper.getInstance().getToken().token, this);
    }

    @Override
    public void success(List<Game> games, Response response) {
        pdProgressView.dismiss();
        Loh.i("Success GET_GAMES: " + games.size() + " games in response");
        listGames.setAdapter(mGamesSettingsAdapter = new GamesSettingsAdapter(this, games));
    }

    @Override
    public void failure(RetrofitError error) {
        pdProgressView.dismiss();
        Loh.i("Error GET_GAMES: " + error.getMessage());
    }
}
