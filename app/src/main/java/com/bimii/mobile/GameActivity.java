package com.bimii.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        loadToken();
    }

    private void loadToken(){
        final String token = CacheHelper.getValue(getApplicationContext(), CacheConstants.CACHE_TOKEN);
        if (!TextUtils.isEmpty(token))
            ApiHelper.getInstance().updateToken(token);
        else Toast.makeText(this, "Open Activity with loginisation", Toast.LENGTH_SHORT).show();
    }

}
