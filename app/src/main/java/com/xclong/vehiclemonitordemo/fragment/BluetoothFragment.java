package com.xclong.vehiclemonitordemo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.xclong.vehiclemonitordemo.R;

/**
 * Created by xcl02 on 2016/5/19.
 */
public class BluetoothFragment extends BaseFragment {

    private String TAG = this.getClass().getSimpleName();
    private ListView lv_devices;
    private Button btn_search;
    private static BluetoothFragment fragment;

    public synchronized static BluetoothFragment newInstance() {
        if (fragment == null) {
            fragment = new BluetoothFragment();
        }
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.frag_bluetooth);
        Log.e(TAG, "initView");
        lv_devices = getViewById(R.id.lv_devices);
        btn_search = getViewById(R.id.btn_search);

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void lazyLoad() {

    }
}
