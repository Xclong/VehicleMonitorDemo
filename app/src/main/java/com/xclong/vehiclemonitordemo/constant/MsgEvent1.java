package com.xclong.vehiclemonitordemo.constant;

/**
 * Created by xcl02 on 2016/5/24.
 */
public class MsgEvent1 {
    public String message;
    public int tips;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTips(int tips) {
        this.tips = tips;
    }

    public MsgEvent1(String message, int tips) {
        this.message = message;
        this.tips = tips;
    }

    public MsgEvent1() {
    }

    public String getMessage() {
        return message;
    }

    public int getTips() {
        return tips;
    }
}
