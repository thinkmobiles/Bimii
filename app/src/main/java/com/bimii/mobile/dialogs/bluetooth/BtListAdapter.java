package com.bimii.mobile.dialogs.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bimii.mobile.R;

import java.util.ArrayList;

/**
 * Created by WORK on 18.09.2015.
 */
public class BtListAdapter extends ArrayAdapter<BluetoothDevice> {

    private Context                         mCtx;
    private ArrayList<BluetoothDevice>      mDeviceList;

    public BtListAdapter(Context _context) {
        super(_context, 0, new ArrayList<BluetoothDevice>());
        mCtx            = _context;
        mDeviceList     = new ArrayList<>();
    }

    //region Adapter callbacks
    @Override
    public int getCount() {
        return mDeviceList.size();
    }

    @Override
    public BluetoothDevice getItem(int _position) {
        return mDeviceList.get(_position);
    }

    @Override
    public long getItemId(int _position) {
        return _position;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if(_convertView == null) _convertView = LayoutInflater.from(mCtx).inflate(R.layout.bt_list_item, _parent, false);
        BluetoothDevice currentDevice       = mDeviceList.get(_position);
        TextView tvDeviceName_BLI           = (TextView) _convertView.findViewById(R.id.tvDeviceName_BLI);
        TextView tvDeviceStatus_BLI         = (TextView) _convertView.findViewById(R.id.tvDeviceStatus_BLI);
        tvDeviceName_BLI            .setText(currentDevice.getName());
        if(getDeviceStatus(currentDevice).equalsIgnoreCase(mCtx.getResources().getString(R.string.bt_item_status_pairing))) {
            tvDeviceStatus_BLI      .setVisibility(View.VISIBLE);
            tvDeviceStatus_BLI      .setText(getDeviceStatus(currentDevice));
        } else if(getDeviceStatus(currentDevice).equalsIgnoreCase(mCtx.getResources().getString(R.string.bt_item_status_paired))) {
            tvDeviceStatus_BLI      .setVisibility(View.VISIBLE);
            tvDeviceStatus_BLI      .setText(getDeviceStatus(currentDevice));
        } else {
            tvDeviceStatus_BLI      .setVisibility(View.GONE);
            tvDeviceStatus_BLI      .setText(getDeviceStatus(currentDevice));
        }
        if(_position % 2 == 0) {    // list view zebra style :)
            _convertView.setBackgroundColor(mCtx.getResources().getColor(R.color.table_first_color));
        } else {
            _convertView.setBackgroundColor(mCtx.getResources().getColor(R.color.table_divider_color));
        }
        return _convertView;
    }
    //endregion

    //region User methods
    /*Update adapter with passed list of BT devices*/
    public void update(ArrayList<BluetoothDevice> _list) {
        mDeviceList     .clear();
        mDeviceList     .addAll(_list);
        notifyDataSetChanged();
    }

    /*Add single BT device to adapter*/
    public void addDevice(BluetoothDevice _device) {
        mDeviceList     .add(_device);
        notifyDataSetChanged();
    }

    /*Get list of available devices*/
    public ArrayList<BluetoothDevice> getData() {
        return mDeviceList;
    }

    /*Get status of passed BT device*/
    private String getDeviceStatus(BluetoothDevice _device) {
        switch (_device.getBondState()) {
            case BluetoothDevice.BOND_NONE:
                return mCtx.getResources().getString(R.string.empty_string);
            case BluetoothDevice.BOND_BONDING:
                return mCtx.getResources().getString(R.string.bt_item_status_pairing);
            case BluetoothDevice.BOND_BONDED:
                return mCtx.getResources().getString(R.string.bt_item_status_paired);
        }
        return null;
    }
    //endregion
}
