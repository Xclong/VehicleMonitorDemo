package com.xclong.vehiclemonitordemo.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xcl02 on 2016/5/10.
 */
public class App extends Application {
    private String TAG = "App";
    private static App sInstance;
    private List<Activity> mList = new LinkedList<Activity>();


//    private PushAgent mPushAgent;


    public App() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        /*mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            //TODO 处理自定义消息
            @Override
            public void dealWithCustomMessage(Context context, UMessage uMessage) {
                super.dealWithCustomMessage(context, uMessage);
            }


            //TODO 处理通知
            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
                Log.e(TAG, "收到通知--自定义处理");
                Intent intent = new Intent();
                intent.setAction("com.xclong.vehiclemonitordemo.MAINACTIVITY");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("itemNumber", 1);
                startActivity(intent);
            }

            //TODO 自定义通知栏样式
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                return super.getNotification(context, uMessage);
            }
        };
        //TODO 自定义消息处理类
        mPushAgent.setMessageHandler(messageHandler);


        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            //TODO 点击通知的自定义行为处理
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                super.dealWithCustomAction(context, uMessage);

            }

            //TODO 点击通知打开应用
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                super.launchApp(context, uMessage);
            }

            //TODO 点击通知打开网页
            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
            }

            //TODO 点击通知打开应用内的指定页面
            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);

            }
        };
        //TODO 自定义通知处理类
        mPushAgent.setNotificationClickHandler(notificationClickHandler);*/


        Log.e(TAG, "app onCreate");
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    Log.e(TAG, activity.getLocalClassName());
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public synchronized static App getInstance() {
//        if (sInstance == null) {
//            sInstance = new App();
//        }
        return sInstance;
    }
}
