package com.xclong.vehiclemonitordemo.thread;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.constant.Const;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xcl02 on 2016/4/12.
 */
public class SocketSendCMDThread1 extends Thread {

    public static String TAG = "SocketSendCMDThread1";
    public String recStr;
    public boolean breakwhile = true;

    public static int iState = 0;
    public static String FILE_NAME = Environment.getExternalStorageDirectory() + File.separator + Const.FILENAME;
    public Context context;
    public Handler handler;
    public String lineCount;
    public BluetoothSocket btSocket;
    public OutputStream os;
    public InputStream is;
    public byte[] bytes = new byte[1024];
    byte[] recByte = new byte[Const.CMD_LENGTH];
    public int cnt;
    public boolean running = true;
    public int s1_length;
    public String[] str_data;
    public int progress = 0;

    public SocketSendCMDThread1(Context context/*, BluetoothSocket socket*/, Handler handler) {
        this.context = context;
//        this.btSocket = socket;
        this.handler = handler;
        init();
    }

    public SocketSendCMDThread1(Context context, BluetoothSocket socket) {
        this.context = context;
        this.btSocket = socket;
//        init(socket);
    }

    public void init() {
        if (BluetoothMsg.device != null) {
            try {
                Method m = BluetoothMsg.device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                btSocket = (BluetoothSocket) m.invoke(BluetoothMsg.device, 1);
                btSocket.connect();
                BluetoothMsg.hasConnect = true;
                Log.e(TAG, "Socket连接成功");
                is = btSocket.getInputStream();
                os = btSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        String str = getFileLineCount(FILE_NAME);
        switch (str.length()) {
            case 1:
                lineCount = "000" + str;
                break;
            case 2:
                lineCount = "00" + str;
                break;
            case 3:
                lineCount = "0" + str;
                break;
            default:
                lineCount = str;
                break;
        }

    }

    @Override
    public void run() {
        try {
            while (running) {
                switch (iState) {
                    //TODO 查询版本号
                    case 0:
                        os.write(Const.CMD26.getBytes());
                        sendIMessage(Const.CMD26, 1);
                        if ((recStr = recvMessage()).startsWith(Const.CMD27)) {
                            String version = Const.DECIMAL_FORMAT.format(Double.valueOf(Integer.parseInt(recStr.substring(12, 14), 16) + "." + Integer.parseInt(recStr.substring(14, 16), 16)));
                            Log.e(TAG, "version = " + version);
                        }
                        iState = 1;
                        break;
                    //TODO  请求更新程序
                    case 1:
                        os.write(Const.CMD1.getBytes());
                        sendIMessage(Const.CMD1, 1);
                        if ((recStr = recvMessage()).equals(Const.CMD9))
                            iState = 2;
                        break;
                    // TODO 发送擦除指令
                    case 2:
                        os.write((Const.CMD2 + lineCount + "FFFFFF*").getBytes());
                        sendIMessage(Const.CMD2 + lineCount + "FFFFFF*", 1);
                        if ((recStr = recvMessage()).equals(Const.CMD12))
                            iState = 3;
                        break;
                    // TODO 发送文件
                    case 3:
                        sendFile(FILE_NAME);
                        break;
                    // TODO 发送文件结束 终止线程
                    case 4:
                        os.write(Const.CMD10.getBytes());
                        sendIMessage(Const.CMD10, 1);
                        if ((recStr = recvMessage()).equals(Const.CMD12)) {
                            sendIMessage("--------重启设备--------", 2);
                            iState = 5;
                        }
                        break;
                    case 5:
                        sendIMessage("--------线程即将关闭--------", 2);
                        sendIMessage("--------升级完成--------", 4);
                        running = false;
                        break;
                    default:
                        iState = 1;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            try {
//                is.close();
//                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void sendIMessage(String str, int i) {
        Message message = Message.obtain();
        message.what = 1;
        if (i == 0) {
            message.obj = "recv: " + str;
        } else if (i == 1) {
            message.obj = "send: " + str;
        } else {
            message.obj = "tips:" + str;
        }
        handler.sendMessage(message);
    }

    private void sendIMessage(int i, int j) {
        Message message = Message.obtain();
        switch (j) {
            case 2:
                message.what = 2;
                message.obj = i;
                break;
            case 3:
                message.what = 3;
                message.obj = i;
                break;
        }
        handler.sendMessage(message);
    }

    private String getFileLineCount(String filename) {
        int cnt = 0;
        LineNumberReader lineNumberReader = null;
        try {
            lineNumberReader = new LineNumberReader(new FileReader(filename));
            while (lineNumberReader.readLine() != null) {
            }
            cnt = lineNumberReader.getLineNumber();
            sendIMessage(cnt, 2);

        } catch (FileNotFoundException e) {
            cnt = -1;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                lineNumberReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "cnt = " + cnt);
        return (Integer.toHexString(cnt)).toUpperCase();
    }

    private boolean sendFile(String filename) {
        if (btSocket == null) {
            Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
            return false;
        }

        InputStream inf = null;
        try {
            inf = new FileInputStream(filename);
            if (inf != null) {
                InputStreamReader ir = new InputStreamReader(inf);
                BufferedReader br = new BufferedReader(ir);
                String str;

                while ((str = br.readLine()) != null) {
                    if (str.startsWith("S0")) continue;
                    else if (str.startsWith("S1")) {
                        s1_length = Integer.valueOf(str.substring(2, 4), 16) - 3;
                        int mod = s1_length % 8;
                        if (mod == 0) {
                            str_data = new String[s1_length / 8 + 3];
                        } else {
                            str_data = new String[s1_length / 8 + 4];
                        }

                        Log.e(TAG, str_data.length + "");

                        for (int i = 1, j = 0; i < str_data.length - 2; i++, j++) {
                            if (i == str_data.length - 3 && mod != 0) {
                                str_data[i] = Const.CMD4 + "0" + mod + str.substring(8 * (2 * (i - 1) + 1), str.length() - 2) + multiFF(mod) + "*";
                            } else {
                                str_data[i] = Const.CMD4 + "08" + str.substring(8 * (i + j), 8 * (i + j + 2)) + "*";
                            }
                        }
                        str_data[0] = Const.CMD3 + "08" + str.substring(4, 8) + str.substring(2, 4) + str.substring(str.length() - 2, str.length()) + "FFFFFFFF*";
                        Log.e(TAG, "str.substring(4, 8)  = " + str.substring(4, 8));
                        Log.e(TAG, "str.substring(2, 4)  = " + str.substring(2, 4));
                        Log.e(TAG, "str.substring(str.length() - 2, str.length() )  = " + str.substring(str.length() - 2, str.length()));
                        str_data[str_data.length - 2] = Const.CMD5;
                        str_data[str_data.length - 1] = Const.CMD6;

                        for (int i = 0; i < str_data.length; i++) {
                            while (breakwhile) {
                                os.write(str_data[i].getBytes());
                                Log.e(TAG, String.format("str_data[%d] = %s ", i, str_data[i]));
                                sendIMessage(str_data[i], 1);
                                recStr = recvMessage();
                                if (recStr.equals(Const.CMD12)) breakwhile = false;
                                else if (recStr.equals(Const.CMD11)) breakwhile = true;
                            }
                            breakwhile = true;
                        }
                        progress++;
                        sendIMessage(progress, 3);
                    } else if (str.endsWith("S9")) break;
                }
                sendIMessage("文件发送完成", 2);
                iState = 4;
                inf.close();
                ir.close();
                br.close();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    private String recvMessage() {

        try {
            cnt = is.read(bytes);
            while (cnt != Const.CMD_LENGTH) {
                cnt += is.read(bytes, cnt, 1024 - cnt);
            }
            for (int i = 0; i < Const.CMD_LENGTH; i++) {
                recByte[i] = bytes[i];
            }
            recStr = new String(recByte);
            sendIMessage(recStr, 0);

            return recStr;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String multiFF(int n) {
        String ff = "";
        if (n == 0) return ff;
        else {
            for (int i = 0; i < 8 - n; i++) {
                ff += "FF";
            }
            return ff;
        }
    }

    private String addZero(String str) {
        String zeroStr = "";
        if (str.length() < 16) {
            for (int i = 0; i < 16 - str.length(); i++) {
                zeroStr += "0";
            }
        }
        return zeroStr + str;
    }

}
