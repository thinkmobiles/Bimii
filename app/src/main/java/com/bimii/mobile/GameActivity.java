package com.bimii.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.api.models.Game;
import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.utils.FontHelper;
import com.bimii.mobile.utils.Loh;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameActivity extends Activity implements Callback<List<Game>>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        FontHelper.init(getApplicationContext());
        if (loadToken())
            loadGames();
    }

    private boolean loadToken() {
        final String token = CacheHelper.getValue(getApplicationContext(), CacheConstants.CACHE_TOKEN);
        Loh.i("Token in cache: " + token);

        if (!TextUtils.isEmpty(token)) {
            ApiHelper.getInstance().updateToken(token);
            return true;
        } else openLogin();

        return false;
    }

    private void loadGames(){
        ApiHelper.getInstance().getBimiiService().gameLibrary(ApiHelper.getInstance().getToken().token, this);
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void success(List<Game> games, Response response) {
        Loh.i("Response Games count: " + games.size());

    }

    @Override
    public void failure(RetrofitError error) {
        Loh.i("Error GET_GAMES: " + error.getMessage());
    }
}
