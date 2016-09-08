package com.xclong.vehiclemonitordemo.constant;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by xcl02 on 2016/7/29.
 */
@DatabaseTable(tableName = "tb_abnormal_info")
public class AbnormalInfo {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = Const.PLATE_NUMBER)
    private String plate_number;
    @DatabaseField(columnName = "message", defaultValue = "----异常")
    private String message;
    @DatabaseField(columnName = Const.AB_DATE, defaultValue = "----/--/--")
    private String date;
    @DatabaseField(columnName = Const.AB_TIME, defaultValue = " --:--")
    private String time;

    public AbnormalInfo() {
        //TODO ARMLite needs a no-arg constructor
    }

    public AbnormalInfo(String plate_number, String message, String date,String time) {
        this.plate_number = plate_number;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "[ datetime : " + getDate()+" "+getTime()+ " plate_number : " + getPlate_number() + "  message : " + getMessage() + "]";
    }
}
