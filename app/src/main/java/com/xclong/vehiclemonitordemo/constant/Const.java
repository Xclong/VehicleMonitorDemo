package com.xclong.vehiclemonitordemo.constant;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/7/28.
 */
public class Const {

    public final static String CAR_NUMBER = "Car_number";
    public final static String PLATE_COLOR = "Plate_color";
    public final static String AVERAGE_SPEED = "Average_speed";
    public final static String ENGINE_SPEED = "Engine_speed";
    public final static String TOTAL_MILEAGE = "Total_mileage";
    public final static String DIFFERENTIAL_PERSSURE_VALUE = "Differential_perssure_value";
    public final static String NOX_F = "NOx_F";
    public final static String NOX_R = "NOx_R";
    public final static String DNOX_EFFI = "DNOx_Effi";
    public final static String TR21 = "TR21";
    public final static String L19 = "L19";
    public final static String TC90 = "TC90";
    public final static String TC92 = "TC92";
    public final static String FREQ_PUMP = "Freq_pump";
    public final static String POWER = "Power";
    public final static String TWOWAY_VALUE_FAILURE = "Twoway_value_failure";
    public final static String METERING_PUMP_FAILURE = "Metering_pump_failure";
    public final static String NOZZLE_BLOCK_FAILURE = "Nozzle_block_failure";
    public final static String LQ8486_FAILURE = "LQ8486_failure";
    public final static String NOX_OVERPROOF_FAILURE = "NOx_overproof_failure";
    public final static String NOX_SENSOR_FAILURE = "NOx_sensor_failure";
    public final static String UREA_LEVEL_FAILURE = "Urea_level_failure";
    public final static String UREA_QUALITY_FAILURE = "Urea_quality_failure";
    public final static String SCR_FAILURE = "SCR_failure";
    public final static String DEPM_CATALYZER_FAILURE = "DePM_catalyzer_failure";
    public final static String TIME = "Time";
    public final static String STATUS = "Status";

    public final static String DEFAULT_CAR_NUMBER = "XXXXX";
    public final static String DEFAULT_PLATE_COLOR = "蓝色";
    public final static String DEFAULT_AVERAGE_SPEED = "-1";
    public final static String DEFAULT_ENGINE_SPEED = "-1";
    public final static String DEFAULT_TOTAL_MILEAGE = "-1";
    public final static String DEFAULT_DIFFERENTIAL_PERSSURE_VALUE = "-1";
    public final static String DEFAULT_NOX_F = "-1";
    public final static String DEFAULT_NOX_R = "-1";
    public final static String DEFAULT_DNOX_EFFI = "-1";
    public final static String DEFAULT_TR21 = "-1";
    public final static String DEFAULT_L19 = "-1";
    public final static String DEFAULT_TC90 = "-1";
    public final static String DEFAULT_TC92 = "-1";
    public final static String DEFAULT_FREQ_PUMP = "-1";
    public final static String DEFAULT_POWER = "未知";
    public final static String DEFAULT_TWOWAY_VALUE_FAILURE = "未知";
    public final static String DEFAULT_METERING_PUMP_FAILURE = "未知";
    public final static String DEFAULT_NOZZLE_BLOCK_FAILURE = "未知";
    public final static String DEFAULT_LQ8486_FAILURE = "未知";
    public final static String DEFAULT_NOX_OVERPROOF_FAILURE = "未知";
    public final static String DEFAULT_NOX_SENSOR_FAILURE = "未知";
    public final static String DEFAULT_UREA_LEVEL_FAILURE = "未知";
    public final static String DEFAULT_UREA_QUALITY_FAILURE = "未知";
    public final static String DEFAULT_SCR_FAILURE = "未知";
    public final static String DEFAULT_DEPM_CATALYZER_FAILURE = "未知";
    public final static String DEFAULT_TIME = "2015/0101 00:00:00";
    public final static String DEFAULT_STATUS = "2";


    public final static int UNKNOWNCOLOR = 0;
    public final static int YELLOWNUM = 1;
    public final static int BLUENUM = 2;

    public final static String AB_DATE = "date";
    public final static String AB_TIME = "time";
    public final static String PLATE_NUMBER = "plate_number";

    public final static String PACKAGE_NAME = "com.xclong.vehiclemonitordemo";
    public final static String VERSION_KEY = "version_key";

    public final static int ABNORMALNUMBER = 11;

    public final static String BLUETOOTH_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public final static String FILENAME = "Project.abs.s19";
    public final static String FILENAME1 = "Project1.abs.s19";

    public final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public final static SimpleDateFormat DATESDF = new SimpleDateFormat("yyyy/MM/dd");
    public final static SimpleDateFormat TIMESDF = new SimpleDateFormat("HH:mm:ss");

    public final static String LOGININFO = "LoginInfo";
    public final static String LOGINNAME = "LoginName";
    public final static String LOGINSTATE = "LoginState";
    public final static String ADMINLOGIN = "AdminLogin";
    public final static String NORMALLOGIN = "NormalLogin";
    public final static String NOTLOGIN = "NotLogin";

    public final static String LOGINURL_POST = "http://172.16.7.4:8080/jeeplus/a/login?__ajax";
    public final static String LOGINURL_GET = "http://localhost:8080/jeeplus/a/sys/user/infoData?__ajax=true&mobileLogin=true";

