package com.xclong.vehiclemonitordemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.Util;
import com.xclong.vehiclemonitordemo.application.App;
import com.xclong.vehiclemonitordemo.constant.Const;

/**
 * Created by xcl02 on 2016/5/20.
 */
public class ShowActivity extends AppCompatActivity {

    private String loginState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aty_show);

        App.getInstance().addActivity(this);

        if (Util.isServiceRunning(this, Const.SERVICE_NAME)) {
            sendBroadcast(new Intent(Const.ACTION6));
        }


        SharedPreferences sharedPreferences = getSharedPreferences(Const.LOGININFO, Context.MODE_WORLD_WRITEABLE); //私有数据
        loginState = sharedPreferences.getString(Const.LOGINSTATE, Const.NOTLOGIN);
        Message msg = Message.obtain();
        switch (loginState) {
            case Const.NOTLOGIN:
                msg.what = 0x01;
                break;
            case Const.ADMINLOGIN:
                msg.what = 0x02;
                break;
            case Const.NORMALLOGIN:
                msg.what = 0x03;
                break;
        }
        handler.sendMessageDelayed(msg, 50);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    startActivity(new Intent(ShowActivity.this, LoginActivity.class));
                    break;
                case 0x02:
                    startActivity(new Intent(ShowActivity.this, BluetoothActivity.class));
                    break;
                case 0x03:
//                    startActivity(new Intent(ShowActivity.this, MainActivity.class));
                    startActivity(new Intent(ShowActivity.this, BluetoothActivity.class));
                    break;
            }
        }
    };
}
