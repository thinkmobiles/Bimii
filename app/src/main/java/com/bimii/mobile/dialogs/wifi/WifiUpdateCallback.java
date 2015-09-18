package com.bimii.mobile.dialogs.wifi;

/**
 * Created by WORK on 14.09.2015.
 */
public interface WifiUpdateCallback {

    void updateWifiState(String _state, boolean _showProgress);

    void savePass(boolean _isSave);

}
