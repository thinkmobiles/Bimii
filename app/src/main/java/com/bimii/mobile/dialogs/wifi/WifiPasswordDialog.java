package com.bimii.mobile.dialogs.wifi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bimii.mobile.R;
import com.bimii.mobile.dialogs.NetworkConstants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WORK on 14.09.2015.
 */
public class WifiPasswordDialog extends Dialog implements TextWatcher {

    private WifiConnectionCallback  mCallback;
    private String                  mSSID;

    public WifiPasswordDialog(Context _context, WifiConnectionCallback _callback, String _ssid) {
        super(_context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mCallback   = _callback;
        mSSID       = _ssid;
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.wifi_pass_dialog);
        ButterKnife.bind(this);
        tvSSID_WPD  .setText(mSSID);
        etPass_WPD  .addTextChangedListener(this);
    }

    @Bind(R.id.tvSSID_WPD)
    protected TextView tvSSID_WPD;

    @Bind(R.id.etPass_WPD)
    protected EditText etPass_WPD;

    @Bind(R.id.cbSavePass_WPD)
    protected CheckBox cbSavePass_WPD;

    @Bind(R.id.btnConnect_WPD)
    protected Button btnConnect_WPD;

    @OnClick(R.id.btnCancel_WPD)
    protected void clickCancel(View _view) {
        dismiss();
    }

    @OnClick(R.id.btnConnect_WPD)
    protected void clickConnect(View _view) {
        if (cbSavePass_WPD.isChecked()) mCallback.savePassword(mSSID, etPass_WPD.getText().toString());
        mCallback.connectToLockedNetwork(mSSID, etPass_WPD.getText().toString());
        dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence _charSequence, int _i, int _i1, int _i2) {

    }

    @Override
    public void onTextChanged(CharSequence _charSequence, int _i, int _i1, int _i2) {
        if (_charSequence.length() >= NetworkConstants.MIN_PASS_LENGTH)
            btnConnect_WPD.setVisibility(View.VISIBLE);
        else
            btnConnect_WPD.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable _editable) {

    }
}
