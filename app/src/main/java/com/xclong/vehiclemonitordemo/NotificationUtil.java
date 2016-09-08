package com.xclong.vehiclemonitordemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.xclong.vehiclemonitordemo.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xcl02 on 2016/7/25.
 */
public class NotificationUtil {
    private Context mContext;
    private NotificationManager mNotificationManager = null;
    private Map<Integer, Notification> mNotifications = null;
    private int notification_id ;
    private Notification notification;

    public static NotificationUtil notificationUtil;

    public synchronized static NotificationUtil newInstance(Context mContext) {
        if (notificationUtil == null) {
            notificationUtil = new NotificationUtil(mContext);
        }
        return notificationUtil;
    }

    public NotificationUtil(Context mContext) {
        this.mContext = mContext;
        //TODO 获取通知系统服务
        this.mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        //TODO 创建通知的集合
        this.mNotifications = new HashMap<>();

        notification = new Notification(R.drawable.ic_launcher, "更新设备", System.currentTimeMillis());
        notification.contentView = new RemoteViews(mContext.getPackageName(), R.layout.update_notification);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.contentView.setProgressBar(R.id.pb_notification, 100, 0, false);

        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        notification.contentIntent = pendingIntent;

    }

    public void showNotification(int progress) {
        notification.contentView.setProgressBar(R.id.pb_notification, 100, progress, false);
        mNotificationManager.notify(notification_id, notification);
    }


}
