package com.xclong.vehiclemonitordemo.constant;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xcl02 on 2016/3/8.
 */
public class BluetoothMsg {
    /**
     * 蓝牙连接类型
     *
     * @author Andy
     */
    public enum ServerOrCilent {
        NONE,
        SERVICE,
        CILENT
    }

    ;
    //蓝牙连接方式
    public static ServerOrCilent serviceOrCilent = ServerOrCilent.NONE;
    //连接蓝牙地址
    public static String BlueToothAddress = null, lastblueToothAddress = null;
    //通信线程是否开启
    public static boolean isOpen = false;

    public static boolean hasConnect = false;

    public static BluetoothSocket socket;

    public static BluetoothDevice device;


    public static OutputStream os;

    public static InputStream is;

    public static String versionCode = "1.0";

}
