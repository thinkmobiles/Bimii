package com.bimii.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.api.models.User;
import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.dialogs.ProgressDialog;
import com.bimii.mobile.utils.Loh;
import com.bimii.mobile.utils.SecureProvider;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends Activity implements Callback<String>{

    @Bind(R.id.etUsername_AL)
    protected EditText mInputUsername;
    @Bind(R.id.etPassword_AL)
    protected EditText mInputPassword;

    private ProgressDialog pdProgressView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        pdProgressView = new ProgressDialog(this);

        if (loadToken())
            openSettingsScreen();
    }


    private boolean loadToken() {
        final String token = CacheHelper.getValue(getApplicationContext(), CacheConstants.CACHE_TOKEN);
        Loh.i("Token in cache: " + token);

        if (!TextUtils.isEmpty(token)) {
            ApiHelper.getInstance().updateToken(token);
            return true;
        }

        return false;
    }

    @OnClick(R.id.tvContinue_AL)
    protected void clickContinue(View viewClicked){
        final String username = mInputUsername.getText().toString();
        final String password = mInputUsername.getText().toString();
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, R.string.login_is_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, R.string.password_is_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        pdProgressView.show();
        ApiHelper.getInstance().getBimiiService().requestToken(new User(username, password, SecureProvider.getUniqueAndroidId(getApplicationContext())), this);
    }

    @Override
    public void success(String token, Response response) {
        Loh.d("Token: " + token);
        pdProgressView.dismiss();
        CacheHelper.saveValue(getApplicationContext(), CacheConstants.CACHE_TOKEN, token);
        ApiHelper.getInstance().updateToken(token);
        openSettingsScreen();
    }

    @Override
    public void failure(RetrofitError error) {
        pdProgressView.dismiss();
        Loh.d("Error GET_TOKEN: " + error.getMessage());
    }

    private void openSettingsScreen(){
        startActivity(new Intent(this, SettingsActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }
}
