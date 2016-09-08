package com.xclong.vehiclemonitordemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.activity.DetailActivity;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;

import java.util.List;

/**
 * Created by xcl02 on 2016/6/9.
 */
public abstract class MyBaseAdapter1 extends BaseAdapter {

    private String TAG = "MyBaseAdapter1";

    private List<VehicledInfo> vehicledInfos;
    private LayoutInflater mInflater;
    private Typeface mTypeFaceLight;
    private Typeface mTypeFaceRegular;
    private Context mcontext;
    private int lastPosition;

    private VehicledInfo vehicledInfo;

    public MyBaseAdapter1(Context context, List<VehicledInfo> vehicledInfos) {
        this.mcontext = context;
        this.mInflater = LayoutInflater.from(context);
        this.vehicledInfos = vehicledInfos;

        mTypeFaceLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        mTypeFaceRegular = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }

//    public void addItem(VehicledInfo vehicledInfo) {
//        vehicledInfos.add(0, vehicledInfo);
//    }

    public void setList(List<VehicledInfo> iList) {
        this.vehicledInfos = iList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (vehicledInfos != null) {
            count = vehicledInfos.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        VehicledInfo vehicledInfo = null;
        if (vehicledInfos != null) {
            vehicledInfo = vehicledInfos.get(position);
        }
        return vehicledInfo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.e(TAG, "position = " + position);
        final int mPosition = position;

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.car_number = (TextView) convertView.findViewById(R.id.tv_item_car_number);
            viewHolder.NOx_F = (TextView) convertView.findViewById(R.id.tv_item_NOx_F);
            viewHolder.NOx_R = (TextView) convertView.findViewById(R.id.tv_item_NOx_R);
            viewHolder.DNOx_Effi = (TextView) convertView.findViewById(R.id.tv_item_DNOx_Effi);
            viewHolder.TR21 = (TextView) convertView.findViewById(R.id.tv_item_TR21);
            viewHolder.L19 = (TextView) convertView.findViewById(R.id.tv_item_L19);
            viewHolder.TC90 = (TextView) convertView.findViewById(R.id.tv_item_TC90);
            viewHolder.TC92 = (TextView) convertView.findViewById(R.id.tv_item_TC92);
            viewHolder.Freq_pump = (TextView) convertView.findViewById(R.id.tv_item_freq_pump);
            viewHolder.Power = (TextView) convertView.findViewById(R.id.tv_item_power);
            viewHolder.Twoway_value = (TextView) convertView.findViewById(R.id.tv_item_twoway_value_failure);
            viewHolder.metering_pump_failure = (TextView) convertView.findViewById(R.id.tv_item_metering_pump_failure);
            viewHolder.nozzle_block_failure = (TextView) convertView.findViewById(R.id.tv_item_nozzle_block_failure);
            viewHolder.LQ8486_failure = (TextView) convertView.findViewById(R.id.tv_item_LQ84);
            viewHolder.NOx_overproof_failure = (TextView) convertView.findViewById(R.id.tv_item_NOx_overproof_failure);
            viewHolder.NOx_sensor_failure = (TextView) convertView.findViewById(R.id.tv_item_NOx_sensor_failure);
            viewHolder.urea_level_failure = (TextView) convertView.findViewById(R.id.tv_item_urea_level_failure);
            viewHolder.urea_quality_failure = (TextView) convertView.findViewById(R.id.tv_item_urea_quality_failure);
            viewHolder.SCR_failure = (TextView) convertView.findViewById(R.id.tv_item_SCR_failure);
            viewHolder.DePM_catalyzer_failure = (TextView) convertView.findViewById(R.id.tv_item_Depm_failure);
            viewHolder.zt = (TextView) convertView.findViewById(R.id.tv_item_zt);
            viewHolder.riqi = (TextView) convertView.findViewById(R.id.tv_item_time);

            viewHolder.car_number.setTypeface(mTypeFaceLight);
            viewHolder.NOx_F.setTypeface(mTypeFaceLight);
            viewHolder.NOx_R.setTypeface(mTypeFaceLight);
            viewHolder.DNOx_Effi.setTypeface(mTypeFaceLight);
            viewHolder.TR21.setTypeface(mTypeFaceLight);
            viewHolder.L19.setTypeface(mTypeFaceLight);
            viewHolder.TC90.setTypeface(mTypeFaceLight);
            viewHolder.TC92.setTypeface(mTypeFaceLight);
            viewHolder.Freq_pump.setTypeface(mTypeFaceLight);
            viewHolder.Power.setTypeface(mTypeFaceLight);
            viewHolder.Twoway_value.setTypeface(mTypeFaceLight);
            viewHolder.metering_pump_failure.setTypeface(mTypeFaceLight);
            viewHolder.nozzle_block_failure.setTypeface(mTypeFaceLight);
            viewHolder.LQ8486_failure.setTypeface(mTypeFaceLight);
            viewHolder.NOx_overproof_failure.setTypeface(mTypeFaceLight);
            viewHolder.NOx_sensor_failure.setTypeface(mTypeFaceLight);
            viewHolder.urea_level_failure.setTypeface(mTypeFaceLight);
            viewHolder.urea_quality_failure.setTypeface(mTypeFaceLight);
            viewHolder.SCR_failure.setTypeface(mTypeFaceLight);
            viewHolder.DePM_catalyzer_failure.setTypeface(mTypeFaceLight);
            viewHolder.zt.setTypeface(mTypeFaceLight);
            viewHolder.riqi.setTypeface(mTypeFaceLight);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mPosition == 0) {
            Animation animation = AnimationUtils.loadAnimation(mcontext, R.anim.login_photo_scale_big);
            convertView.startAnimation(animation);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext.getApplicationContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("VehicledInfo", vehicledInfos.get(mPosition));
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
            }
        });

        vehicledInfo = (VehicledInfo) getItem(mPosition);

        convertView.setBackgroundColor(mcontext.getResources().getColor(R.color.background1));
        viewHolder.car_number.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.NOx_F.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.NOx_R.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.DNOx_Effi.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.TR21.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.L19.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.TC90.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.TC92.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.Freq_pump.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.Power.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.Twoway_value.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.metering_pump_failure.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.nozzle_block_failure.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.LQ8486_failure.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.NOx_overproof_failure.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.NOx_sensor_failure.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.urea_level_failure.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.urea_quality_failure.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.SCR_failure.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.DePM_catalyzer_failure.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));
        viewHolder.riqi.setTextColor(mcontext.getResources().getColor(R.color.secondary_text));

        viewHolder.car_number.setText(vehicledInfo.getCar_number());
        viewHolder.NOx_F.setText(vehicledInfo.getNOx_F());
        viewHolder.NOx_R.setText(vehicledInfo.getNOx_R());
        viewHolder.DNOx_Effi.setText(vehicledInfo.getDNOx_Effi());
        viewHolder.TR21.setText(vehicledInfo.getTR21());
        viewHolder.L19.setText(vehicledInfo.getL19());
        viewHolder.TC90.setText(vehicledInfo.getTC90());
        viewHolder.TC92.setText(vehicledInfo.getTC92());
        viewHolder.Freq_pump.setText(vehicledInfo.getFreq_pump());
        viewHolder.Power.setText(vehicledInfo.getPower());
        viewHolder.Twoway_value.setText(vehicledInfo.getTwoway_value_failure());
        viewHolder.metering_pump_failure.setText(vehicledInfo.getMetering_pump_failure());
        viewHolder.nozzle_block_failure.setText(vehicledInfo.getNozzle_block_failure());
        viewHolder.LQ8486_failure.setText(vehicledInfo.getLQ8486_failure());
        viewHolder.NOx_overproof_failure.setText(vehicledInfo.getNOx_overproof_failure());
        viewHolder.NOx_sensor_failure.setText(vehicledInfo.getNOx_sensor_failure());
        viewHolder.urea_level_failure.setText(vehicledInfo.getUrea_level_failure());
        viewHolder.urea_quality_failure.setText(vehicledInfo.getUrea_quality_failure());
        viewHolder.SCR_failure.setText(vehicledInfo.getSCR_failure());
        viewHolder.DePM_catalyzer_failure.setText(vehicledInfo.getDePM_catalyzer_failure());
        viewHolder.riqi.setText(vehicledInfo.getTime());
        switch (vehicledInfo.getStatus()) {
            case "0":
                viewHolder.zt.setBackground(mcontext.getResources().getDrawable(R.drawable.oval_gray));
                viewHolder.zt.clearAnimation();
                break;
            case "1":
                viewHolder.zt.setBackground(mcontext.getResources().getDrawable(R.drawable.oval_red));
                viewHolder.zt.clearAnimation();

                convertView.setBackgroundColor(mcontext.getResources().getColor(R.color.background2));
                viewHolder.car_number.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.NOx_F.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.NOx_R.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.DNOx_Effi.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.TR21.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.L19.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.TC90.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.TC92.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.Freq_pump.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.Power.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.Twoway_value.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.metering_pump_failure.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.nozzle_block_failure.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.LQ8486_failure.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.NOx_overproof_failure.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.NOx_sensor_failure.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.urea_level_failure.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.urea_quality_failure.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.SCR_failure.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.DePM_catalyzer_failure.setTextColor(mcontext.getResources().getColor(R.color.white));
                viewHolder.riqi.setTextColor(mcontext.getResources().getColor(R.color.white));
                break;
            case "2":
                viewHolder.zt.setBackground(mcontext.getResources().getDrawable(R.drawable.oval_green));
                if (viewHolder.zt.getVisibility() != View.GONE)
                    startFlick(viewHolder.zt);//图片闪烁
                else viewHolder.zt.clearAnimation();
                break;
        }
        notifyVisiblity(convertView);
        return convertView;
    }

    public void startFlick(View view) {
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(500);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(alphaAnimation);
    }

    protected abstract void notifyVisiblity(View view);

    private final class ViewHolder {
        public TextView car_number;
        public TextView NOx_F;
        public TextView NOx_R;
        public TextView DNOx_Effi;
        public TextView TR21;
        public TextView L19;
        public TextView TC90;
        public TextView TC92;
        public TextView Freq_pump;
        public TextView Power;
        public TextView Twoway_value;
        public TextView metering_pump_failure;
        public TextView nozzle_block_failure;
        public TextView LQ8486_failure;
        public TextView NOx_overproof_failure;
        public TextView NOx_sensor_failure;
        public TextView urea_level_failure;
        public TextView urea_quality_failure;
        public TextView SCR_failure;
        public TextView DePM_catalyzer_failure;
        public TextView riqi;
        public TextView zt;
    }
}
