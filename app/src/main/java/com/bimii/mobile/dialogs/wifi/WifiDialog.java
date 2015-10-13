package com.bimii.mobile.dialogs.wifi;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.bimii.mobile.R;
import com.bimii.mobile.dialogs.NetworkConstants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

/**
 * Created by WORK on 11.09.2015.
 */
public final class WifiDialog extends Dialog implements WifiUpdateCallback, WifiConnectionCallback, ForgetNetworkCallback {

    private Context                     mCtx;

    private WifiManager                 mWifiManager;
    private WifiListAdapter             mWifiListAdapter;
    private WifiReceiver                mWifiReceiver;
    private SharedPreferences           mSharedPreferences;
    private SharedPreferences.Editor    mEditor;

    @Bind(R.id.pbStatus_WD)
    protected ProgressBar   pbStatus_WD;

    @Bind(R.id.tvStatus_WD)
    protected TextView      tvStatus_WD;

    @Bind(R.id.swWifi_WD)
    protected Switch        swWifi_WD;

    @Bind(R.id.lvNetworks_WD)
    protected ListView      lvNetworks_WD;

    public WifiDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mCtx = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_dialog);
        ButterKnife.bind(this);
        setCancelable(false);

        mWifiListAdapter    = new WifiListAdapter(mCtx);
        mWifiManager        = (WifiManager) mCtx.getSystemService(Context.WIFI_SERVICE);
        mWifiReceiver       = new WifiReceiver(this);
        mSharedPreferences  = mCtx.getSharedPreferences(NetworkConstants.SP_NAME, Context.MODE_PRIVATE);
        mEditor             = mSharedPreferences.edit();

        lvNetworks_WD       .setAdapter(mWifiListAdapter);
        if(mWifiManager.isWifiEnabled()) swWifi_WD.setChecked(true);
    }

    @OnClick(R.id.ivCancel_WD)
    protected void clickClose() {
        dismiss();
        mCtx.unregisterReceiver(mWifiReceiver);
    }

    @OnCheckedChanged(R.id.swWifi_WD)
    protected void enableWifi(boolean _enabled) {
        if(_enabled) {   // wifi on
            IntentFilter iFilter = new IntentFilter();
                iFilter     .addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                iFilter     .addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
                iFilter     .addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            mCtx                .registerReceiver(mWifiReceiver, iFilter);
            mWifiManager        .setWifiEnabled(true);
            mWifiManager        .startScan();
        } else {    // wifi off
            mWifiManager        .setWifiEnabled(false);
            mCtx                .unregisterReceiver(mWifiReceiver);
            mWifiListAdapter    .update(new ArrayList<ScanResult>());
            tvStatus_WD         .setText(mCtx.getResources().getString(R.string.wifi_status_turn_on));
            pbStatus_WD         .setVisibility(View.INVISIBLE);
        }
    }

    @OnItemClick(R.id.lvNetworks_WD)
    protected void clickWifiListItem(View view) {
        String ssid = ((TextView) view.findViewById(R.id.tvNetworkName_WLI)).getText().toString();
        if(!ssid.equalsIgnoreCase(getCurrentSsid(getContext()))) {
            if (((ImageView) view.findViewById(R.id.ivLock_WLI)).getDrawable() != null) {
                if (mSharedPreferences.contains(ssid)) {
                    String pass = mSharedPreferences.getString(ssid, mCtx.getResources().getString(R.string.empty_string));
                    connectToLockedNetwork(ssid, pass);
                } else
                    new WifiPasswordDialog(getContext(), this, ssid).show();
            } else {
                connectToOpenNetwork(ssid);
            }
        }
    }

    /*Show forget network dialog | w/o disconnect*/
    @OnItemLongClick(R.id.lvNetworks_WD)
    protected boolean forgetWifi(View view) {
        final String ssid = ((TextView) view.findViewById(R.id.tvNetworkName_WLI)).getText().toString();
        final String state = ((TextView) view.findViewById(R.id.tvNetworkStatus_WLI)).getText().toString();
        if(state.equalsIgnoreCase(NetworkConstants.CONNECTED)) {
            new ForgetWifiNetworkDialog(mCtx, this, ssid).show();
        }
        return true;
    }

    /*Save network password to shared preferences*/
    @Override
    public void savePassword(String _ssid, String _pass) {
        mEditor = null;
        mEditor = mSharedPreferences.edit();
        mEditor.putString(_ssid, _pass);
    }

    /*Connect to open wifi network*/
    private void connectToOpenNetwork(String _ssid) {
        tvStatus_WD.setText("Connecting..");
        pbStatus_WD.setVisibility(View.VISIBLE);

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", _ssid);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int id = mWifiManager.addNetwork(conf);
        mWifiManager.enableNetwork(id, true);
        mWifiManager.setWifiEnabled(true);
    }

    /*Connect to locked wifi network*/
    public void connectToLockedNetwork(String _ssid, String _pass) {
        tvStatus_WD.setText("Connecting..");
        pbStatus_WD.setVisibility(View.VISIBLE);

        WifiConfiguration wifiConfiguration     = new WifiConfiguration();
        wifiConfiguration.SSID                  = String.format("\"%s\"", _ssid);
        wifiConfiguration.preSharedKey          = String.format("\"%s\"", _pass);

        int netId = mWifiManager.addNetwork(wifiConfiguration);

        mWifiManager.enableNetwork(netId, true);
        mWifiManager.setWifiEnabled(true);
    }

    /*Return current wifi network name*/
    public String getCurrentSsid(Context _context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        if(ssid != null) ssid = ssid.substring(1, ssid.length()-1);
        return ssid;
    }

    @Override
    public void updateWifiState(String _state, boolean _showProgress) {
        tvStatus_WD.setText(_state);
        if(_showProgress) pbStatus_WD.setVisibility(View.VISIBLE);
        else pbStatus_WD.setVisibility(View.INVISIBLE);

        mWifiListAdapter.update(mWifiManager.getScanResults());
    }

    @Override
    public void updateWifiStateWithoutRefresh(String _state, boolean _showProgress) {
        tvStatus_WD.setText(_state);
        if(_showProgress) pbStatus_WD.setVisibility(View.VISIBLE);
        else pbStatus_WD.setVisibility(View.INVISIBLE);
        mWifiListAdapter.notifyDataSetChanged();
    }

    @Override
    public void savePass(boolean _isSave) {
        if(_isSave) {
            mEditor.commit();
            mEditor = mCtx.getSharedPreferences(NetworkConstants.SP_NAME, Context.MODE_PRIVATE).edit();
        } else {
            mEditor = null;
            mEditor = mSharedPreferences.edit();
        }

    }

    @Override
    public void forgetNetwork(String _ssid) {
        if(mSharedPreferences.contains(_ssid)) mSharedPreferences.edit().remove(_ssid).commit();
        mWifiManager.disconnect();
        mWifiListAdapter.notifyDataSetChanged();
    }
}
