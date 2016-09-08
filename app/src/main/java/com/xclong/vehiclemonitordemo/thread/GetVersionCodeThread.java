package com.xclong.vehiclemonitordemo.thread;

import android.util.Log;

import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.constant.Const;

import java.io.IOException;

/**
 * Created by xcl02 on 2016/6/13.
 */
public class GetVersionCodeThread extends Thread {

    private String TAG = this.getClass().getSimpleName();
    private byte[] bytes = new byte[1024];
    private byte[] recByte = new byte[Const.CMD_LENGTH];
    private String recStr;
    private int cnt = 0;

    @Override
    public void run() {
        if (BluetoothMsg.socket != null && BluetoothMsg.socket.isConnected()) {
            try {
                BluetoothMsg.os.write(Const.CMD26.getBytes());
                if (recvMessage().startsWith(Const.CMD27)) {
                    BluetoothMsg.versionCode = Integer.valueOf(recStr.substring(12, 14)) + "." + Integer.valueOf(recStr.substring(15, 16));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String recvMessage() {
        try {
            cnt = BluetoothMsg.is.read(bytes);
            Log.e(TAG, "cnt_1 = " + cnt);
            if (cnt < 29) {
                while (cnt < Const.CMD_LENGTH) {
                    cnt += BluetoothMsg.is.read(bytes, cnt, 1024 - cnt);
                    Log.e(TAG, "cnt_2 = " + cnt);
                }
            }

            for (int i = 0; i < Const.CMD_LENGTH; i++) {
                recByte[i] = bytes[i];
            }
            recStr = new String(recByte);
            Log.e(TAG, "recStr = " + recStr);
            return recStr;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
