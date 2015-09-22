package com.bimii.mobile.dialogs.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by WORK on 18.09.2015.
 */
public interface BtUpdateCallback {

    void addSingleDevice(BluetoothDevice _device);

    void notifyAdapter();

    void startScan();

    void stopScan();

    void goVis();

    void goInvis();

}
