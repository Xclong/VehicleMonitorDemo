package com.xclong.vehiclemonitordemo.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.adapter.BluetoothDeviceAdapter;
import com.xclong.vehiclemonitordemo.application.App;
import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.constant.Const;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by xcl02 on 2016/5/16.
 */
public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.tv)
    ListView lv;
    @Bind(R.id.btn_search)
    Button btn_search;

    private SweetAlertDialog mLoadingDialog;

    private String TAG = "BluetoothActivity";
    private BluetoothAdapter bluetoothAdapter;
    //        private MyBaseAdapter adapter;
    private BluetoothDeviceAdapter adapter;
    private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
    private BluetoothDeviceReceiver mReceiver = new BluetoothDeviceReceiver();
    private boolean hasRegister = false;
    private BluetoothSocket socket;
    private String jumpTo = "null";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFinishOnTouchOutside(false);
        setContentView(R.layout.aty_bluetooth);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        jumpTo = intent.getStringExtra("jump");
        Log.e(TAG, "jumpTo = " + jumpTo);

        App.getInstance().addActivity(this);

        setView();
        setBluetooth();
    }

    private void setView() {
        adapter = new BluetoothDeviceAdapter(this, R.layout.list_item_device, deviceList);
//        adapter = new MyBaseAdapter(this, deviceList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    private void setBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent intent1 = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent1, RESULT_FIRST_USER);

                Intent intent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                intent2.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
                startActivity(intent2);

                bluetoothAdapter.enable();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("No Bluetooth Device")
                    .setMessage("Your equipment does not support bluetooth, please change device")
                    .setNegativeButton("cancel", null)
                    .create().show();
        }
    }

    @Override
    protected void onStart() {
        if (!hasRegister) {
            hasRegister = true;
            IntentFilter filterStart = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            IntentFilter filterFinish = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

            registerReceiver(mReceiver, filterStart);
            registerReceiver(mReceiver, filterFinish);
        }
        super.onStart();
    }

    @OnClick(R.id.btn_skip)
    public void skip() {
        bluetoothAdapter.cancelDiscovery();
        if (jumpTo == null) {
            Intent intent = new Intent(BluetoothActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (jumpTo.equals("main")) {
            finish();
        }
    }

    private class BluetoothDeviceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (!deviceList.contains(device)) {
                        if (device.getName().startsWith("ltf"))
                            deviceList.add(device);
                        adapter.notifyDataSetChanged();
                    }
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminate(false);
                if (lv.getCount() == 0) {
                    adapter.notifyDataSetChanged();
                }
                btn_search.setText("搜索蓝牙设备");
            }
        }
    }

    private void findAvalibleDevice() {
        //获取可配对蓝牙设备
        Set<BluetoothDevice> device = bluetoothAdapter.getBondedDevices();

        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            deviceList.clear();
            adapter.notifyDataSetChanged();
        }
        if (device.size() > 0) { //存在已经配对过的蓝牙设备
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                if (btd.getName().startsWith("ltf"))
                    deviceList.add(btd);
                adapter.notifyDataSetChanged();
            }
        } else {  //不存在已经配对过的蓝牙设备

        }
    }

    @OnClick(R.id.btn_search)
    public void onClick() {
        if (bluetoothAdapter.isDiscovering()) {
            setProgressBarIndeterminate(false);
            bluetoothAdapter.cancelDiscovery();
            btn_search.setText("开始搜索设备");
        } else {
            deviceList.clear();
            setProgressBarIndeterminate(true);
            findAvalibleDevice();
            bluetoothAdapter.startDiscovery();
            btn_search.setText("停止搜索设备");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            setProgressBarIndeterminate(false);
            bluetoothAdapter.cancelDiscovery();
            btn_search.setText("开始搜索设备");
        }

        final BluetoothDevice mDevice = deviceList.get(position);

        new AlertDialog.Builder(this)
                .setMessage("确认与设备 : " + mDevice.getName() + " 连接?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BluetoothMsg.BlueToothAddress = mDevice.getAddress();
                        if (BluetoothMsg.lastblueToothAddress != BluetoothMsg.BlueToothAddress) {
                            BluetoothMsg.lastblueToothAddress = BluetoothMsg.BlueToothAddress;
                        }

                        Method createBondMethod = null;
                        try {
                            createBondMethod = BluetoothDevice.class.getMethod("createBond");
                            Log.d("BlueToothActivity", "开始配对");
                            createBondMethod.invoke(mDevice);
                            Log.d("BlueToothActivity", "配对成功");
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                            BluetoothMsg.device = mDevice;

//                            Log.e(TAG, "准备启动服务");
                            Intent serviceIntent = new Intent();
                            serviceIntent.setAction(Const.SERVICE_ACTION);
                            serviceIntent.setPackage(getPackageName());
                            startService(serviceIntent);
//                            Log.e(TAG, "启动服务");


                            Intent intent1 = new Intent();
                            intent1.setAction(Const.ACTION2);
                            intent1.putExtra("state", true);
                            sendBroadcast(intent1);

                            if (jumpTo == null) {
                                Log.e(TAG, "jumpTo is null");
                                Intent intent = new Intent(BluetoothActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else if (jumpTo.equals("main")) {
                                Log.e(TAG, "jumpTo == main");
                                finish();
                            }
                        }

                    }
                })
                .setNegativeButton("取消", null)
                .create().show();

    }

    private void connectDevices(BluetoothDevice mbt) {
        try {
            // 连接建立之前的先配对
            if (mbt.getBondState() == BluetoothDevice.BOND_NONE) {
                Method creMethod = BluetoothDevice.class.getMethod("createBond");
                Log.e("TAG", "开始配对");
                creMethod.invoke(mbt);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("TAG", "无法配对");
            e.printStackTrace();
        }
        bluetoothAdapter.cancelDiscovery();
        try {
            socket.connect();
            Log.e("TAG", "连接成功");
            //connetTime++;
        } catch (IOException e) {
            // TODO: handle exception
            Log.e("TAG", "连接失败");
            try {
                socket.close();
                socket = null;
            } catch (IOException e2) {
                // TODO: handle exception
                Log.e(TAG, "Cannot close connection when connection failed");
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if (hasRegister) {
            hasRegister = false;
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("正在连接中...");
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
