package com.xclong.vehiclemonitordemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xcl02 on 2016/3/22.
 */
public class DatebaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "car_info.db";
    private static final int DATABASE_VERSION = 1;

    private static DatebaseHelper instance;
    private Map<String, Dao> daos = new HashMap<>();

    public DatebaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, VehicledInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, VehicledInfo.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static synchronized DatebaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DatebaseHelper.class) {
                if (instance == null)
                    instance = new DatebaseHelper(context);
            }
        }
        return instance;
    }

    public synchronized Dao getDao(Class clz) throws SQLException {
        Dao dao = null;
        String classNmae = clz.getSimpleName();
        if (daos.containsKey(classNmae)) {
            dao = daos.get(classNmae);
        }
        if (dao == null) {
            dao = super.getDao(clz);
            daos.put(classNmae, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
