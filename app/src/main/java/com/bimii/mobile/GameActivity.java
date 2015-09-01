package com.bimii.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.utils.FontHelper;
import com.bimii.mobile.utils.Loh;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        FontHelper.init(getApplicationContext());
        loadToken();
    }

    private void loadToken(){
        final String token = CacheHelper.getValue(getApplicationContext(), CacheConstants.CACHE_TOKEN);
        Loh.i("Token in cache: " + token);

        if (!TextUtils.isEmpty(token))
            ApiHelper.getInstance().updateToken(token);
        else openLogin();
    }

    private void openLogin(){
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
