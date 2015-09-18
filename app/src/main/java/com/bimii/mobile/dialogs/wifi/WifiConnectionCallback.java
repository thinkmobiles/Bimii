package com.bimii.mobile.dialogs.wifi;

/**
 * Created by WORK on 14.09.2015.
 */
public interface WifiConnectionCallback {
    void savePassword(String _ssid, String _pass);
    void connectToLockedNetwork(String _ssid, String _pass);
}
