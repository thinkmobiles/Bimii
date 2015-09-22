package com.bimii.mobile.dialogs.bluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.bimii.mobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WORK on 21.09.2015.
 */
public class BtUnpairDialog extends Dialog {

    private BluetoothDevice     mDevice;
    private UnpairCallback      mUnpairCallback;

    public BtUnpairDialog(Context _context, UnpairCallback _unpairCallback, BluetoothDevice _device) {
        super(_context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mDevice             = _device;
        mUnpairCallback     = _unpairCallback;
    }

    @Bind(R.id.tvMessage_BUD)
    protected TextView tvMessage_BUD;

    @OnClick(R.id.btnOk_BUD)
    protected void clickOk() {
        mUnpairCallback.unpair(mDevice);
        dismiss();
    }

    @OnClick(R.id.btnCancel_BUD)
    protected void clickCancel() {
        dismiss();
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.bt_unpair_dialog);
        ButterKnife.bind(this);
        setCancelable(false);
        tvMessage_BUD.setText(String.format(getContext().getResources().getString(R.string.bt_unpaid_dialog_msg_format), mDevice.getName()));
    }
}
