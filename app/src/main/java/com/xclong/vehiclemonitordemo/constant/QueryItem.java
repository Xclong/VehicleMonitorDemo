package com.xclong.vehiclemonitordemo.constant;

import java.io.Serializable;

/**
 * Created by xcl02 on 2016/5/5.
 */
public class QueryItem implements Serializable {
    private String car_number = null;
    private int province = 0;
    private int city = 0;

    public QueryItem(String car_number, int province, int city) {
        this.car_number = car_number;
        this.province = province;
        this.city = city;
    }

    public QueryItem() {
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getRusult(){
        return getCar_number()+" " +getProvince()+" " +getCity();
    }
}
