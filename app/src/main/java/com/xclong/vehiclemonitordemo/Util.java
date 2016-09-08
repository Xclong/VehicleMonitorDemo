package com.xclong.vehiclemonitordemo;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by xcl02 on 2016/5/24.
 */
public class Util {
    public static String multiFF(int n) {
        String ff = "";
        if (n == 0) return ff;
        else {
            for (int i = 0; i < 8 - n; i++) {
                ff += "FF";
            }
            return ff;
        }
    }

    public static String addZero(String str) {
        String zeroStr = "";
        if (str.length() < 16) {
            for (int i = 0; i < 16 - str.length(); i++) {
                zeroStr += "0";
            }
        }
        return zeroStr + str;
    }

    public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() > 0 ? true : false;
    }

    public static boolean isServiceRunning(Context mContext, String classname) {
        boolean isRunning = false;

        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(1000);

        if (serviceList.size() <= 0) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {

            if (serviceList.get(i).service.getClassName().equals(classname)) {
                isRunning = true;
                break;
            }
        }

        return isRunning;
    }

    public static String charToHexStr(String str) {
        return String.valueOf(str.charAt(0));
    }

    public static String hexToAsciiStr(String str) {
        return String.valueOf((char) (Integer.parseInt(str, 16)));
    }

    public static String strToHexAscii(String str) {
        char[] ch = str.toCharArray();
//        return String.valueOf(     (byte)(ch[0])        );
        return Integer.toHexString(((byte) (ch[0])) & 0xFF);
    }

}
