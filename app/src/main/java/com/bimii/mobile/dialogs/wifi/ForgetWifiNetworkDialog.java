package com.bimii.mobile.dialogs.wifi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.bimii.mobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WORK on 22.09.2015.
 */
public class ForgetWifiNetworkDialog extends Dialog {

    private String                  mSSID;
    private ForgetNetworkCallback   mCallback;

    public ForgetWifiNetworkDialog(Context _context, ForgetNetworkCallback _callback, String _mSSID) {
        super(_context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mSSID       = _mSSID;
        mCallback   = _callback;
    }

    @Bind(R.id.tvMessage_WFND)
    protected TextView tvMessage_FWND;

    @OnClick(R.id.btnOk_WFND)
    protected void clickOk() {
        mCallback.forgetNetwork(mSSID);
        dismiss();
    }

    @OnClick(R.id.btnCancel_WFND)
    protected void clickCancel() {
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_forget_network_dialog);
        ButterKnife.bind(this);
        setCancelable(false);
        tvMessage_FWND.setText(String.format(getContext().getResources().getString(R.string.wifi_forget_network_dialog_msg_format), mSSID));
    }
}