    public final static int CMD_LENGTH = 29;
    public final static String CMD1 = "$CMD00172008AAAAFFFFFFFFFFFF*";     //TODO :    请求更新程序指令
    public final static String CMD2 = "$CMD01172008190049";                            //TODO :    擦除指令
    public final static String CMD3 = "$CMD021720";                                             //TODO :    发送文件（高低地址，长度，校验和）
    public final static String CMD4 = "$CMD031720";                                             //TODO :    发送文件 (S1 Code)
    public final static String CMD5 = "$CMD04172008FFFFFFFFFFFFFFFF*";       //TODO :    比较检验和
    public final static String CMD6 = "$CMD05172008FFFFFFFFFFFFFFFF*";       //TODO :    Code 写入Flash
    public final static String CMD7 = "$CMD06172008";                                         //TODO :    写入代码版本号
    public final static String CMD8 = "$CMD07201708";                                         //TODO :    CDMA程序版本号
    public final static String CMD9 = "$CMD082017085555FFFFFFFFFFFF*";      //TODO :    发送CMD1后返回的正确指令
    public final static String CMD10 = "$CMD09172008FFFFFFFFFFFFFFFF*";     //TODO :    发送文件完成后发送的指令
    public final static String CMD11 = "$CMD0B201708FFFFFFFFFFFFFFFF*";     //TODO :    返回的错误指令
    public final static String CMD12 = "$CMD0C201708FFFFFFFFFFFFFFFF*";     //TODO :    返回的正确指令

    public final static String CMD13 = "$CMD0F20170801";                                  //TODO :    省份、区号、车牌号（1,1,5）
    public final static String CMD14 = "$CMD0F20170802";                                  //TODO :    车牌颜色、车速、发动机转速、里程（1,1,2,3）
    public final static String CMD15 = "$CMD0F20170803";                                  //TODO :    差压值、NOx_F、NOx_R、DNOx_Effi、TR21（1,2,2,1,1）
    public final static String CMD16 = "$CMD0F20170804";                                  //TODO :    L19、TC90、TC92、Freq_pump   (1,2,2,1)
    public final static String CMD17 = "$CMD0F20170805";                                  //TODO :    故障1,、故障2、经度度、经度分、经度秒（1,1,1,1,2）
    public final static String CMD18 = "$CMD0F20170806";                                  //TODO :    纬度度、纬度分、纬度秒（1,1,2）
    public final static String CMD19 = "$CMD0F20170807";                                  //TODO :    年、月、日、时、分、秒（1,1,1,1,1,1）

    public final static String CMD20 = "$CMD0D17200801";                                  //TODO :    省份、区号、车牌号（1,1,5）
    public final static String CMD21 = "$CMD0D17200802";                                  //TODO :    车牌颜色（1） 黄牌01、蓝牌00
    public final static String CMD22 = "$CMD0D17200803";                                  //TODO :    IP、端口、上传服务器时间（4,2,1）

    public final static String CMD23 = "$CMD0D20170801";                                  //TODO :    省份、区号、车牌号（1,1,5）
    public final static String CMD24 = "$CMD0D20170802";                                  //TODO :    车牌颜色（1）黄牌01、蓝牌00
    public final static String CMD25 = "$CMD0D20170803";                                  //TODO :    IP、端口、上传服务器时间（4,2,1）

    public final static String CMD26 = "$CMD0E172008FFFFFFFFFFFFFFFF*";      //TODO :    查询版本号
    public final static String CMD27 = "$CMD0E201708";                                       //TODO :    返回版本号（1,1）

    public final static String CMD28 = "$CMDF00E5108";                                                //TODO :   前氮氧化物
    public final static String CMD29 = "$CMDF00F5208";                                                //TODO :   后氮氧化物

    public static final String TAG_FAILURE = "连接失败";
    public static final String TAG_CONNECTFAILURE = "网络连接失败";

    public static final String ACTION1 = "com.xclong.vehiclemonitordemo.BROADCAST1";
    public static final String ACTION2 = "com.xclong.vehiclemonitordemo.BROADCAST2";
    public static final String ACTION3 = "com.xclong.vehiclemonitordemo.BROADCAST3";
    public static final String ACTION4 = "com.xclong.vehiclemonitordemo.BROADCAST4";
    public static final String ACTION5 = "com.xclong.vehiclemonitordemo.BROADCAST5";
    public static final String ACTION6 = "com.xclong.vehiclemonitordemo.BROADCAST6";
    public static final String ACTION7 = "com.xclong.vehiclemonitordemo.BROADCAST7";   //TODO 绑定车牌号和车牌颜色
    public static final String ACTION8 = "com.xclong.vehiclemonitordemo.BROADCAST8";   //TODO 绑定车牌号和车牌颜色 成功
    public static final String ACTION9 = "com.xclong.vehiclemonitordemo.BROADCAST9";   //TODO 绑定IP
    public static final String ACTION10 = "com.xclong.vehiclemonitordemo.BROADCAST10";   //TODO 绑定IP 成功

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    public static final String SERVICE_NAME = "com.xclong.vehiclemonitordemo.service.CommunicationService";
    public static final String SERVICE_ACTION = "com.xclong.vehiclemonitordemo.COMMUNICATION_SERVICE";

}
