package com.xclong.vehiclemonitordemo.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.adapter.base.BaseViewHolder;
import com.xclong.vehiclemonitordemo.adapter.base.CommonAdapter;

import java.util.List;

import static com.xclong.vehiclemonitordemo.R.id.tv_boolean;

/**
 * Created by xcl02 on 2016/5/16.
 */
public class BluetoothDeviceAdapter extends CommonAdapter<BluetoothDevice> {

    //    private List<BluetoothDevice> devices;
    private Typeface mTypeFaceLight;
    private Typeface mTypeFaceRegular;

    public BluetoothDeviceAdapter(Context context, int layoutId, List<BluetoothDevice> devices) {
        super(context, layoutId, devices);
        mTypeFaceLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        mTypeFaceRegular = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");

    }

    @Override
    public void convert(BaseViewHolder holder, BluetoothDevice bluetoothDevice) {
        holder.setTypeface(R.id.tv_device_name, mTypeFaceLight);
        holder.setTypeface(R.id.tv_device_address, mTypeFaceLight);
        holder.setTypeface(tv_boolean, mTypeFaceRegular);

        holder.setText(R.id.tv_device_name, bluetoothDevice.getName());
        holder.setText(R.id.tv_device_address, bluetoothDevice.getAddress());
        holder.setVisibility(R.id.tv_boolean, bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED ? View.VISIBLE : View.GONE);
    }
}
