package com.xclong.vehiclemonitordemo.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.xclong.vehiclemonitordemo.constant.Const;
import com.xclong.vehiclemonitordemo.constant.QueryItem;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by xcl02 on 2016/3/25.
 */
public class VehicledInfoDao {
    private Context context;
    private Dao<VehicledInfo, Integer> vehicledInfoDaoOpe;
    private DatebaseHelper helper;

    public String[] province_num = new String[]{"", "鄂", "鲁"};
    public String[] city_num = new String[]{"", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S"};

    public VehicledInfoDao(Context context) {
        this.context = context;

        try {
            helper = DatebaseHelper.getHelper(context);
            vehicledInfoDaoOpe = helper.getDao(VehicledInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(VehicledInfo vehicledInfo) {
        /*TransactionManager.callInTransaction(helper.getConnectionSource(), new Callable<void>() {
            @Override
            public void call() throws Exception {
                return null;
            }
        });*/

        try {
            int i = vehicledInfoDaoOpe.create(vehicledInfo);
            Log.e("dao", "add " + i);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public VehicledInfo getVehicledInfo(int id) {
        try {

            return vehicledInfoDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VehicledInfo> getAll() {
        try {
            return vehicledInfoDaoOpe.queryBuilder().orderBy(Const.TIME, false).query();
//            return vehicledInfoDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getSize() {
        try {
            return vehicledInfoDaoOpe.queryForAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<VehicledInfo> getTimeBewteen(String car_number, Date startDate, Date endDate) {
        try {
            return vehicledInfoDaoOpe.queryBuilder().orderBy(Const.TIME, true).where().eq(Const.CAR_NUMBER, car_number).and().between(Const.TIME, Const.SDF.format(startDate), Const.SDF.format(endDate)).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VehicledInfo> getSelect(QueryItem queryItem) {
        String selection = "";
        String number = queryItem.getCar_number();
        int province = queryItem.getProvince();
        int city = queryItem.getCity();
        if (number == null) {   //TODO 车牌号未填写
            if (province != 0) {   //TODO 省份填写
                if (city == 0) {     //TODO 城市未填写
                    selection = province_num[province] + "%";
                } else {              //TODO 城市填写
                    selection = province_num[province] + city_num[city] + "%";
                }
            } else {                  //TODO 省份未填写
                return getAll();
            }
        } else {                      //TODO 车牌号填写
            if (province != 0) {    //TODO 省份填写
                if (city == 0) {     //TODO 城市未填写
                    selection = province_num[province] + "%" + number + "%";
                } else {
                    selection = province_num[province] + city_num[city] + "%" + "%";
                }
            } else {
                selection = "%" + number + "%";
            }
        }

        try {
            return vehicledInfoDaoOpe.queryBuilder().orderBy(Const.TIME, false).where().like(Const.CAR_NUMBER, selection).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VehicledInfo> getSelection(String startTime, String endTime) {
        try {
            return vehicledInfoDaoOpe.queryBuilder().where().between(Const.TIME, startTime, endTime).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
