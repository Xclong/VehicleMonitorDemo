package com.xclong.vehiclemonitordemo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.xclong.vehiclemonitordemo.constant.Const;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by xcl02 on 2016/5/12.
 */
public class MyDatePickerDialog extends DatePickerDialog {
    public static MyDatePickerDialog newInstance(Context context, final TextView tv) {
        if (tv.getText() == null) return null;
        String a[] = tv.getText().toString().split("/");
        MyDatePickerDialog dialog = new MyDatePickerDialog(context, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                try {
                    view.setMinDate(Const.DATESDF.parse("2015/01/01").getTime());
                    Date date = new Date();
                    view.setMaxDate(date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tv.setText(new StringBuilder().append(year).append("/")
                        .append((monthOfYear + 1 < 10 ? ("0" + monthOfYear) : monthOfYear))
                        .append("/").append(dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth));

            }
        }, Integer.valueOf(a[0]), Integer.valueOf(a[1]) - 1, Integer.valueOf(a[0]));
        Log.e("MyDatePickerDialog", Integer.valueOf(a[0]) + " " + (Integer.valueOf(a[1]) - 1));
        return dialog;
    }

    public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }
}
