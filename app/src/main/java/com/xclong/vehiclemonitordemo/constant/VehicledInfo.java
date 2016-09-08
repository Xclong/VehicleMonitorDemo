package com.xclong.vehiclemonitordemo.constant;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by xclong on 2016/3/2.
 */
@DatabaseTable(tableName = "tb_car_info")
public class VehicledInfo implements Serializable, Cloneable/*, Comparable<VehicledInfo>*/ {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = Const.CAR_NUMBER, defaultValue = Const.DEFAULT_CAR_NUMBER)
    private String car_number = Const.DEFAULT_CAR_NUMBER;                              //TODO 车牌号
    @DatabaseField(columnName = Const.PLATE_COLOR, defaultValue = "蓝色")
    private String plate_color = "蓝色";                                 //TODO  车牌颜色
    @DatabaseField(columnName = Const.AVERAGE_SPEED, defaultValue = "0")
    private String average_speed = "0";                       //TODO  平均转速
    @DatabaseField(columnName = Const.ENGINE_SPEED, defaultValue = "0")
    private String engine_speed = "0";                        //TODO  发动机转速
    @DatabaseField(columnName = Const.TOTAL_MILEAGE, defaultValue = "0")
    private String total_mileage = "0";                       //TODO  总里程
    @DatabaseField(columnName = Const.DIFFERENTIAL_PERSSURE_VALUE, defaultValue = "0")
    private String differential_perssure_value = "0";     //TODO 差压值
    @DatabaseField(columnName = Const.NOX_F, defaultValue = "0")
    private String NOx_F = "0";
    @DatabaseField(columnName = Const.NOX_R, defaultValue = "0")
    private String NOx_R = "0";
    @DatabaseField(columnName = Const.DNOX_EFFI, defaultValue = "0")
    private String DNOx_Effi = "0";
    @DatabaseField(columnName = Const.TR21, defaultValue = "0")
    private String TR21 = "0";
    @DatabaseField(columnName = Const.L19, defaultValue = "0")
    private String L19 = "0";
    @DatabaseField(columnName = Const.TC90, defaultValue = "0")
    private String TC90 = "0";
    @DatabaseField(columnName = Const.TC92, defaultValue = "0")
    private String TC92 = "0";
    @DatabaseField(columnName = Const.FREQ_PUMP, defaultValue = "0")
    private String freq_pump = "0";
    @DatabaseField(columnName = Const.POWER, defaultValue = "正常")
    private String power = "正常";
    @DatabaseField(columnName = Const.TWOWAY_VALUE_FAILURE, defaultValue = "正常")
    private String twoway_value_failure = "正常";                                          //TODO 两通阀故障
    @DatabaseField(columnName = Const.METERING_PUMP_FAILURE, defaultValue = "正常")
    private String metering_pump_failure = "正常";                                       //TODO 计量泵故障
    @DatabaseField(columnName = Const.NOZZLE_BLOCK_FAILURE, defaultValue = "正常")
    private String nozzle_block_failure = "正常";                                            //TODO 喷嘴堵故障
    @DatabaseField(columnName = Const.LQ8486_FAILURE, defaultValue = "正常")
    private String LQ8486_failure = "正常";                                                    //TODO  LQ8486故障
    @DatabaseField(columnName = Const.NOX_OVERPROOF_FAILURE, defaultValue = "正常")
    private String NOx_overproof_failure = "正常";                                       //TODO  NOx超标故障
    @DatabaseField(columnName = Const.NOX_SENSOR_FAILURE, defaultValue = "正常")
    private String NOx_sensor_failure = "正常";                                             //TODO NOx传感器故障
    @DatabaseField(columnName = Const.UREA_LEVEL_FAILURE, defaultValue = "正常")
    private String urea_level_failure = "正常";                                                //TODO 尿素液位故障
    @DatabaseField(columnName = Const.UREA_QUALITY_FAILURE, defaultValue = "正常")
    private String urea_quality_failure = "正常";                                             //TODO 尿素质量故障
    @DatabaseField(columnName = Const.SCR_FAILURE, defaultValue = "正常")
    private String SCR_failure = "正常";                                                           //TODO SCR故障
    @DatabaseField(columnName = Const.DEPM_CATALYZER_FAILURE, defaultValue = "正常")
    private String DePM_catalyzer_failure = "正常";                                         //TODO DePM催化剂故障
    @DatabaseField(columnName = Const.TIME/*,dataType = DataType.DATE_TIME*/, defaultValue = "2016/06/13 10:01")
    private String time = "2016/06/13 10:01";
    @DatabaseField(columnName = Const.STATUS, defaultValue = "2")
    private String status = "2";

    public VehicledInfo() {
        //TODO ARMLite needs a no-arg constructor
    }

    public VehicledInfo(String car_number, String plate_color, String average_speed,
                        String engine_speed, String total_mileage, String differential_perssure_value,
                        String NOx_F, String NOx_R, String DNOx_Effi, String TR21, String L19,
                        String TC90, String TC92, String freq_pump, String power, String twoway_value_failure,
                        String metering_pump_failure, String nozzle_block_failure, String LQ8486_failure,
                        String NOx_overproof_failure, String NOx_sensor_failure, String urea_level_failure,
                        String urea_quality_failure, String SCR_failure, String dePM_catalyzer_failure, String time, String status) {
        this.car_number = car_number;
        this.plate_color = plate_color;
        this.average_speed = average_speed;
        this.engine_speed = engine_speed;
        this.total_mileage = total_mileage;
        this.differential_perssure_value = differential_perssure_value;
        this.NOx_F = NOx_F;
        this.NOx_R = NOx_R;
        this.DNOx_Effi = DNOx_Effi;
        this.TR21 = TR21;
        this.L19 = L19;
        this.TC90 = TC90;
        this.TC92 = TC92;
        this.freq_pump = freq_pump;
        this.power = power;
        this.twoway_value_failure = twoway_value_failure;
        this.metering_pump_failure = metering_pump_failure;
        this.nozzle_block_failure = nozzle_block_failure;
        this.LQ8486_failure = LQ8486_failure;
        this.NOx_overproof_failure = NOx_overproof_failure;
        this.NOx_sensor_failure = NOx_sensor_failure;
        this.urea_level_failure = urea_level_failure;
        this.urea_quality_failure = urea_quality_failure;
        this.SCR_failure = SCR_failure;
        this.DePM_catalyzer_failure = dePM_catalyzer_failure;
        this.time = time;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCar_number() {
        return car_number;
    }

    public String getPlate_color() {
        return plate_color;
    }

    public String getAverage_speed() {
        return average_speed;
    }

    public String getEngine_speed() {
        return engine_speed;
    }

    public String getTotal_mileage() {
        return total_mileage;
    }

    public String getDifferential_perssure_value() {
        return differential_perssure_value;
    }

    public String getNOx_F() {
        return NOx_F;
    }

    public String getNOx_R() {
        return NOx_R;
    }

    public String getDNOx_Effi() {
        return DNOx_Effi;
    }

    public String getTR21() {
        return TR21;
    }

    public String getL19() {
        return L19;
    }

    public String getTC90() {
        return TC90;
    }

    public String getTC92() {
        return TC92;
    }

    public String getFreq_pump() {
        return freq_pump;
    }

    public String getPower() {
        return power;
    }

    public String getTwoway_value_failure() {
        return twoway_value_failure;
    }

    public String getMetering_pump_failure() {
        return metering_pump_failure;
    }

    public String getNozzle_block_failure() {
        return nozzle_block_failure;
    }

    public String getLQ8486_failure() {
        return LQ8486_failure;
    }

    public String getNOx_overproof_failure() {
        return NOx_overproof_failure;
    }

    public String getNOx_sensor_failure() {
        return NOx_sensor_failure;
    }

    public String getUrea_level_failure() {
        return urea_level_failure;
    }

    public String getUrea_quality_failure() {
        return urea_quality_failure;
    }

    public String getSCR_failure() {
        return SCR_failure;
    }

    public String getDePM_catalyzer_failure() {
        return DePM_catalyzer_failure;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public void setPlate_color(String plate_color) {
        this.plate_color = plate_color;
    }

    public void setAverage_speed(String average_speed) {
        this.average_speed = average_speed;
    }

    public void setEngine_speed(String engine_speed) {
        this.engine_speed = engine_speed;
    }

    public void setTotal_mileage(String total_mileage) {
        this.total_mileage = total_mileage;
    }

    public void setDifferential_perssure_value(String differential_perssure_value) {
        this.differential_perssure_value = differential_perssure_value;
    }

    public void setNOx_F(String NOx_F) {
        this.NOx_F = NOx_F;
    }

    public void setNOx_R(String NOx_R) {
        this.NOx_R = NOx_R;
    }

    public void setDNOx_Effi(String DNOx_Effi) {
        this.DNOx_Effi = DNOx_Effi;
    }

    public void setTR21(String TR21) {
        this.TR21 = TR21;
    }

    public void setL19(String l19) {
        L19 = l19;
    }

    public void setTC90(String TC90) {
        this.TC90 = TC90;
    }

    public void setTC92(String TC92) {
        this.TC92 = TC92;
    }

    public void setFreq_pump(String freq_pump) {
        this.freq_pump = freq_pump;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setTwoway_value_failure(String twoway_value_failure) {
        this.twoway_value_failure = twoway_value_failure;
    }

    public void setMetering_pump_failure(String metering_pump_failure) {
        this.metering_pump_failure = metering_pump_failure;
    }

    public void setNozzle_block_failure(String nozzle_block_failure) {
        this.nozzle_block_failure = nozzle_block_failure;
    }

    public void setLQ8486_failure(String LQ8486_failure) {
        this.LQ8486_failure = LQ8486_failure;
    }

    public void setNOx_overproof_failure(String NOx_overproof_failure) {
        this.NOx_overproof_failure = NOx_overproof_failure;
    }

    public void setNOx_sensor_failure(String NOx_sensor_failure) {
        this.NOx_sensor_failure = NOx_sensor_failure;
    }

    public void setUrea_level_failure(String urea_level_failure) {
        this.urea_level_failure = urea_level_failure;
    }

    public void setUrea_quality_failure(String urea_quality_failure) {
        this.urea_quality_failure = urea_quality_failure;
    }

    public void setSCR_failure(String SCR_failure) {
        this.SCR_failure = SCR_failure;
    }

    public void setDePM_catalyzer_failure(String dePM_catalyzer_failure) {
        DePM_catalyzer_failure = dePM_catalyzer_failure;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String get(String column) {
        switch (column) {
            case Const.NOX_F:
                return this.NOx_F;
            case Const.NOX_R:
                return this.NOx_R;
            case Const.DNOX_EFFI:
                return this.DNOx_Effi;
            case Const.L19:
                return this.L19;
            case Const.TR21:
                return this.TR21;
            case Const.TC90:
                return this.TC90;
            case Const.TC92:
                return this.TC92;
            case Const.FREQ_PUMP:
                return this.freq_pump;
        }
        return null;
    }

    /*@Override
    public int compareTo(VehicledInfo another) {
        try {
            Date date1 = Const.SDF.parse(this.getTime());
            Date date2 = Const.SDF.parse(another.getTime());
            if (date1.after(date2)) return 1;
            else return 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }*/

    @Override
    public String toString() {
        return "car_number=" + getCar_number() + "  time = " + getTime();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void cleanData() {
        this.car_number = Const.DEFAULT_CAR_NUMBER;
        this.plate_color = Const.DEFAULT_PLATE_COLOR;
        this.average_speed = Const.DEFAULT_AVERAGE_SPEED;
        this.total_mileage = Const.DEFAULT_TOTAL_MILEAGE;
        this.differential_perssure_value = Const.DEFAULT_DIFFERENTIAL_PERSSURE_VALUE;
        this.NOx_F = Const.DEFAULT_NOX_F;
        this.NOx_R = Const.DEFAULT_NOX_R;
        this.DNOx_Effi = Const.DEFAULT_DNOX_EFFI;
        this.TR21 = Const.DEFAULT_TR21;
        this.L19 = Const.DEFAULT_L19;
        this.TC90 = Const.DEFAULT_TC90;
        this.TC92 = Const.DEFAULT_TC92;
        this.freq_pump = Const.DEFAULT_FREQ_PUMP;
        this.power = Const.DEFAULT_POWER;
        this.twoway_value_failure = Const.DEFAULT_TWOWAY_VALUE_FAILURE;
        this.metering_pump_failure = Const.DEFAULT_METERING_PUMP_FAILURE;
        this.nozzle_block_failure = Const.DEFAULT_NOZZLE_BLOCK_FAILURE;
        this.LQ8486_failure = Const.DEFAULT_LQ8486_FAILURE;
        this.NOx_overproof_failure = Const.DEFAULT_NOX_OVERPROOF_FAILURE;
        this.NOx_sensor_failure = Const.DEFAULT_NOX_SENSOR_FAILURE;
        this.urea_level_failure = Const.DEFAULT_UREA_LEVEL_FAILURE;
        this.urea_quality_failure = Const.DEFAULT_UREA_QUALITY_FAILURE;
        this.SCR_failure = Const.DEFAULT_SCR_FAILURE;
        this.DePM_catalyzer_failure = Const.DEFAULT_DEPM_CATALYZER_FAILURE;
        this.time = Const.DEFAULT_TIME;
        this.status = Const.DEFAULT_STATUS;
    }
}
