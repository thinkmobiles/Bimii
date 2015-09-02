package com.bimii.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.dialogs.ProgressDialog;
import com.bimii.mobile.games.base.BaseHelperFactory;
import com.bimii.mobile.games.base.DatabaseHelper;
import com.bimii.mobile.utils.FontHelper;
import com.bimii.mobile.utils.Loh;

import java.sql.SQLException;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingsActivity extends Activity implements Callback<List<Game>> {

    private ProgressDialog pdProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        pdProgressView = new ProgressDialog(this);
        loadGames();
    }


    private void loadGames() {
        pdProgressView.show();
        ApiHelper.getInstance().getBimiiService().gameLibrary(ApiHelper.getInstance().getToken().token, this);
    }

    @Override
    public void success(List<Game> games, Response response) {
        pdProgressView.dismiss();
        Loh.i("Success GET_GAMES: " + games.size() + " games in response");
        try {
            BaseHelperFactory.getHelper().updateGames(games);
        } catch (SQLException e) {
            Loh.e("Error when write games in base: " + e.getMessage());
        }
    }

    @Override
    public void failure(RetrofitError error) {
        pdProgressView.dismiss();
        Loh.i("Error GET_GAMES: " + error.getMessage());
    }
}
