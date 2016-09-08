package com.xclong.vehiclemonitordemo.service;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.constant.Const;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;
import com.xclong.vehiclemonitordemo.database.VehicledInfoDao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xcl02 on 2016/7/13.
 */
public class CommunicationService1 extends Service {

    public static String TAG;
    public InputStream is;
    public OutputStream os;
    public int cnt = 0;
    public byte[] bytes = new byte[1024];
    public byte[] recByte = new byte[Const.CMD_LENGTH];
    public String recStr;
    public static boolean state = true;
    private VehicledInfo vehicledInfo = new VehicledInfo();
    private VehicledInfoDao dao;

    private BluetoothSocket socket;


    private void connectSocket() {
        Log.e(TAG, "连接Socket");
        if (BluetoothMsg.device != null) {
            try {
                if (socket == null) {
                    Method m = BluetoothMsg.device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                    socket = (BluetoothSocket) m.invoke(BluetoothMsg.device, 1);
//                socket = BluetoothMsg.device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Const.BLUETOOTH_UUID));
                    socket.connect();
                    BluetoothMsg.hasConnect = true;
                    Log.e(TAG, "Socket连接成功");
                    if (is == null)
                        is = socket.getInputStream();
                    if (os == null)
                        os = socket.getOutputStream();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "BluetoothMsg.device is null");
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TAG = this.getClass().getSimpleName();
        dao = new VehicledInfoDao(this);
        connectSocket();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (socket == null || !socket.isConnected()) {
            connectSocket();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public class ReceiverThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }
}
