package com.xclong.vehiclemonitordemo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xcl02 on 2016/5/9.
 */
public class ListDataFragment extends Fragment {

    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_license_plate_color)
    TextView tvLicensePlateColor;
    @Bind(R.id.tv_average_speed)
    TextView tvAverageSpeed;
    @Bind(R.id.tv_engine_speed)
    TextView tvEngineSpeed;
    @Bind(R.id.tv_total_mileage)
    TextView tvTotalMileage;
    @Bind(R.id.tv_differential_perssure_value)
    TextView tvDifferentialPerssureValue;
    @Bind(R.id.tv_NOx_F)
    TextView tvNOxF;
    @Bind(R.id.tv_NOx_R)
    TextView tvNOxR;
    @Bind(R.id.tv_DNOX_Effi)
    TextView tvDNOXEffi;
    @Bind(R.id.tv_TR21)
    TextView tvTR21;
    @Bind(R.id.tv_L19)
    TextView tvL19;
    @Bind(R.id.tv_TC90)
    TextView tvTC90;
    @Bind(R.id.tv_TC92)
    TextView tvTC92;
    @Bind(R.id.tv_freq_pump)
    TextView tvFreqPump;
    @Bind(R.id.tv_power)
    TextView tvPower;
    @Bind(R.id.tv_twoway_vaule_failure)
    TextView tvTwowayVauleFailure;
    @Bind(R.id.tv_metering_pump_failure)
    TextView tvMeteringPumpFailure;
    @Bind(R.id.tv_nozzle_block_faulure)
    TextView tvNozzleBlockFaultD;
    @Bind(R.id.tv_LQ8486_failure)
    TextView tvLQ8486FailureD;
    @Bind(R.id.tv_NOx_overproof_failure)
    TextView tvNOxOverproofFailure;
    @Bind(R.id.tv_NOx_sensor_failure)
    TextView tvNOxSensorFailure;
    @Bind(R.id.tv_urea_level_failure)
    TextView tvUreaLevelFailure;
    @Bind(R.id.tv_urea_quality_failure)
    TextView tvUreaQualityFailure;
    @Bind(R.id.tv_SCR_failure)
    TextView tvSCRFailure;
    @Bind(R.id.tv_depm_failure)
    TextView tvDepmFailure;

    private String TAG;
    private static String KEY = "VehicleInfo";
    private VehicledInfo vehicledInfo;

    @SuppressLint("ValidFragment")
    /*public ListDataFragment(VehicledInfo vehicledInfo) {
        this.vehicledInfo = vehicledInfo;
    }*/

    public static ListDataFragment newInstance(VehicledInfo vehicledInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, vehicledInfo);
        ListDataFragment fragment = new ListDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TAG = this.getClass().getSimpleName();
        Bundle bundle = getArguments();
        if (bundle != null) {
            vehicledInfo = (VehicledInfo) bundle.getSerializable(KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTextToView(vehicledInfo);
    }

    private void setTextToView(VehicledInfo vehicledInfo) {
        tvTime.setText(vehicledInfo.getTime());
        tvAverageSpeed.setText(vehicledInfo.getAverage_speed());
        tvEngineSpeed.setText(vehicledInfo.getEngine_speed());
        tvTotalMileage.setText(vehicledInfo.getTotal_mileage());
        tvDifferentialPerssureValue.setText(vehicledInfo.getDifferential_perssure_value());
        tvNOxF.setText(vehicledInfo.getNOx_F());
        tvNOxR.setText(vehicledInfo.getNOx_R());
        tvDNOXEffi.setText(vehicledInfo.getDNOx_Effi());
        tvTR21.setText(vehicledInfo.getTR21());
        tvL19.setText(vehicledInfo.getL19());
        tvTC90.setText(vehicledInfo.getTC90());
        tvTC92.setText(vehicledInfo.getTC92());
        tvFreqPump.setText(vehicledInfo.getFreq_pump());
        tvPower.setText(vehicledInfo.getPower());
        tvTwowayVauleFailure.setText(vehicledInfo.getTwoway_value_failure());
        tvMeteringPumpFailure.setText(vehicledInfo.getMetering_pump_failure());
        tvNozzleBlockFaultD.setText(vehicledInfo.getNozzle_block_failure());
        tvLQ8486FailureD.setText(vehicledInfo.getLQ8486_failure());
        tvNOxOverproofFailure.setText(vehicledInfo.getNOx_overproof_failure());
        tvNOxSensorFailure.setText(vehicledInfo.getNOx_sensor_failure());
        tvUreaLevelFailure.setText(vehicledInfo.getUrea_level_failure());
        tvUreaQualityFailure.setText(vehicledInfo.getUrea_quality_failure());
        tvSCRFailure.setText(vehicledInfo.getSCR_failure());
        tvDepmFailure.setText(vehicledInfo.getDePM_catalyzer_failure());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
