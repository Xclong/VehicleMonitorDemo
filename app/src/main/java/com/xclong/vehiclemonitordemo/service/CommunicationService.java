package com.xclong.vehiclemonitordemo.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.xclong.vehiclemonitordemo.Conversations;
import com.xclong.vehiclemonitordemo.NotificationUtil;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.Util;
import com.xclong.vehiclemonitordemo.constant.AbnormalInfo;
import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.constant.Const;
import com.xclong.vehiclemonitordemo.constant.MsgEvent2;
import com.xclong.vehiclemonitordemo.constant.MsgEvent3;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;
import com.xclong.vehiclemonitordemo.database.AbnormalInfoDao;
import com.xclong.vehiclemonitordemo.database.VehicledInfoDao;
import com.xclong.vehiclemonitordemo.fragment.UpgradeFragment;

import org.greenrobot.eventbus.EventBus;

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
import java.util.Date;
import java.util.Iterator;

/**
 * Created by xcl02 on 2016/5/20.
 */
public class CommunicationService extends Service {

    /*private MyBinder binder = new MyBinder();

    public class MyBinder extends Binder {

    }*/


    private static CommunicationService communicationService;
    public static String TAG;
    public InputStream is;
    public OutputStream os;
    public int cnt = 0, row = 2;
    public byte[] bytes = new byte[1024];
    public byte[] recByte = new byte[Const.CMD_LENGTH];
    public String recStr;
    public static boolean state = true;
    public static boolean running = true;
    private VehicledInfo vehicledInfo = new VehicledInfo();
    private VehicledInfoDao vdao;
    private AbnormalInfoDao adao;

    private BluetoothSocket socket;

    private SimplexThread simplexThread = new SimplexThread();
    private UpdateThread updateThread = new UpdateThread();
    private LicensePlateSettingThread licensePlateSettingThread;
    private IPSettingThread ipSettingThread;

    private String car_number, plate_color, average_speed, engine_speed, total_mileage, differential_perssure_value, NOx_F, NOx_R, DNOx_Effi,
            TR21, L19, TC90, TC92, freq_pump, value1, value2, value3, value4, power, twoway_value_failure, metering_pump_failure,
            nozzle_block_failure, LQ8486_failure, NOx_overproof_failure, NOx_sensor_failure, urea_level_failure, urea_quality_failure, SCR_failure,
            dePM_catalyzer_failure, year, month, day, hour, minute, second;

//    private Timer timer;
//    private int i = 0;

    private BackgroundServiceReceiver receiver = new BackgroundServiceReceiver();
    private boolean hasRegesiger = false;

    private int[] abnormals = new int[Const.ABNORMALNUMBER];
    public static final String READ_ACTION = "com.xclong.messengerdemo.ACTION_MESSAGE_READ";
    private static final String CONVERSATION_ID = "conversation_id";
    private NotificationManagerCompat mNotificationManager;
    public static final String EOL = "\n";


    public synchronized static CommunicationService newInstance() {
        if (communicationService == null) {
            communicationService = new CommunicationService();
            Log.e(TAG, "HomePageFragment : newInstance");
        }
        return communicationService;
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
        communicationService = CommunicationService.newInstance();
        mNotificationManager = NotificationManagerCompat.from(getApplicationContext());

        Log.e(TAG, "服务启动");
        //TODO 注册广播
        if (!hasRegesiger) {
            hasRegesiger = true;
            Log.e(TAG, "注册广播");
            IntentFilter filter1 = new IntentFilter(Const.ACTION2);
            registerReceiver(receiver, filter1);

            IntentFilter filter2 = new IntentFilter(Const.ACTION3);
            registerReceiver(receiver, filter2);

            IntentFilter filter3 = new IntentFilter(Const.ACTION6);
            registerReceiver(receiver, filter3);

            IntentFilter filter4 = new IntentFilter(Const.ACTION5);
            registerReceiver(receiver, filter4);

            IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            registerReceiver(receiver, filter5);

            IntentFilter filter6 = new IntentFilter(Const.ACTION7);
            registerReceiver(receiver, filter6);

            IntentFilter filter7 = new IntentFilter(Const.ACTION9);
            registerReceiver(receiver, filter7);


        }
        vdao = new VehicledInfoDao(this);
        adao = new AbnormalInfoDao(this);

        connectSocket();
    }

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

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (socket == null || !socket.isConnected()) {
            connectSocket();
        }


        if (!simplexThread.isAlive()) {
            state = true;
            simplexThread.start();
        }

     /*   timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                vehicledInfo.setL19(++i + "");
                vehicledInfo.setTime(Const.SDF.format(new Date()));
                Log.e(TAG, vehicledInfo.toString());
                dao.add(vehicledInfo);

                Intent intent = new Intent();
                intent.setAction(Const.ACTION1);
                Bundle bundle = new Bundle();
                bundle.putSerializable("broadcast", vehicledInfo);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        }, 0, 5000);*/

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        timer.cancel();

