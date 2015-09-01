package com.bimii.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @Bind(R.id.etUsername_AL)
    protected EditText mInputUsername;
    @Bind(R.id.etPassword_AL)
    protected EditText mInputPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvContinue_AL)
    protected void clickContinue(View viewClicked){
        Toast.makeText(this, "Continue", Toast.LENGTH_SHORT).show();
    }
}
