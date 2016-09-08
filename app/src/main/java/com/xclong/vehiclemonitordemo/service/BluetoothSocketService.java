package com.xclong.vehiclemonitordemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.constant.Const;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xcl02 on 2016/6/23.
 */
public class BluetoothSocketService extends Service {

    private String TAG = this.getClass().getSimpleName();
    private InputStream is;
    private OutputStream os;
    private int cnt = 0;
    private byte[] bytes = new byte[1024];
    private byte[] recByte = new byte[Const.CMD_LENGTH];
    private String recStr;

    private MyBinder binder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "Service is binder");
        return binder;
    }

    public class MyBinder extends Binder {

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    //TODO Service被断开连接回调该方法
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "service is unbinder");
        return true;
    }

    //TODO  Service被关闭之前回调该方法
    @Override
    public void onDestroy() {
        Log.e(TAG, "service is destory");
        super.onDestroy();
    }

    private void initSocket(){
        if(BluetoothMsg.socket!=null && BluetoothMsg.socket.isConnected()){
            try {
                is = BluetoothMsg.socket.getInputStream();
                os = BluetoothMsg.socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
