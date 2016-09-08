package com.xclong.vehiclemonitordemo.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xclong.vehiclemonitordemo.R;

import java.util.List;

/**
 * Created by xcl02 on 2016/5/16.
 */
public class MyBaseAdapter extends BaseAdapter {

    private List<BluetoothDevice> devices;
    private LayoutInflater mInflater;
    private Typeface mTypeFaceLight;
    private Typeface mTypeFaceRegular;

    public MyBaseAdapter(Context context, List<BluetoothDevice> devices) {
        this.mInflater = LayoutInflater.from(context);
        this.devices = devices;

        mTypeFaceLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        mTypeFaceRegular = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_device, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_device_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_device_address);
            holder.tv_boolean = (TextView) convertView.findViewById(R.id.tv_boolean);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setTypeface(mTypeFaceLight);
        holder.tv_address.setTypeface(mTypeFaceLight);
        holder.tv_boolean.setTypeface(mTypeFaceRegular);

        holder.tv_name.setText(devices.get(position).getName());
        holder.tv_address.setText(devices.get(position).getAddress());
        if (devices.get(position).getBondState() == BluetoothDevice.BOND_BONDED) {
            holder.tv_boolean.setVisibility(View.VISIBLE);
            holder.tv_boolean.setText("已配对");
        } else {
            holder.tv_boolean.setVisibility(View.GONE);
        }

        return convertView;
    }

    private final class ViewHolder {
        TextView tv_name;
        TextView tv_address;
        TextView tv_boolean;
    }
}
