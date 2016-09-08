package com.xclong.vehiclemonitordemo.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.Util;
import com.xclong.vehiclemonitordemo.activity.BluetoothActivity;
import com.xclong.vehiclemonitordemo.adapter.MyBaseAdapter1;
import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.constant.Const;
import com.xclong.vehiclemonitordemo.constant.QueryItem;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;
import com.xclong.vehiclemonitordemo.database.VehicledInfoDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xcl02 on 2016/5/9.
 */
public class HomePageFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.tv_head_car_number)
    TextView tvHeadCarNumber;
    @Bind(R.id.tv_head_NOx_F)
    TextView tvHeadNOxF;
    @Bind(R.id.tv_head_NOx_R)
    TextView tvHeadNOxR;
    @Bind(R.id.tv_head_DNOx_Effi)
    TextView tvHeadDNOxEffi;
    @Bind(R.id.tv_head_TR21)
    TextView tvHeadTR21;
    @Bind(R.id.tv_head_L19)
    TextView tvHeadL19;
    @Bind(R.id.tv_head_TC90)
    TextView tvHeadTC90;
    @Bind(R.id.tv_head_TC92)
    TextView tvHeadTC92;
    @Bind(R.id.tv_head_freq_pump)
    TextView tvHeadFreqPump;
    @Bind(R.id.tv_head_power)
    TextView tvHeadPower;
    @Bind(R.id.tv_head_twoway_value_failure)
    TextView tvHeadTwowayValueFailure;
    @Bind(R.id.tv_head_metering_pump_failure)
    TextView tvHeadMeteringPumpFailure;
    @Bind(R.id.tv_head_nozzle_block_failure)
    TextView tvHeadNozzleBlockFailure;
    @Bind(R.id.tv_head_LQ84)
    TextView tvHeadLQ84;
    @Bind(R.id.tv_head_NOx_overproof_failure)
    TextView tvHeadNOxOverproofFailure;
    @Bind(R.id.tv_head_NOx_sensor_failure)
    TextView tvHeadNOxSensorFailure;
    @Bind(R.id.tv_head_urea_level_failure)
    TextView tvHeadUreaLevelFailure;
    @Bind(R.id.tv_head_urea_quality_failure)
    TextView tvHeadUreaQualityFailure;
    @Bind(R.id.tv_head_SCR_failure)
    TextView tvHeadSCRFailure;
    @Bind(R.id.tv_head_Depm_failure)
    TextView tvHeadDepmFailure;
    @Bind(R.id.tv_head_time)
    TextView tvHeadTime;
    @Bind(R.id.tv_head_zt)
    TextView tvHeadZt;
    @Bind(R.id.listview)
    ListView listview;
    private static String TAG;
    private EditText et_dialog_number;
    private Spinner sp_dialog_province, sp_dialog_city;
    private String[] arr_province;
    private ArrayAdapter<String> sp_adapter1, sp_adapter2;

    private List<VehicledInfo> vlist2 = new ArrayList<>();

    private TextView[] headViews;
    private MyBaseAdapter1 adapter;
    private VehicledInfoDao dao;
    private static HomePageFragment fragment;


    private View rootView;
    public static boolean[] itemSelects = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true};
    /*private int[] headViewIDs = new int[]{R.id.tv_head_TC90, R.id.tv_head_TC92, R.id.tv_head_freq_pump, R.id.tv_head_power, R.id.tv_head_twoway_value_failure,
            R.id.tv_head_metering_pump_failure, R.id.tv_head_nozzle_block_failure, R.id.tv_head_LQ84, R.id.tv_head_NOx_overproof_failure, R.id.tv_head_NOx_sensor_failure,
            R.id.tv_head_urea_level_failure, R.id.tv_head_urea_quality_failure, R.id.tv_head_SCR_failure, R.id.tv_head_Depm_failure};*/
    private int[] itemViewIDs = new int[]{R.id.tv_item_TC90, R.id.tv_item_TC92, R.id.tv_item_freq_pump, R.id.tv_item_power, R.id.tv_item_twoway_value_failure,
            R.id.tv_item_metering_pump_failure, R.id.tv_item_nozzle_block_failure, R.id.tv_item_LQ84, R.id.tv_item_NOx_overproof_failure, R.id.tv_item_NOx_sensor_failure,
            R.id.tv_item_urea_level_failure, R.id.tv_item_urea_quality_failure, R.id.tv_item_SCR_failure, R.id.tv_item_Depm_failure};

    private boolean hasRegister = false;
    private BackgroundReceiver mReceiver = new BackgroundReceiver();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TAG = this.getClass().getSimpleName();
        arr_province = getResources().getStringArray(R.array.province);

    }


    @Override
    public void onResume() {
        super.onResume();

        if (!hasRegister) {
            hasRegister = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Const.ACTION1);
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_homepage, null);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        Log.e(TAG, "onCreateView");
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public synchronized static HomePageFragment newInstance() {
        if (fragment == null) {
            fragment = new HomePageFragment();
            Log.e(TAG, "HomePageFragment : newInstance");
        }
        return fragment;
    }

    private void init() {
        headViews = new TextView[]{tvHeadTC90, tvHeadTC92, tvHeadFreqPump, tvHeadPower, tvHeadTwowayValueFailure,
                tvHeadMeteringPumpFailure, tvHeadNozzleBlockFailure, tvHeadLQ84, tvHeadNOxOverproofFailure, tvHeadNOxSensorFailure,
                tvHeadUreaLevelFailure, tvHeadUreaQualityFailure, tvHeadSCRFailure, tvHeadDepmFailure};
        dao = new VehicledInfoDao(getContext());


        vlist2 = dao.getAll();//从数据库里取
//        Collections.sort(vlist2);
        Log.e(TAG, vlist2.size() + "");
        adapter = new MyBaseAdapter1(getContext(), vlist2) {
            @Override
            protected void notifyVisiblity(View view) {
                setItemVisibility(view);
            }
        };
        listview.setAdapter(adapter);
        listview.setFastScrollEnabled(true);
    }

    private void setItemVisibility(View convertView) {
        for (int i = 0; i < itemSelects.length; i++) {
            if (itemSelects[i] == true) {
                headViews[i].setVisibility(View.VISIBLE);
                convertView.findViewById(itemViewIDs[i]).setVisibility(View.VISIBLE);
            } else {
                headViews[i].setVisibility(View.GONE);
                convertView.findViewById(itemViewIDs[i]).setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_query:
                customDialog();
                break;
            case R.id.action_add:
                displayColumn();
                break;
            case R.id.action_bluetooth:
                if (Util.isServiceRunning(getContext(), "com.xclong.vehiclemonitordemo.service.CommunicationService")) {
                    Log.e(TAG, "服务正在运行");
                    MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                            .content("已连接蓝牙设备，是否更换？")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    Intent intent1 = new Intent();
                                    intent1.setAction(Const.ACTION2);
                                    intent1.putExtra("state", false);
                                    getActivity().sendBroadcast(intent1);

                                    Intent intent = new Intent();
                                    intent.setAction("com.xclong.vehiclemonitordemo.COMMUNICATION_SERVICE");
                                    intent.setPackage(getActivity().getPackageName());
                                    getActivity().stopService(intent);

                                    BluetoothMsg.hasConnect = false;

                                    startBluetoothActivity();
                                }
                            }).build();
                    dialog.show();
                } else {
                    startBluetoothActivity();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void customDialog() {
        final QueryItem queryItem = new QueryItem();
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("请填入搜索项")
                .customView(R.layout.query_dialog, true)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (!et_dialog_number.getText().toString().equals(""))
                            queryItem.setCar_number(et_dialog_number.getText().toString());
                        if (!sp_dialog_province.getSelectedItem().toString().equals("请选择")) {
                            queryItem.setProvince(sp_dialog_province.getSelectedItemPosition());
                            if (!sp_dialog_city.getSelectedItem().toString().equals("请选择")) {
                                queryItem.setCity(sp_dialog_city.getSelectedItemPosition());
                            }
                        }
                        vlist2 = dao.getSelect(queryItem);
                        Log.e(TAG, vlist2.size() + "");
                        adapter = new MyBaseAdapter1(getContext(), vlist2) {
                            @Override
                            protected void notifyVisiblity(View view) {
                                setItemVisibility(view);
                            }
                        };
                        listview.setAdapter(adapter);

                    }
                }).build();

        et_dialog_number = (EditText) dialog.getCustomView().findViewById(R.id.et_dialog_number);
        sp_dialog_province = (Spinner) dialog.getCustomView().findViewById(R.id.sp_dialog_province);
        sp_dialog_city = (Spinner) dialog.getCustomView().findViewById(R.id.sp_dialog_city);

        sp_adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.province));
        sp_dialog_province.setAdapter(sp_adapter1);
        sp_dialog_province.setOnItemSelectedListener(this);

        dialog.show();
        Log.e(TAG, "dialog.show()");


    }

    private void displayColumn() {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_launcher_settings)
                .setTitle("选择显示/隐藏的列")
                .setMultiChoiceItems(getResources().getStringArray(R.array.displayColumn), itemSelects, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyDataSetChanged();
                    }
                })
                .create()
                .show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String str = sp_dialog_province.getSelectedItem().toString();
        int i = 0;
        while (i < arr_province.length) {
            if (str.equals(arr_province[i])) break;
            ++i;
        }
        switch (i) {
            case 0:
                sp_adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"请选择"});
                break;
            case 1:
                sp_adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.hubei));
                break;
            case 2:
                sp_adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.shandong));
                break;
        }
        sp_dialog_city.setAdapter(sp_adapter2);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hasRegister) {
            hasRegister = false;
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    public void startBluetoothActivity() {
        Intent intent = new Intent(getActivity().getApplicationContext(), BluetoothActivity.class);
        intent.putExtra("jump", "main");
        startActivity(intent);
    }


    public class BackgroundReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Const.ACTION1)) {
                vlist2.add(0, (VehicledInfo) intent.getSerializableExtra("broadcast"));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
