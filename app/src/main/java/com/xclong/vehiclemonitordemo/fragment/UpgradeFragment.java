package com.xclong.vehiclemonitordemo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xclong.vehiclemonitordemo.CustomView.HorizontalProgressbarWithProgress;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.Util;
import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.constant.Const;
import com.xclong.vehiclemonitordemo.constant.MsgEvent2;
import com.xclong.vehiclemonitordemo.constant.MsgEvent3;
import com.xclong.vehiclemonitordemo.thread.GetVersionCodeThread;
import com.xclong.vehiclemonitordemo.thread.SocketSendCMDThread1;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcl02 on 2016/5/30.
 */
public class UpgradeFragment extends BaseFragment implements View.OnClickListener {

    private static UpgradeFragment fragment;


    private State upgradeState;

    public enum State {
        UPGRADEUNABLE,
        UPGRADEABLE,
        UPGRADING,
    }

    public List<String> msgList = new ArrayList<String>();
    public TextView tv_current_version, tv_newest_version, tv_current, tv_newest;
    public Button btn_upgrade;
    public ListView lv_upgrade;
    public HorizontalProgressbarWithProgress pb;

    private ArrayAdapter<String> mAdapter;

    private String currentVersion, newestVersion;

    private SocketSendCMDThread1 thread;

    private static String TAG = "UpgradeFragment";

    private Typeface lightType, regularType;

    private UpgradeBoardcastReceiver receiver = new UpgradeBoardcastReceiver();

    public synchronized static UpgradeFragment newInstance() {
        if (fragment == null) {
            fragment = new UpgradeFragment();
            Log.e(TAG, "UpgradeFragment : newInstance");
        }
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Const.ACTION4);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.frag_upgrade);
        EventBus.getDefault().register(this);

        lightType = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");
        regularType = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");

        tv_current = getViewById(R.id.tv_current);
        tv_newest = getViewById(R.id.tv_newest);

        tv_current_version = getViewById(R.id.tv_current_version);
        tv_newest_version = getViewById(R.id.tv_newest_version);

        tv_current.setTypeface(regularType);
        tv_newest.setTypeface(regularType);
        tv_current_version.setTypeface(lightType);
        tv_newest_version.setTypeface(lightType);


        btn_upgrade = getViewById(R.id.btn_upgrade);
        lv_upgrade = getViewById(R.id.lv_upgrade);
        pb = getViewById(R.id.progressbar_upgrade);

        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, msgList);
        lv_upgrade.setAdapter(mAdapter);
        lv_upgrade.setFastScrollEnabled(true);


        initData();

        getText();

        if (Double.valueOf(currentVersion) <= Double.valueOf(newestVersion)) {
//            btn_upgrade.setClickable(true);
            btn_upgrade.setText("点击升级");
            upgradeState = State.UPGRADEABLE;
        } else {
            tv_current_version.setText(newestVersion);
            btn_upgrade.setText("无需升级");
            upgradeState = State.UPGRADEUNABLE;
        }

    }

    private void initData() {
        new GetVersionCodeThread().start();
//        currentVersion = (Math.random() * 2 + "").substring(0, 3);
        EventBus.getDefault().post(BluetoothMsg.versionCode);
    }

    @Override
    protected void setListener() {
        btn_upgrade.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
       /* if (BluetoothMsg.socket == null) {
            upgradeState = State.UPGRADEUNABLE;
            Toast.makeText(mApp, "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
        } else if (BluetoothMsg.socket != null && BluetoothMsg.socket.isConnected()) {
            upgradeState = State.UPGRADEABLE;
        }*/
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                Log.e(TAG, "升级设备");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        getActivity().unregisterReceiver(receiver);
    }


    private void getText() {
        currentVersion = tv_current_version.getText().toString();
        newestVersion = tv_newest_version.getText().toString();
    }

    @Subscribe
    public void onEventMainThread(String str) {
//        tv_current_version.setText(str);
        Log.e(TAG, "收到Event信息1");
       /* msgList.add(str);
        mAdapter.notifyDataSetChanged();*/
    }

    @Subscribe
    public void onEventMainThread(MsgEvent2 event2) {
        Log.e(TAG, "收到Event信息2");
//        pb.setVisibility(View.VISIBLE);
//        pb.setMax(event2.getMax());
    }

    @Subscribe
    public void onEventMainThread(MsgEvent3 even3) {
        Log.e(TAG, "收到Event信息3");
//        pb.setProgress(even3.getProgress());
    }


    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.btn_upgrade:
                if (btn_upgrade.getText().toString().equals("无需升级")) {
                    Toast.makeText(mApp, "已是最新版，无需升级", Toast.LENGTH_SHORT).show();
                } else {
                    if (thread != null && thread.isAlive())
                        upgradeState = State.UPGRADING;
                    switch (upgradeState) {
                        case UPGRADEUNABLE:
                            Toast.makeText(mApp, "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
                            break;
                        case UPGRADEABLE:
                            btn_upgrade.setText("正在升级中...");
                            thread = new SocketSendCMDThread1(getContext(), BluetoothMsg.socket, linkDetectedHandler);
                            thread.start();
                            break;
                        case UPGRADING:
                            Toast.makeText(mApp, "正在升级中，请勿随意点击", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

        }*/


        if (Util.isServiceRunning(getContext(), Const.SERVICE_NAME)) {

            Intent intent = new Intent();
            intent.setAction(Const.ACTION3);
//            intent.putExtra("toService", true);
            getActivity().sendBroadcast(intent);
            Log.e(TAG, "发送广播，准备升级");

        } else {
            Log.e(TAG, "服务未运行");
            Toast.makeText(mApp, "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
        }
    }

    public Handler getHandler() {
        return linkDetectedHandler;
    }


    public Handler linkDetectedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1:
                    Log.e(TAG, "收到数据");
                    msgList.add((String) msg.obj);
                    mAdapter.notifyDataSetChanged();
                    lv_upgrade.setSelection(msgList.size() - 1);
                    break;
//                case 2:
////                    pb.setVisibility(View.VISIBLE);
//                    pb.setMax((Integer) msg.obj);
//                    break;
                case 3:
                    Log.e(TAG, "收到进度");
                    pb.setProgress((Integer) msg.obj);
                    break;
                case 4:
                    btn_upgrade.setText("升级完成！");
                    break;

            }
        }
    };

    public class UpgradeBoardcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Const.ACTION4)) {
                Log.e(TAG, "收到广播ACTION4");
                int recCount = intent.getIntExtra("upgrade", 1);
                switch (recCount) {
                    case 1:
                        Log.e(TAG, "收到广播ACTION4");
                        msgList.add(intent.getStringExtra("msg"));
                        mAdapter.notifyDataSetChanged();
                        lv_upgrade.setSelection(msgList.size() - 1);
                        break;

                    case 2:
                        pb.setVisibility(View.VISIBLE);
                        pb.setMax(intent.getIntExtra("max", 100));
                        break;

                    case 3:
                        pb.setProgress(intent.getIntExtra("progress", 0));
                        break;
                    case 4:
                        btn_upgrade.setText("升级完成！");
                        break;
                }

            }

        }
    }
}
