package com.xclong.vehiclemonitordemo.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.xclong.vehiclemonitordemo.CustomView.MultiLineRadioGroup;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.constant.Const;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;
import com.xclong.vehiclemonitordemo.database.VehicledInfoDao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xcl02 on 2016/5/9.
 */
public class CurveChartFragment extends BaseFragment implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();

    private MultiLineRadioGroup multiLineRadioGroup;
    private RadioGroup /*rg_1, rg_2, */rg_date;
    private TextView tv_start_date, tv_start_time, tv_end_date, tv_end_time;
    private Button btn_show;
    private LineChart lineChart;
    private VehicledInfoDao dao;

//    public MyHandler myHandler;

    private String initStartDateTime = null; // 初始化开始时间
    private String initEndDateTime = null; // 初始化结束时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinuter;

    private static Date startDate, endDate, now;
    private String startDateStr, endDateStr;
    private String tempStartDateStr, tempEndDateStr;

    private String titles[], titlesUnits[];
    private String title, titleUnit;

    private void initDate() {
        titles = getResources().getStringArray(R.array.curveName);
        titlesUnits = getResources().getStringArray(R.array.curveName_unit);
        title = titles[0];
        titleUnit = titlesUnits[0];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        initEndDateTime = Const.SDF.format(calendar.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        initStartDateTime = Const.SDF.format(calendar.getTime());

        Log.e(TAG, "init:" + initStartDateTime + "-----" + initEndDateTime);
        setTvDate(initStartDateTime, initEndDateTime);
    }

    private void getTvDate() {
        try {
            startDateStr = tv_start_date.getText().toString() + " " + tv_start_time.getText().toString();
            endDateStr = tv_end_date.getText().toString() + " " + tv_end_time.getText().toString();
            startDate = Const.SDF.parse(startDateStr);
            endDate = Const.SDF.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setTvDate(Date startDate, Date endDate) {
        tv_start_date.setText(Const.DATESDF.format(startDate));
        tv_start_time.setText(Const.TIMESDF.format(startDate));

        tv_end_date.setText(Const.DATESDF.format(endDate));
        tv_end_time.setText(Const.TIMESDF.format(endDate));
    }

    private void setTvDate(String str1, String str2) {
        String a[] = str1.split(" ");
        tv_start_date.setText(a[0]);
        tv_start_time.setText(a[1]);
        String b[] = str2.split(" ");
        tv_end_date.setText(b[0]);
        tv_end_time.setText(b[1]);
    }

    public static CurveChartFragment newInstance(String car_number) {
        Bundle bundle = new Bundle();
        bundle.putString("car_number", car_number);
        CurveChartFragment fragment = new CurveChartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private String mCar_number;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.frag_curve);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCar_number = bundle.getString("car_number");
        }

      /*  rg_1 = getViewById(R.id.rg_1);
        rg_2 = getViewById(R.id.rg_2);*/
        multiLineRadioGroup = getViewById(R.id.multilineradiogroup);
        rg_date = getViewById(R.id.rg_date);
        tv_start_date = getViewById(R.id.tv_start_date);
        tv_start_time = getViewById(R.id.tv_start_time);
        tv_end_date = getViewById(R.id.tv_end_date);
        tv_end_time = getViewById(R.id.tv_end_time);
        btn_show = getViewById(R.id.btn_show);
        lineChart = getViewById(R.id.linechart);

//        rg_1.check(R.id.rb_nox_f);
        multiLineRadioGroup.setItemChecked(0);
        rg_date.check(R.id.rb_days);

        dao = new VehicledInfoDao(getContext());
//        myHandler = new MyHandler();
        initDate();
        initLineChart();

        showChart();
    }

    private void initLineChart() {
        //TODO 是否在折线图上添加边框
        lineChart.setDrawBorders(true);
        //TODO 数据描述
        lineChart.setDescription("");
        // TODO 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataText("No chart data available");
        //TODO 是否显示表格颜色
        lineChart.setDrawGridBackground(false);
        //TODO 表格颜色
//        lineChart.setGridBackgroundColor(R.color.colorPrimary);
        //TODO 设置是否可以触摸
        lineChart.setTouchEnabled(true);
        //TODO 是否可以拖拽
        lineChart.setDragEnabled(true);
        //TODO 是否可以缩放
        lineChart.setScaleEnabled(true);

        lineChart.setVisibleXRangeMaximum(120);

        lineChart.setPinchZoom(false);
        // TODO 设置背景颜色
        lineChart.setBackgroundColor(Color.parseColor("#FFFAFAFA"));
    }

    @Override
    protected void setListener() {
      /*  rg_1.setOnCheckedChangeListener(new RadioCheckListener(this, rg_1, rg_2));
        rg_2.setOnCheckedChangeListener(new RadioCheckListener(this, rg_1, rg_2));*/
        multiLineRadioGroup.setOnCheckChangedListener(new MultiLineRadioGroup.OnCheckedChangedListener() {
            @Override
            public void onItemChecked(MultiLineRadioGroup group, int position, boolean checked) {
                title = titles[position];
                titleUnit = titlesUnits[position];
                showChart();
            }
        });

        rg_date.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(Const.SDF.parse(tv_end_date.getText().toString() + " " + tv_end_time.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                switch (checkedId) {
                    case R.id.rb_weeks:
                        calendar.add(Calendar.DAY_OF_YEAR, -7);
                        break;
                    case R.id.rb_months:
                        calendar.add(Calendar.MONTH, -1);
                        break;
                    case R.id.rb_years:
                        calendar.add(Calendar.YEAR, -1);
                        break;
                    case R.id.rb_days:
                        break;
                    case -1:
                        return;
                }
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                tv_start_date.setText(Const.DATESDF.format(calendar.getTime()));
                tv_start_time.setText(Const.TIMESDF.format(calendar.getTime()));

                showChart();
            }
        });

        tv_start_date.setOnClickListener(this);
        tv_start_time.setOnClickListener(this);

        tv_end_date.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);

        btn_show.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {


    }

    @Override
    protected void lazyLoad() {
        /*LineData lineData = getLineData();
        lineChart.setData(lineData);*/
        lineChart.animateXY(1000, 1000);

    }

    private LineData getLineData(String mCar_number) {
        List<VehicledInfo> list = dao.getTimeBewteen(mCar_number, startDate, endDate);
        ArrayList<String> xValues = new ArrayList<String>();
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < list.size(); i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add(list.get(i).getTime());
            yValues.add(new Entry(Float.valueOf(list.get(i).get(title)), i));
        }

        LineDataSet lineDataSet = new LineDataSet(yValues, titleUnit /*显示在比例图上*/);

        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(4f);// 显示的圆形大小
        lineDataSet.setColor(R.color.colorPrimary);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色

        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSet);

        return lineData;

    }

    @Override
    public void onClick(View v) {
        getTvDate();
        tempStartDateStr = startDateStr;
        tempEndDateStr = endDateStr;
        switch (v.getId()) {
            case R.id.tv_start_date:
                String a[] = tv_start_date.getText().toString().split("/");
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            view.setMinDate(Const.DATESDF.parse("2015/01/01").getTime());
                            view.setMaxDate(System.currentTimeMillis());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mYear = year;
                        mMonth = monthOfYear + 1;
                        mDay = dayOfMonth;
                        tv_start_date.setText("" + mYear + "/" + (mMonth + 1 < 10 ? ("0" + mMonth) : mMonth) + "/" + (mDay < 10 ? ("0" + mDay) : mDay));
                    }
                }, Integer.valueOf(a[0]), Integer.valueOf(a[1]) - 1, Integer.valueOf(a[2])).show();

                break;
            case R.id.tv_start_time:
                String b[] = tv_start_time.getText().toString().split(":");
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinuter = minute;
                        tv_start_time.setText("" + (mHour < 10 ? "0" + mHour : mHour) + ":" + (mMinuter < 10 ? "0" + mMinuter : mMinuter) + ":00");
                    }
                }, Integer.valueOf(b[0]), Integer.valueOf(b[1]), true).show();


                break;
            case R.id.tv_end_date:
                String c[] = tv_end_date.getText().toString().split("/");
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            view.setMinDate(Const.DATESDF.parse("2015/01/01").getTime());
                            view.setMaxDate(System.currentTimeMillis());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mYear = year;
                        mMonth = monthOfYear + 1;
                        mDay = dayOfMonth;
                        tv_end_date.setText("" + mYear + "/" + (mMonth + 1 < 10 ? ("0" + mMonth) : mMonth) + "/" + (mDay < 10 ? ("0" + mDay) : mDay));

                    }
                }, Integer.valueOf(c[0]), Integer.valueOf(c[1]) - 1, Integer.valueOf(c[2])).show();
                break;
            case R.id.tv_end_time:
                String d[] = tv_end_time.getText().toString().split(":");
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinuter = minute;
                        tv_end_time.setText("" + (mHour < 10 ? "0" + mHour : mHour) + ":" + (mMinuter < 10 ? "0" + mMinuter : mMinuter) + ":00");
                    }
                }, Integer.valueOf(d[0]), Integer.valueOf(d[1]), true).show();
                break;
            case R.id.btn_show:
                Log.e(TAG, "Btn show has been clicked");
                rg_date.check(-1);
                setTvDate(tempStartDateStr, tempEndDateStr);
                getTvDate();

                now = new Date();

                if (startDate.after(now) && endDate.after(now)) {
                    initDate();
                } else if (startDate.before(now) && endDate.before(now)) {
                    if (startDate.after(endDate)) {
                        exchange();
                    } else if (startDate.compareTo(endDate) == 0) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startDate);
                        calendar.set(Calendar.HOUR_OF_DAY, -1);
                        String str[] = Const.SDF.format(calendar.getTime()).split(" ");
                        tv_start_date.setText(str[0]);
                        tv_start_time.setText(str[1]);
                    }
                } else if (startDate.after(now) || endDate.after(now)) {
                    Date date = startDate.compareTo(endDate) < 0 ? startDate : endDate;
                    setTvDate(date, now);
                }

                getTvDate();
                showChart();


                break;
        }
    }

    private void exchange() {
        String tmp = startDateStr;
        startDateStr = endDateStr;
        endDateStr = tmp;

        setTvDate(startDateStr, endDateStr);
    }

    public void showChart() {
        getTvDate();
        lineChart.setData(getLineData(mCar_number));
        lineChart.animateXY(500, 500);
    }

    /*public class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            title = titles[msg.what];
            showChart();

        }
    }*/


}
