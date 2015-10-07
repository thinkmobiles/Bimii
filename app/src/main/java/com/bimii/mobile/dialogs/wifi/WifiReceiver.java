package com.bimii.mobile.dialogs.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.bimii.mobile.R;

/**
 * Created by WORK on 14.09.2015.
 */
public class WifiReceiver extends BroadcastReceiver {

    private WifiUpdateCallback mCallback;

    public WifiReceiver(WifiUpdateCallback _callback) {
        mCallback = _callback;
    }

    @Override
    public void onReceive(Context _context, Intent _intent) {
        switch (_intent.getAction()) {
            case WifiManager.WIFI_STATE_CHANGED_ACTION:         // begin scanning
                mCallback.updateWifiState(_context.getResources().getString(R.string.wifi_status_scanning), true);
                break;
            case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:     // update list view with scan values
                mCallback.updateWifiState(_context.getResources().getString(R.string.empty_string), false);
                break;
            case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                SupplicantState supl_state=(_intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
                switch (supl_state) {
                    case SCANNING:      // scanning
//                        mCallback.updateWifiState(_context.getResources().getString(R.string.wifi_status_scanning), true);
                        mCallback.updateWifiState(_context.getResources().getString(R.string.wifi_status_connecting), true);
                        break;
                    case ASSOCIATING:   // connecting
                        mCallback.updateWifiState(_context.getResources().getString(R.string.wifi_status_connecting), true);
                        break;
                    case COMPLETED:     // connected | finish scanning
                        mCallback.updateWifiState(_context.getResources().getString(R.string.empty_string), false);
                        mCallback.savePass(true);
                        break;
                }
                int supl_error=_intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if(supl_error == WifiManager.ERROR_AUTHENTICATING) {    // password denied
                    mCallback.savePass(false);
                    mCallback.updateWifiState(_context.getResources().getString(R.string.wifi_status_password_denied), false);
                }
                break;
        }
    }
}
