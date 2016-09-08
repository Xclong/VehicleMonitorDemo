package com.xclong.vehiclemonitordemo.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.thread.SocketSendCMDThread1;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcl02 on 2016/5/16.
 */
public class BluetoothSendFileActivity extends AppCompatActivity {
    public String TAG = this.getClass().getSimpleName();

    private ListView mListView;
    private ProgressBar progressBar;
    private ArrayAdapter<String> mAdapter;
    private List<String> msgList = new ArrayList<String>();
    private Context mContext;

    private BluetoothSocket socket = null;

    private SocketSendCMDThread1 sendCMDThread = null;
    private ClientConectThread clientConnectThread = null;
    private BluetoothDevice device = null;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        mContext = this;
        init();
    }

    @Override
    protected void onResume() {

        BluetoothMsg.serviceOrCilent = BluetoothMsg.ServerOrCilent.CILENT;

        if (BluetoothMsg.isOpen) {
            Toast.makeText(mContext, "连接已经打开，可以通信。如果要再建立连接，请先断开！", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = BluetoothMsg.BlueToothAddress;
        if (!address.equals("null")) {
            device = mBluetoothAdapter.getRemoteDevice(address);
            clientConnectThread = new ClientConectThread();
            clientConnectThread.start();
            BluetoothMsg.isOpen = true;
        } else {
            Toast.makeText(mContext, "address is null !", Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    //开启客户端
    private class ClientConectThread extends Thread {
        @Override
        public void run() {
            try {
                //创建一个Socket连接：只需要服务器在注册时的UUID号
//                 socket = device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
//                socket = device.createRfcommSocketToServiceRecord(UUID.fromString(Const.BLUETOOTH_UUID));
                Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                socket = (BluetoothSocket) m.invoke(device, 1);
                //连接
                Message msg2 = Message.obtain();
                msg2.obj = "请稍候，正在连接服务器:" + BluetoothMsg.BlueToothAddress;
                msg2.what = 0;
                linkDetectedHandler.sendMessage(msg2);

                socket.connect();

                Message msg = Message.obtain();
                msg.obj = "已经连接上服务端！可以发送信息。";
                msg.what = 0;
                linkDetectedHandler.sendMessage(msg);
                //启动接受数据
                /*mreadThread = new ReadThread();
                mreadThread.start();*/
                sendCMDThread = new SocketSendCMDThread1(getApplicationContext(), linkDetectedHandler);
                sendCMDThread.start();

            } catch (IOException e) {
                Log.e("connect", "", e);
                Message msg = new Message();
                msg.obj = "连接服务端异常！断开连接重新试一试。";
                msg.what = 0;
                linkDetectedHandler.sendMessage(msg);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO 停止客户端连接
    private void shutdownClient() {
        new Thread() {
            @Override
            public void run() {
                if (sendCMDThread != null) {
                    sendCMDThread.interrupt();
                    sendCMDThread = null;
                }

                if (clientConnectThread != null) {
                    clientConnectThread.interrupt();
                    clientConnectThread = null;
                }

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    socket = null;
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shutdownClient();
        BluetoothMsg.isOpen = false;
    }

    private void init() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgList);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setFastScrollEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

     /*   editMsgView = (EditText) findViewById(R.id.MessageText);
        editMsgView.clearFocus();

        sendButton = (Button) findViewById(R.id.btn_msg_send);
        sendButton.setOnClickListener(this);

        disconnectButton = (Button) findViewById(R.id.btn_disconnect);
        disconnectButton.setOnClickListener(this);*/
    }

    public Handler linkDetectedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1:
                    msgList.add((String) msg.obj);
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(msgList.size() - 1);
                    break;
                case 2:
                    progressBar.setMax((Integer) msg.obj);
                    break;
                case 3:
                    progressBar.setProgress((Integer) msg.obj);
                    break;
            }
        }
    };
}
