package com.bimii.mobile.dialogs.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.bimii.mobile.R;
import com.bimii.mobile.dialogs.NetworkConstants;

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
        ConnectivityManager connManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo.DetailedState current = mWifi.getDetailedState();

        SupplicantState ss = ((WifiManager) _context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getSupplicantState();

        if(ss == SupplicantState.DISCONNECTED) {
            mCallback.savePass(false);
        }

        Log.d("myLogs", "SP = " + _context.getSharedPreferences(NetworkConstants.SP_NAME,Context.MODE_PRIVATE).getAll().toString()
                + "   STATE = " + current + " SS = " + ss + " >>>>>> TOTAL: " + _intent.getAction());

        switch (_intent.getAction()) {
            case WifiManager.WIFI_STATE_CHANGED_ACTION:         // begin scanning
                int state = _intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 4);
                if(state == WifiManager.WIFI_STATE_ENABLING) mCallback.updateWifiStateWithoutRefresh("Switching-on Wi-Fi..", true);
                else mCallback.updateWifiStateWithoutRefresh(_context.getResources().getString(R.string.wifi_status_scanning), true);
                break;
            case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:     // update list view with scan values
                if(!_intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)) { // !
                    mCallback.updateWifiState(_context.getResources().getString(R.string.empty_string), false);
                }
                break;
            case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                SupplicantState supl_state=(_intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
                int supl_error=_intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if(supl_error == WifiManager.ERROR_AUTHENTICATING) {    // password denied
                    mCallback.savePass(false);
                    mCallback.updateWifiStateWithoutRefresh(_context.getResources().getString(R.string.wifi_status_password_denied), false);
                } else {
                    switch (supl_state) {
                        case ASSOCIATING:   // connecting
                            mCallback.updateOnlyInfo("Associating..", true);
                            break;
                        case COMPLETED:     // connected | finish scanning
                            mCallback.updateWifiStateWithoutRefresh(_context.getResources().getString(R.string.empty_string), false);
                            mCallback.savePass(true);
                            break;
                    }
                }
                break;
        }
    }
}
