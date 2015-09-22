package com.bimii.mobile.dialogs.bluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bimii.mobile.R;
import com.bimii.mobile.dialogs.NetworkConstants;

import java.lang.reflect.Method;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

/**
 * Created by WORK on 18.09.2015.
 */
public class BtDialog extends Dialog implements BtUpdateCallback, UnpairCallback {

    private Context             mCtx;

    private BluetoothAdapter    mBtAdapter;
    private BtListAdapter       mBtListAdapter;
    private BtReceiver          mBtReceiver;

    //region Butterknife binds
    @Bind(R.id.pbStatus_BD)
    protected ProgressBar pbStatus_BD;

    @Bind(R.id.tvStatus_BD)
    protected TextView tvStatus_BD;

    @Bind(R.id.swBt_BD)
    protected Switch swBt_BD;

    @Bind(R.id.lvDevices_BD)
    protected ListView lvDevices_BD;

    @Bind(R.id.cbVisible_BD)
    protected CheckBox cbVisible_BD;
    //endregion

    public BtDialog(Context _context) {
        super(_context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mCtx = _context;
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.bt_dialog);
        ButterKnife.bind(this);
        setCancelable(false);

        mBtAdapter          = BluetoothAdapter.getDefaultAdapter();
        mBtListAdapter      = new BtListAdapter(mCtx);
        mBtReceiver         = new BtReceiver(this);
        lvDevices_BD        .setAdapter(mBtListAdapter);

        if(mBtAdapter.isEnabled()) onBT();
    }

    //region Butterknife bind listeners
    @OnClick(R.id.ivCancel_BD)
    protected void clickClose() {
        dismiss();
        if(mBtAdapter.isEnabled()) mCtx.unregisterReceiver(mBtReceiver);
    }

    @OnCheckedChanged(R.id.swBt_BD)
    protected void enableBT(boolean _enabled) {
        if(_enabled) {
            onBT();
        } else {
            offBT();
            cbVisible_BD.setChecked(false);
        }
    }

    @OnItemClick(R.id.lvDevices_BD)
    protected void clickBTListItem(int _i) {
        pairToDevice(mBtListAdapter.getItem(_i));
    }

    @OnItemLongClick(R.id.lvDevices_BD)
    protected boolean longClickBTListItem(int _i) {
        final BluetoothDevice device = mBtListAdapter.getItem(_i);
        if(mBtAdapter.getBondedDevices().contains(device)) {
            new BtUnpairDialog(mCtx, this, device).show();
        }
        return true;
    }

    @OnClick(R.id.btnScan_BD)
    protected void clickScan() {
        if(mBtAdapter.isEnabled()) mBtAdapter.startDiscovery();
        else Toast.makeText(mCtx, mCtx.getResources().getString(R.string.bt_toast_turn_on_bt), Toast.LENGTH_SHORT).show();
    }

    @OnCheckedChanged(R.id.cbVisible_BD)
    protected void goVisible(boolean _isVisible) {
        if(mBtAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            cbVisible_BD.setChecked(true);
            return;
        }
        if(_isVisible) {
            cbVisible_BD.setChecked(false);
            mCtx.startActivity(
                    new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                            .putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, NetworkConstants.DISCOVERABLE_DURATION));   // for 120 sec
        }
    }
    //endregion

    //region User methods
    /*Turn on Bluetooth*/
    private void onBT() {
        startReceiver();
        startScan();
        tvStatus_BD     .setVisibility(View.VISIBLE);
        mBtAdapter      .enable();
        swBt_BD         .setChecked(true);
        tvStatus_BD     .setText(mCtx.getResources().getString(R.string.empty_string));
        cbVisible_BD    .setEnabled(true);
        if(mBtAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) cbVisible_BD.setChecked(true);
    }

    /*Turn off Bluetooth*/
    private void offBT() {
        if(mBtAdapter.isEnabled()) {
            mBtAdapter      .disable();
            cbVisible_BD    .setEnabled(false);
        }
        mBtListAdapter  .update(new ArrayList<BluetoothDevice>());
        tvStatus_BD     .setText(mCtx.getResources().getString(R.string.bt_msg_turn_on));
        pbStatus_BD     .setVisibility(View.INVISIBLE);
        try {
            mCtx.unregisterReceiver(mBtReceiver);
        } catch (IllegalArgumentException e) {}
    }

    /*Start bluetooth receiver with defined intent filter*/
    private void startReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mCtx.registerReceiver(mBtReceiver, intentFilter);
    }

    /*Pair with passed bluetooth device*/
    private void pairToDevice(BluetoothDevice _device) {
        try {
            Method m = _device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(_device, (Object[]) null);
        } catch (Exception e) {
        }
    }

    /*Unpair with passed bluetooth device*/
    private void unpairFromDevice(BluetoothDevice _device) {
        try {
            Method m = _device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(_device, (Object[]) null);
        } catch (Exception e) {
        }
    }
    //endregion

    //region BtUpdateCallback methods
    @Override
    public void addSingleDevice(BluetoothDevice _device) {
        if(!mBtListAdapter.getData().contains(_device)) mBtListAdapter.addDevice(_device);
        mBtListAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyAdapter() {
        mBtListAdapter.notifyDataSetChanged();
    }

    @Override
    public void startScan() {
        mBtAdapter      .startDiscovery();
        tvStatus_BD     .setText(mCtx.getResources().getString(R.string.bt_msg_scanning));
        pbStatus_BD     .setVisibility(View.VISIBLE);
    }

    @Override
    public void stopScan() {
        tvStatus_BD     .setText(mCtx.getResources().getString(R.string.empty_string));
        pbStatus_BD     .setVisibility(View.INVISIBLE);
    }

    @Override
    public void goVis() {
        cbVisible_BD.setChecked(true);
    }

    @Override
    public void goInvis() {
        cbVisible_BD.setChecked(false);
    }

    @Override
    public void unpair(BluetoothDevice _device) {
        unpairFromDevice(_device);
    }
    //endregion

}
