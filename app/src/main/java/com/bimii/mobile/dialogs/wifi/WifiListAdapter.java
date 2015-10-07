package com.bimii.mobile.dialogs.wifi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bimii.mobile.R;
import com.bimii.mobile.dialogs.NetworkConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by WORK on 14.09.2015.
 */
public class WifiListAdapter extends ArrayAdapter<ScanResult> {

    private Context mCtx;
    private List<ScanResult> mNetworksList;

    public WifiListAdapter(Context _context) {
        super(_context, 0, new ArrayList<ScanResult>());
        mCtx            = _context;
        mNetworksList   = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mNetworksList.size();
    }

    @Override
    public ScanResult getItem(int _position) {
        return mNetworksList.get(_position);
    }

    @Override
    public long getItemId(int _position) {
        return _position;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if(_convertView == null)
                    _convertView            = LayoutInflater.from(mCtx).inflate(R.layout.wifi_list_item, _parent, false);
        ScanResult  currentNetwork          = mNetworksList.get(_position);

        TextView    tvNetworkName_WLI       = (TextView)    _convertView.findViewById(R.id.tvNetworkName_WLI);
        TextView    tvNetworkStatus_WLI     = (TextView)    _convertView.findViewById(R.id.tvNetworkStatus_WLI);
        ImageView   ivSygnal_WLI            = (ImageView)   _convertView.findViewById(R.id.ivSygnal_WLI);
        ImageView   ivLock_WLI              = (ImageView)   _convertView.findViewById(R.id.ivLock_WLI);


        tvNetworkName_WLI           .setText(getWifiName(currentNetwork));
        ivLock_WLI                  .setImageDrawable(getWifiLock(currentNetwork));
        if(getWifiState(currentNetwork).equalsIgnoreCase(NetworkConstants.CONNECTED)) {
            tvNetworkStatus_WLI     .setVisibility(View.VISIBLE);
            tvNetworkStatus_WLI     .setText(NetworkConstants.CONNECTED);
        } else {
            tvNetworkStatus_WLI     .setVisibility(View.GONE);
        }
        ivSygnal_WLI                .setImageDrawable(getWifiPower(currentNetwork));
        tvNetworkName_WLI           .setTextColor(mCtx.getResources().getColor(R.color.app_button_text_color));
        tvNetworkStatus_WLI         .setTextColor(mCtx.getResources().getColor(R.color.app_button_text_color_pressed));

        if(_position % 2 == 0) {    // list view zebra style :)
            _convertView.setBackgroundColor(mCtx.getResources().getColor(R.color.table_first_color));
        } else {
            _convertView.setBackgroundColor(mCtx.getResources().getColor(R.color.table_divider_color));
        }
        return _convertView;
    }

    /*Update adapter with new data*/
    public void update(List<ScanResult> _list) {
        Collections.sort(_list, new Comparator<ScanResult>() {  // sort by sygnal level
            @Override
            public int compare(ScanResult scanResult, ScanResult t1) {
                return WifiManager.calculateSignalLevel(t1.level, 4) - WifiManager.calculateSignalLevel(scanResult.level, 4);
            }
        });
        mNetworksList.clear();
        mNetworksList.addAll(_list);
        notifyDataSetChanged();
    }

    /*Get network name*/
    private String getWifiName(ScanResult _sr) {
        return _sr.SSID;
    }

    /*Connected?*/
    private String getWifiState(ScanResult _scanResult) {
        String current = String.format("\"%s\"", _scanResult.SSID);
        if(current.equals(getCurrentWifiName())) return NetworkConstants.CONNECTED;
        return mCtx.getResources().getString(R.string.empty_string);
    }

    /*Get current (connected) wifi network name*/
    public String getCurrentWifiName() {
        WifiManager manager = (WifiManager) mCtx.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return null;
    }

    /*Get wifi secure icon (null if open)*/
    private Drawable getWifiLock(ScanResult _sr) {
        Drawable lock = null;
        String capabilities = _sr.capabilities;
        if (capabilities.toUpperCase().contains(NetworkConstants.WEP)) {
            lock = mCtx.getResources().getDrawable(R.drawable.icn_wifi_status_lock);
        } else if ( capabilities.toUpperCase().contains(NetworkConstants.WPA) ||
                    capabilities.toUpperCase().contains(NetworkConstants.WPA2)) {
            lock = mCtx.getResources().getDrawable(R.drawable.icn_wifi_status_lock);
        }
        return lock;
    }

    /*Get wifi connection power icon*/
    private Drawable getWifiPower(ScanResult _sr) {
        Drawable result = null;
        int level = WifiManager.calculateSignalLevel(_sr.level, 4); // 4 different wifi power states
        switch (level) {
            case 0:
                result = mCtx.getResources().getDrawable(R.drawable.icn_wifi_status_1);
                break;
            case 1:
                result = mCtx.getResources().getDrawable(R.drawable.icn_wifi_status_2);
                break;
            case 2:
                result = mCtx.getResources().getDrawable(R.drawable.icn_wifi_status_3);
                break;
            case 3:
                result = mCtx.getResources().getDrawable(R.drawable.icn_wifi_status_4);
                break;
        }
        return result;
    }
}
