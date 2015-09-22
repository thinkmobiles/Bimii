package com.bimii.mobile.dialogs.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by WORK on 18.09.2015.
 */
public class BtReceiver extends BroadcastReceiver {

    private BtUpdateCallback mCallback;

    public BtReceiver(BtUpdateCallback _callback) {
        mCallback = _callback;
    }

    @Override
    public void onReceive(Context _context, Intent _intent) {
        switch (_intent.getAction()) {
            case BluetoothDevice.ACTION_FOUND:                  // found new device
                BluetoothDevice device = _intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mCallback.addSingleDevice(device);
                break;
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED:     // some bound changes
                mCallback.notifyAdapter();
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:     // start scanning
                mCallback.startScan();
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:    // finish scanning
                mCallback.stopScan();
                break;
            case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
                int mode = _intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:   // discoverable on
                        mCallback.goVis();
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:                // discoverable off
                        mCallback.goInvis();
                        break;
                }
                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int code = _intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (code) {
                    case BluetoothAdapter.STATE_ON:
                        mCallback.startScan();      // turned on
                        break;
                }
                break;
        }
    }

}
