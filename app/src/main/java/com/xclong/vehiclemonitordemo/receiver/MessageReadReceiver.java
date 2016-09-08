package com.xclong.vehiclemonitordemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Created by xcl02 on 2016/7/27.
 */
public class MessageReadReceiver extends BroadcastReceiver {

    private static final String TAG = MessageReadReceiver.class.getSimpleName();
    private static final String CONVERSATION_ID = "conversation_id";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceiver");
        int conversationId = intent.getIntExtra(CONVERSATION_ID, -1);
        if (conversationId != -1) {
            //TODO 消息已读后的处理
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(conversationId);
        }
    }
}
