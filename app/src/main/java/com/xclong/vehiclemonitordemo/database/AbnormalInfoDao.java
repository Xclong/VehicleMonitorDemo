package com.xclong.vehiclemonitordemo.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.xclong.vehiclemonitordemo.constant.AbnormalInfo;
import com.xclong.vehiclemonitordemo.constant.Const;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xcl02 on 2016/7/29.
 */
public class AbnormalInfoDao {
    private Context context;
    private Dao<AbnormalInfo, Integer> abnormalInfoDaoOpe;
    private DatebaseHelper helper;

    public AbnormalInfoDao(Context context) {
        this.context = context;

        try {
            helper = DatebaseHelper.getHelper(context);
            abnormalInfoDaoOpe = helper.getDao(AbnormalInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(AbnormalInfo abnormalInfo) {
        try {
            int i = abnormalInfoDaoOpe.create(abnormalInfo);
            Log.e("AbnormalInfoDao", "add " + i);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<AbnormalInfo> getAll() {
        try {
            return abnormalInfoDaoOpe.queryBuilder().orderBy(Const.AB_DATE, false).query();
//            return vehicledInfoDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAll() {
        try {
            abnormalInfoDaoOpe.delete(getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