        unregisterReceiver(receiver);
        if (simplexThread.isAlive()) {
            simplexThread.interrupt();
            simplexThread = null;
        }

        if (updateThread.isAlive()) {
            updateThread.interrupt();
            updateThread = null;
        }

        disconnectSocket();
        BluetoothMsg.hasConnect = false;
        Log.e(TAG, "服务停止运行");

    }

    private String recvMessage() {
        try {
            if (is != null) {

                cnt = is.read(bytes);
                if (cnt < 29) {
                    while (cnt < Const.CMD_LENGTH) {
                        cnt += is.read(bytes, cnt, 1024 - cnt);
                    }
                }
                System.arraycopy(bytes, 0, recByte, 0, Const.CMD_LENGTH);
               /* for (int i = 0; i < Const.CMD_LENGTH; i++) {
                    recByte[i] = bytes[i];
                }*/
                recStr = new String(recByte);
                if (recStr != null && recStr.startsWith("$") && recStr.endsWith("*")) {
                    Log.e(TAG, "recStr = " + recStr);
                    return recStr;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void dealWithCMD13() {
        switch (recStr.substring(14, 16)) {
            case "01":
                car_number = "鄂";
                break;
            case "02":
                car_number = "鲁";
                break;
            case "05":
                car_number = "豫";
                break;
            default:
                car_number = "鄂";
                break;
        }

        car_number += Util.hexToAsciiStr(recStr.substring(16, 18)) +
                Util.hexToAsciiStr(recStr.substring(18, 20)) +
                Util.hexToAsciiStr(recStr.substring(20, 22)) +
                Util.hexToAsciiStr(recStr.substring(22, 24)) +
                Util.hexToAsciiStr(recStr.substring(24, 26)) +
                Util.hexToAsciiStr(recStr.substring(26, 28));
        vehicledInfo.setCar_number(car_number);
    }

    private void dealWithCMD14() {
        plate_color = recStr.substring(14, 16).equals("00") ? "蓝牌" : "黄牌";
        average_speed = Integer.valueOf(recStr.substring(16, 18), 16).toString();
        engine_speed = Integer.valueOf(recStr.substring(18, 22), 16).toString();
        total_mileage = Integer.valueOf(recStr.substring(22, 28), 16).toString();
        vehicledInfo.setPlate_color(plate_color);
        vehicledInfo.setAverage_speed(average_speed);
        vehicledInfo.setEngine_speed(engine_speed);
        vehicledInfo.setTotal_mileage(total_mileage);
    }

    private void dealWithCMD15() {
        differential_perssure_value = Integer.valueOf(recStr.substring(14, 16), 16).toString();
        NOx_F = Integer.valueOf(recStr.substring(16, 20), 16).toString();
        NOx_R = Integer.valueOf(recStr.substring(20, 24), 16).toString();
        DNOx_Effi = Integer.valueOf(recStr.substring(24, 26), 16).toString();
        TR21 = Integer.valueOf(recStr.substring(26, 28), 16).toString();
        vehicledInfo.setDifferential_perssure_value(differential_perssure_value);
        vehicledInfo.setNOx_F(NOx_F);
        vehicledInfo.setNOx_R(NOx_R);
        vehicledInfo.setDNOx_Effi(DNOx_Effi);
        vehicledInfo.setTR21(TR21);
    }

    private void dealWithCMD16() {
        L19 = Integer.valueOf(recStr.substring(14, 16), 16).toString();
        TC90 = Integer.valueOf(recStr.substring(16, 20), 16).toString();
        TC92 = Integer.valueOf(recStr.substring(20, 24), 16).toString();
        freq_pump = Integer.valueOf(recStr.substring(24, 26), 16).toString();

        vehicledInfo.setL19("" + Const.DECIMAL_FORMAT.format(Math.random() * 100));

//        vehicledInfo.setL19(L19);
        vehicledInfo.setTC90(TC90);
        vehicledInfo.setTC92(TC92);
        vehicledInfo.setFreq_pump(freq_pump);
    }

    private void dealWithCMD17() {
//        value = Integer.valueOf(recStr.substring(14, 18), 2).toString();
        value1 = Integer.toBinaryString(Integer.parseInt(recStr.substring(14, 15), 16));
        value2 = Integer.toBinaryString(Integer.parseInt(recStr.substring(15, 16), 16));
        value3 = Integer.toBinaryString(Integer.parseInt(recStr.substring(16, 17), 16));
        value4 = Integer.toBinaryString(Integer.parseInt(recStr.substring(17, 18), 16));

        switch (value1.length()) {
            case 1:
                value1 = "000" + value1;
                break;
            case 2:
                value1 = "00" + value1;
                break;
            case 3:
                value1 = "0" + value1;
                break;
            default:
                break;
        }

        switch (value2.length()) {
            case 1:
                value2 = "000" + value2;
                break;
            case 2:
                value2 = "00" + value2;
                break;
            case 3:
                value2 = "0" + value2;
                break;
            default:
                break;
        }

        switch (value3.length()) {
            case 1:
                value3 = "000" + value3;
                break;
            case 2:
                value3 = "00" + value3;
                break;
            case 3:
                value3 = "0" + value3;
                break;
            default:
                break;
        }


        /*switch (value4.length()) {
            case 1:
                value4 = "000" + value4;
                break;
            case 2:
                value4 = "00" + value4;
                break;
            case 3:
                value4 = "0" + value4;
                break;
            default:
                break;
        }*/


        power = (value1.substring(0, 1).equals("0") ? "正常" : "异常");
        abnormals[0] = power.equals("正常") ? 0 : 1;
        twoway_value_failure = (value1.substring(1, 2).equals("0") ? "正常" : "异常");
        abnormals[1] = twoway_value_failure.equals("正常") ? 0 : 1;
        metering_pump_failure = (value1.substring(2, 3).equals("0") ? "正常" : "异常");
        abnormals[2] = metering_pump_failure.equals("正常") ? 0 : 1;
        nozzle_block_failure = (value1.substring(3, 4).equals("0") ? "正常" : "异常");
        abnormals[3] = nozzle_block_failure.equals("正常") ? 0 : 1;

        LQ8486_failure = (value2.substring(0, 1).equals("0") ? "正常" : "异常");
        abnormals[4] = LQ8486_failure.equals("正常") ? 0 : 1;
        NOx_overproof_failure = (value2.substring(1, 2).equals("0") ? "正常" : "异常");
        abnormals[5] = NOx_overproof_failure.equals("正常") ? 0 : 1;
        NOx_sensor_failure = (value2.substring(2, 3).equals("0") ? "正常" : "异常");
        abnormals[6] = NOx_sensor_failure.equals("正常") ? 0 : 1;
        urea_level_failure = (value2.substring(3, 4).equals("0") ? "正常" : "异常");
        abnormals[7] = urea_level_failure.equals("正常") ? 0 : 1;

        urea_quality_failure = (value3.substring(0, 1).equals("0") ? "正常" : "异常");
        abnormals[8] = urea_quality_failure.equals("正常") ? 0 : 1;
        SCR_failure = (value3.substring(1, 2).equals("0") ? "正常" : "异常");
        abnormals[9] = SCR_failure.equals("正常") ? 0 : 1;
        dePM_catalyzer_failure = (value3.substring(2, 3).equals("0") ? "正常" : "异常");
        abnormals[10] = dePM_catalyzer_failure.equals("正常") ? 0 : 1;

        vehicledInfo.setPower(power);
        vehicledInfo.setTwoway_value_failure(twoway_value_failure);
        vehicledInfo.setMetering_pump_failure(metering_pump_failure);
        vehicledInfo.setNozzle_block_failure(nozzle_block_failure);
        vehicledInfo.setLQ8486_failure(LQ8486_failure);
        vehicledInfo.setNOx_overproof_failure(NOx_overproof_failure);
        vehicledInfo.setNOx_sensor_failure(NOx_sensor_failure);
        vehicledInfo.setUrea_level_failure(urea_level_failure);
        vehicledInfo.setUrea_quality_failure(urea_quality_failure);
        vehicledInfo.setSCR_failure(SCR_failure);
        vehicledInfo.setDePM_catalyzer_failure(dePM_catalyzer_failure);

        if (power == "异常" || twoway_value_failure == "异常" || metering_pump_failure == "异常" || nozzle_block_failure == "异常"
                || LQ8486_failure == "异常" || NOx_overproof_failure == "异常" || NOx_sensor_failure == "异常" || urea_level_failure == "异常"
                || urea_quality_failure == "异常" || SCR_failure == "异常" || dePM_catalyzer_failure == "异常") {
            vehicledInfo.setStatus("1");
        } else vehicledInfo.setStatus("2");
    }

    private void dealWithCMD18() {

    }

    private void dealWithCMD19() {
        /*year = Integer.valueOf(recStr.substring(14, 16), 16).toString();
        month = Integer.valueOf(recStr.substring(16, 18), 16).toString();
        day = Integer.valueOf(recStr.substring(18, 20), 16).toString();
        hour = Integer.valueOf(recStr.substring(20, 22), 16).toString();
        minute = Integer.valueOf(recStr.substring(22, 24), 16).toString();
        second = Integer.valueOf(recStr.substring(24, 26), 16).toString();
      *//*  vehicledInfo.setTime("20" + year + "/" + (month.length() == 1 ? "0" + month : month) + "/"
                + (day.length() == 1 ? "0" + day : day) + " "
                + (hour.length() == 1 ? "0" + hour : hour) + ":"
                + (minute.length() == 1 ? "0" + minute : minute) + ":"
                + (second.length() == 1 ? "0" + second : second));*//*
        Date now = new Date();
        Log.e(TAG, "now = " + Const.SDF.format(now));
        vehicledInfo.setTime(Const.SDF.format(now));
        EventBus.getDefault().post(vehicledInfo);
//        dao.add(vehicledInfo);*/

        if (!vehicledInfo.getCar_number().equals(Const.DEFAULT_CAR_NUMBER)) {
            Date now = new Date();
            vehicledInfo.setTime(Const.SDF.format(now));

            Intent intent = new Intent();
            intent.setAction(Const.ACTION1);
            Bundle bundle = new Bundle();
            bundle.putSerializable("broadcast", vehicledInfo);
            intent.putExtras(bundle);
            sendBroadcast(intent);
            vdao.add(vehicledInfo);

            //异常通知
            //dosomething...
            for (int i = 0; i < Const.ABNORMALNUMBER; i++) {
                if (abnormals[i] == 1) {
                    Conversations.Conversion conversion = Conversations.getUnreadConversation(vehicledInfo.getCar_number(), abnormals);
//                    sendNotifications(vehicledInfo, conversion);
                    break;
                }
            }

            vehicledInfo.cleanData();

        }
    }

    private void disconnectSocket() {
        try {
            os.close();
            is.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class BackgroundServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch (action) {
                case Const.ACTION2:
                    Log.e(TAG, "收到广播ACTION2");
                    state = intent.getBooleanExtra("state", false);
                    break;

                case Const.ACTION3:         //TODO //TODO 开启updateThread线程
                    Log.e(TAG, "收到广播ACTION3");
                    if (simplexThread.isAlive()) {
                        Log.e(TAG, "simplexThread is alive1");
                        simplexThread.interrupt();
                        state = false;
                    }

                    if (simplexThread.isAlive()) {
                        Log.e(TAG, "simplexThread is alive2");
                    }

                    if (!updateThread.isAlive()) {
                        Log.e(TAG, "updateThread isn't alive");
                        state = true;
                        updateThread.start();
                    } else {
                        Log.e(TAG, "updateThread is alive");
                    }
                    break;

                case Const.ACTION5:         //TODO 开启simplexThread线程
                    if (updateThread.isAlive()) {
                        updateThread.interrupt();
                        running = false;
                    }
                    if (simplexThread == null || !simplexThread.isAlive()) {
//                        simplexThread = null;
                        simplexThread = new SimplexThread();
                        state = true;
                        simplexThread.start();
                    }
                    break;
                case Const.ACTION6:
                    Log.e(TAG, "收到ACTION6");
                    state = false;
                    running = false;
                    stopSelf();
                    break;
                case Const.ACTION7:
                    String license = intent.getStringExtra("license");
                    String color = intent.getStringExtra("color");
                    if (licensePlateSettingThread == null || (licensePlateSettingThread != null && !licensePlateSettingThread.isAlive())) {
                        licensePlateSettingThread = new LicensePlateSettingThread(license, color);
                        licensePlateSettingThread.start();
                    }
                    break;
                case Const.ACTION9:
                    String ip1 = intent.getStringExtra("ip1");
                    String ip2 = intent.getStringExtra("ip2");
                    String ip3 = intent.getStringExtra("ip3");
                    String ip4 = intent.getStringExtra("ip4");
                    String port = intent.getStringExtra("port");
                    if (ipSettingThread == null || (ipSettingThread != null && !ipSettingThread.isAlive())) {
                        ipSettingThread = new IPSettingThread(ip1, ip2, ip3, ip4, port);
                        ipSettingThread.start();
                    }
                    break;

            }

        }
    }

    public class SimplexThread extends Thread {

        @Override
        public void run() {
            Log.e(TAG, "SimplexThread线程启动");
            while (state && !isInterrupted()) {
                if (recvMessage() != null) {
                    switch (recStr.substring(0, 14)) {
                        case Const.CMD13:
                            new DealWithCMD13Thread().start();
//                            dealWithCMD13();
                            break;
                        case Const.CMD14:
                            new DealWithCMD14Thread().start();
//                            dealWithCMD14();
                            break;
                        case Const.CMD15:
                            new DealWithCMD15Thread().start();
//                            dealWithCMD15();
                            break;
                        case Const.CMD16:
                            new DealWithCMD16Thread().start();
//                            dealWithCMD16();
                            break;
                        case Const.CMD17:
                            new DealWithCMD17Thread().start();
//                            dealWithCMD17();
                            break;
                        case Const.CMD18:
                            new DealWithCMD18Thread().start();
//                            dealWithCMD18();
                            break;
                        case Const.CMD19:
                            new DealWithCMD19Thread().start();
//                            dealWithCMD19();
                            break;
                        case Const.CMD28:

                    }
                }

             /*   if (recStr.startsWith(Const.CMD13)) {
                    dealWithCMD13();
                } else if (recStr.startsWith(Const.CMD14)) {
                    dealWithCMD14();
                } else if (recStr.startsWith(Const.CMD15)) {
                    dealWithCMD15();
                } else if (recStr.startsWith(Const.CMD16)) {
                    dealWithCMD16();
                } else if (recStr.startsWith(Const.CMD17)) {
                    dealWithCMD17();
                } else if (recStr.startsWith(Const.CMD18)) {
                    dealWithCMD18();
                } else if (recStr.startsWith(Const.CMD19)) {
                    dealWithCMD19();
                }*/
//                }
            }


            Log.e(TAG, "SimplexThread线程即将关闭");
//            }

            /*timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    *//*vehicledInfo.setL19(++i + "");
                    vehicledInfo.setTime(Const.SDF.format(new Date()));
                    Log.e(TAG, vehicledInfo.toString());
                    dao.add(vehicledInfo);

                    Intent intent = new Intent();
                    intent.setAction(Const.ACTION1);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("broadcast", vehicledInfo);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);*//*
                    NotificationUtil.newInstance(CommunicationService.this).showNotification(++i);
                }
            }, 0, 5000);*/

        }
    }

    public class UpdateThread extends Thread {

        //        public String TAG = "UpdateThread";
        public String recStr;
        public boolean breakwhile = true;

        public int iState = 1;
        public String FILE_NAME = Environment.getExternalStorageDirectory() + File.separator + Const.FILENAME;
        public Context context;
        public String lineCount;
        public String lengthStr;

        public int s1_length;
        public String[] str_data;
        public int progress = 0;
        public int cnt = 0;

        public UpdateThread() {
            init();
        }


        public void init() {

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
            Log.e(TAG, "UpdateThread线程启动 ");
            try {
                while (running) {
                    Log.e(TAG, "iState = " + iState);
                    switch (iState) {
                        //TODO 查询版本号
                        case 0:
                            os.write(Const.CMD26.getBytes());
                            Log.e(TAG, "send: CMD26 " + Const.CMD26);
//                            sendBroadcastMsg(Const.CMD26, 1);
//                            sendEventMessage(Const.CMD26, 0);
                            sendHandlerMessage(Const.CMD26, 0);
                            if ((recStr = recvMessage()) != null && recStr.startsWith(Const.CMD27)) {
                                Log.e(TAG, "recv: CMD27 " + Const.CMD27);
//                                sendEventMessage(Const.CMD27, 1);
                                sendHandlerMessage(Const.CMD27, 1);
                                String version = Const.DECIMAL_FORMAT.format(Double.valueOf(Integer.parseInt(recStr.substring(12, 14), 16) + "." + Integer.parseInt(recStr.substring(14, 16), 16)));
                                Log.e(TAG, "version = " + version);
                                iState = 1;
                            }
                            break;
                        //TODO  请求更新程序
                        case 1:
                            os.write(Const.CMD1.getBytes());
                            Log.e(TAG, "send: CMD1  " + Const.CMD1);
//                            sendBroadcastMsg(Const.CMD1, 1);
                            sendHandlerMessage(Const.CMD1, 0);
                            if ((recStr = recvMessage()) != null && recStr.startsWith(Const.CMD9)) {
                                Log.e(TAG, "recv: CMD9  " + Const.CMD9);
                                sendHandlerMessage(Const.CMD9, 1);
                                iState = 2;
                            }
                            break;
                        // TODO 发送擦除指令
                        case 2:
                            os.write((Const.CMD2 + lineCount + "FFFFFF*").getBytes());
//                            sendBroadcastMsg(Const.CMD2 + lineCount + "FFFFFF*", 1);
                            sendHandlerMessage(Const.CMD2 + lineCount + "FFFFFF*", 0);
                            if ((recStr = recvMessage()) != null && recStr.startsWith(Const.CMD12)) {
                                sendHandlerMessage(Const.CMD12, 1);
                                iState = 3;
                            }
                            break;
                        // TODO 发送文件
                        case 3:
                            sendFile(FILE_NAME);
                            break;
                        // TODO 发送文件结束 终止线程
                        case 4:
                            os.write(Const.CMD10.getBytes());
//                            sendBroadcastMsg(Const.CMD10, 1);
                            sendHandlerMessage(Const.CMD10, 0);
                            if ((recStr = recvMessage()) != null && recStr.startsWith(Const.CMD12)) {
                                sendHandlerMessage(Const.CMD12, 1);
//                                sendIMessage("--------重启设备--------", 2);
//                                sendBroadcastMsg("--------重启设备--------", 2);
                                iState = 5;
                            }
                            break;
                        case 5:
//                            sendIMessage("--------线程即将关闭--------", 2);
//                            sendIMessage("--------升级完成--------", 4);
//                            sendBroadcastMsg("--------线程即将关闭--------", 2);
//                            sendBroadcastMsg("--------升级完成--------", 2
                            sendHandlerMessage(3, 100);
                            sendHandlerMessage("升级完成", 2);
                            running = false;
                            Intent intent = new Intent(Const.ACTION5);
                            sendBroadcast(intent);
                            break;
                        default:
                            iState = 1;
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendBroadcastMsg(String str, int i) {

            switch (i) {
                case 0:
                    str = "recv: " + str;
                    break;
                case 1:
                    str = "send: " + str;
                    break;
                case 2:
                    str = "tips:" + str;
                    break;
            }
            Intent intent = new Intent();
            intent.setAction(Const.ACTION4);
            intent.putExtra("upgrade", 1);
            intent.putExtra("msg", str);
            sendBroadcast(intent);
        }

        private void sendBroadcastMsg(int i, int j) {
            Intent intent = new Intent();
            intent.setAction(Const.ACTION4);
            intent.putExtra("upgrade", j);
            switch (j) {
                case 2:
                    intent.putExtra("max", i);
                    break;
                case 3:
                    intent.putExtra("progress", i);
                    break;
            }
            sendBroadcast(intent);
        }

        private String getFileLineCount(String filename) {
//            int cnt = 0;
            LineNumberReader lineNumberReader = null;
            try {
                lineNumberReader = new LineNumberReader(new FileReader(filename));
                while (lineNumberReader.readLine() != null) {
                }
                cnt = lineNumberReader.getLineNumber();
//                sendIMessage(cnt, 2);

//                sendBroadcastMsg(cnt, 2);
//                sendEventMessage(new MsgEvent2(cnt));
//                sendHandlerMessage(2, cnt);
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
//            sendHandlerMessage(2, cnt);
            if (socket == null) {
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
                            Log.e(TAG, "row = " + row);
                            row++;
                            s1_length = Integer.valueOf(str.substring(2, 4), 16) - 3;
                            lengthStr = Integer.toHexString(s1_length).length() == 1 ? "0" + Integer.toHexString(s1_length) : Integer.toHexString(s1_length);
                            int mod = s1_length % 8;
                            if (mod == 0) {
                                str_data = new String[s1_length / 8 + 3];
                            } else {
                                str_data = new String[s1_length / 8 + 4];
                            }

                            str_data[0] = Const.CMD3 + "08" + str.substring(4, 8) + lengthStr + str.substring(str.length() - 2, str.length()) + "FFFFFFFF*";
                            for (int i = 1, j = 0; i < str_data.length - 2; i++, j++) {
                                if (i == str_data.length - 3 && mod != 0) {
                                    str_data[i] = Const.CMD4 + "0" + mod + str.substring(8 * (2 * (i - 1) + 1), str.length() - 2) + multiFF(mod) + "*";
                                } else {
                                    str_data[i] = Const.CMD4 + "08" + str.substring(8 * (i + j), 8 * (i + j + 2)) + "*";
                                }
                            }
//                            str_data[0] = Const.CMD3 + "08" + str.substring(4, 8) + str.substring(2, 4) + str.substring(str.length() - 2, str.length()) + "FFFFFFFF*";
                            Log.e(TAG, "str.substring(4, 8)  = " + str.substring(4, 8));
                            Log.e(TAG, "str.substring(2, 4)  = " + str.substring(2, 4));
                            Log.e(TAG, "str.substring(str.length() - 2, str.length() )  = " + str.substring(str.length() - 2, str.length()));
                            str_data[str_data.length - 2] = Const.CMD5;
                            str_data[str_data.length - 1] = Const.CMD6;

                            for (int i = 0; i < str_data.length; i++) {
                                while (breakwhile) {
                                    os.write(str_data[i].getBytes());
                                    Log.e(TAG, String.format("str_data[%d] = %s ", i, str_data[i]));
//                                    sendBroadcastMsg(str_data[i], 1);
                                    sendHandlerMessage(str_data[i], 0);
                                    recStr = recvMessage();
                                    if (recStr != null && recStr.equals(Const.CMD12)) {
                                        sendHandlerMessage(Const.CMD12, 1);
                                        breakwhile = false;
                                    } else if (recStr != null && recStr.equals(Const.CMD11)) {
                                        sendHandlerMessage(Const.CMD11, 1);
                                        breakwhile = true;
                                    }
                                }
                                breakwhile = true;
                            }
                            progress++;
//                            sendBroadcastMsg(progress, 3);
//                            sendEventMessage(new MsgEvent3(progress));
                            sendHandlerMessage(3, (int) (100 * progress / cnt));
                            NotificationUtil.newInstance(CommunicationService.this).showNotification((int) (100 * progress / cnt));
                        } else if (str.endsWith("S9")) break;
                    }
//                    sendBroadcastMsg("文件发送完成", 2);
                    sendHandlerMessage("文件发送完成", 2);
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

    public class LicensePlateSettingThread extends Thread {

        private boolean iLoop = true;
        private String str;
        private String license_plate;
        private String color;
        private String[] CMDNumber = new String[2];
        private String[] CMDColor = new String[2];
        private int bundState = 0;

        public LicensePlateSettingThread(String license_plate, String color) {
            this.license_plate = license_plate;
            this.color = color;
        }

        private void initNumberCMD() {
            String sendCMDNumber = Const.CMD20;
            String recvCMDNumber = Const.CMD23;
            switch (license_plate.substring(0, 1)) {
                case "鄂":
                    sendCMDNumber += "01";
                    break;
                case "鲁":
                    sendCMDNumber += "02";
                    break;
                case "豫":
                    sendCMDNumber += "05";
                    break;
                default:
                    sendCMDNumber += "00";
                    break;
            }
            sendCMDNumber = sendCMDNumber
                    + Util.strToHexAscii(license_plate.substring(1, 2)).toUpperCase()
                    + Util.strToHexAscii(license_plate.substring(2, 3)).toUpperCase()
                    + Util.strToHexAscii(license_plate.substring(3, 4)).toUpperCase()
                    + Util.strToHexAscii(license_plate.substring(4, 5)).toUpperCase()
                    + Util.strToHexAscii(license_plate.substring(5, 6)).toUpperCase()
                    + Util.strToHexAscii(license_plate.substring(6, 7)).toUpperCase() + "*";

            recvCMDNumber += sendCMDNumber.substring(14);

            CMDNumber[0] = sendCMDNumber;
            CMDNumber[1] = recvCMDNumber;
            Log.e(TAG, "CMDNumber[0] = " + CMDNumber[0]);
            Log.e(TAG, "CMDNumber[1] = " + CMDNumber[1]);
        }

        private void initColorCMD() {
            String sendCMDColor = Const.CMD21;
            String recvCMDColor = Const.CMD24;
            sendCMDColor += (color.equals("蓝色") ? "00" : "01") + Util.multiFF(2) + "*";
            recvCMDColor += sendCMDColor.substring(14);

            Log.e(TAG, "sendCMDColor = " + sendCMDColor);
            Log.e(TAG, "recvCMDColor = " + recvCMDColor);

            CMDColor[0] = sendCMDColor;
            CMDColor[1] = recvCMDColor;
        }

        @Override
        public void run() {

            initNumberCMD();
            initColorCMD();
            try {
                while (iLoop) {
                    switch (bundState) {
                        case 0:
                            os.write(CMDNumber[0].getBytes());
                            Log.e(TAG, "发送绑定车牌号命令:" + CMDNumber[0]);
                            if ((str = recvMessage()) != null && str.equals(CMDNumber[1]))
                                bundState = 1;

                            break;
                        case 1:
                            os.write(CMDColor[0].getBytes());
                            Log.e(TAG, "发送绑定车牌颜色命令:" + CMDColor[0]);
                            if ((str = recvMessage()) != null && str.equals(CMDColor[1])) {
                                Intent intent = new Intent(Const.ACTION8);
                                sendBroadcast(intent);
                                iLoop = false;
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class IPSettingThread extends Thread {
        private String ip1;
        private String ip2;
        private String ip3;
        private String ip4;
        private String port;
        private String[] CMDIP = new String[2];
        private boolean iLoop = true;
        private String str;

        public IPSettingThread(String ip1, String ip2, String ip3, String ip4, String port) {
            this.ip1 = ip1;
            this.ip2 = ip2;
            this.ip3 = ip3;
            this.ip4 = ip4;
            this.port = port;
        }

        private void initIPCMD() {
            String sendCMDIP = Const.CMD22;
            String recvCMDIP = Const.CMD25;

            String s1 = Integer.toHexString(Integer.valueOf(ip1)).toUpperCase();
            String s2 = Integer.toHexString(Integer.valueOf(ip2)).toUpperCase();
            String s3 = Integer.toHexString(Integer.valueOf(ip3)).toUpperCase();
            String s4 = Integer.toHexString(Integer.valueOf(ip4)).toUpperCase();
            String s5 = Integer.toHexString(Integer.valueOf(port)).toUpperCase();

            switch (s1.length()) {
                case 1:
                    sendCMDIP += "0" + s1;
                    break;
                default:
                    sendCMDIP += s1;
                    break;
            }

            switch (s2.length()) {
                case 1:
                    sendCMDIP += "0" + s2;
                    break;
                default:
                    sendCMDIP += s2;
                    break;
            }

            switch (s3.length()) {
                case 1:
                    sendCMDIP += "0" + s3;
                    break;
                default:
                    sendCMDIP += s3;
                    break;
            }

            switch (s4.length()) {
                case 1:
                    sendCMDIP += "0" + s4;
                    break;
                default:
                    sendCMDIP += s4;
                    break;
            }

            switch (s5.length()) {
                case 1:
                    sendCMDIP += "000" + s5;
                    break;
                case 2:
                    sendCMDIP += "00" + s5;
                    break;
                case 3:
                    sendCMDIP += "0" + s5;
                    break;
                default:
                    sendCMDIP += s5;
                    break;
            }

            sendCMDIP += "05*";

            recvCMDIP += sendCMDIP.substring(14);

            Log.e(TAG, "sendCMDIP = " + sendCMDIP);
            Log.e(TAG, "recvCMDIP = " + recvCMDIP);

            CMDIP[0] = sendCMDIP;
            CMDIP[1] = recvCMDIP;
        }

        @Override
        public void run() {
            initIPCMD();
            while (iLoop) {
                try {
                    os.write(CMDIP[0].getBytes());
                    if ((str = recvMessage()) != null && str.equals(CMDIP[1])) {
                        Intent intent = new Intent(Const.ACTION10);
                        sendBroadcast(intent);
                        iLoop = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class DealWithCMD13Thread extends Thread {
        @Override
        public void run() {
            dealWithCMD13();
        }
    }

    public class DealWithCMD14Thread extends Thread {
        @Override
        public void run() {
            dealWithCMD14();
        }
    }

    public class DealWithCMD15Thread extends Thread {
        @Override
        public void run() {
            dealWithCMD15();
        }
    }

    public class DealWithCMD16Thread extends Thread {
        @Override
        public void run() {
            dealWithCMD16();
        }
    }

    public class DealWithCMD17Thread extends Thread {
        @Override
        public void run() {
            dealWithCMD17();
        }
    }

    public class DealWithCMD18Thread extends Thread {
        @Override
        public void run() {
            dealWithCMD18();
        }
    }

    public class DealWithCMD19Thread extends Thread {
        @Override
        public void run() {
            dealWithCMD19();
        }
    }

    public void sendEventMessage(String str, int i) {
        switch (i) {
            case 0:
                str = "send :  " + str;
                break;
            case 1:
                str = "recv :  " + str;
                break;
            case 2:
                str = "tips :  " + str;
                break;
        }
        EventBus.getDefault().post(str);
    }

    public void sendEventMessage(MsgEvent2 event) {
        EventBus.getDefault().post(event.getMax());
    }

    public void sendEventMessage(MsgEvent3 event) {
        EventBus.getDefault().post(event.getProgress());
    }

    public void sendHandlerMessage(String str, int i) {
        Message msg = Message.obtain();
        msg.what = 1;
        switch (i) {
            case 0:
                str = "send : " + str;
                break;
            case 1:
                str = "recv : " + str;
                break;
            case 2:
                str = "tips : " + str;
                break;
        }
        msg.obj = str;
        UpgradeFragment.newInstance().getHandler().sendMessage(msg);
    }

    public void sendHandlerMessage(int i, int j) {
        Message msg = Message.obtain();
        switch (i) {
            case 2:
                msg.what = 2;
                break;
            case 3:
                msg.what = 3;
                break;
        }
        msg.obj = j;
        UpgradeFragment.newInstance().getHandler().sendMessage(msg);
    }

    private Intent getMessageReadIntent(int id) {
        return new Intent(READ_ACTION)
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .putExtra(CONVERSATION_ID, id);
    }

    private void sendNotifications(VehicledInfo vehicledInfo, Conversations.Conversion conversation) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                conversation.getConversation_id(),
                getMessageReadIntent(conversation.getConversation_id()),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.CarExtender.UnreadConversation.Builder unreadConvBuilder = new NotificationCompat.CarExtender.UnreadConversation.Builder("")
                .setLatestTimestamp(conversation.getTimestamp())
                .setReadPendingIntent(pendingIntent);

        StringBuilder messageForNotification = new StringBuilder();
        for (Iterator<String> messages = conversation.getMessages().iterator(); messages.hasNext(); ) {
            String message = messages.next();
            unreadConvBuilder.addMessage(message);
            messageForNotification.append(message);
            if (messages.hasNext()) {
                messageForNotification.append(EOL);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_stat_action_report_problem)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getApplicationContext().getResources(), R.mipmap.ic_stat_action_report_problem))
                .setContentText(messageForNotification.toString())
                .setWhen(conversation.getTimestamp())
                .setContentTitle(vehicledInfo.getCar_number())
                .setTicker(vehicledInfo.getCar_number() + "出现故障")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .extend(new NotificationCompat.CarExtender()
                        .setUnreadConversation(unreadConvBuilder.build())
                        .setColor(getApplicationContext()
                                .getResources().getColor(R.color.colorPrimary)));

       /* MessageLogger.logMessage(getApplicationContext(), "Sending notification "
                + conversation.getConversationId() + " conversation: " + conversation);*/
        String[] datetime = vehicledInfo.getTime().split(" ");
        adao.add(new AbnormalInfo(conversation.getTitle(), conversation.getMessagesString(), datetime[0], datetime[1]));
        mNotificationManager.notify(conversation.getConversation_id(), builder.build());
    }

}
