package com.xclong.vehiclemonitordemo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xclong.vehiclemonitordemo.CustomView.SmoothCheckBox;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.Util;
import com.xclong.vehiclemonitordemo.constant.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by xcl02 on 2016/5/10.
 */
public class BundingFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    //    private LinearLayout ll_a, ll_b, ll_c, ll_d, ll_e;
    private TextView tv_region/*, tv_car_number, tv_plate_color, tv_ip*/;
    private EditText et_plate_number, et_color, et_ip_1, et_ip_2, et_ip_3, et_ip_4, et_port;
    private SmoothCheckBox scb_yellow, scb_blue;
    private Button btn_bund_license_plate, btn_bund_ip;
//    private View positiveAction;

    private SweetAlertDialog mLoadingDialog;
    private int i;

    private String[] privince;
    private List<Map<String, Object>> privincelistItems = new ArrayList<Map<String, Object>>();
    private PopupWindow window;

    private int colorNUM = Const.UNKNOWNCOLOR;
    private static String TAG;
    private String license_plate;
    private String license_color;
    private String ip;
    private static BundingFragment fragment;

    private String[] CMDNumber = new String[2];
    private String[] CMDColor = new String[2];
    private String[] CMDIP = new String[2];

    private SharedPreferences sp;

    private boolean hasRegister = false;

    private BundBroadcastReceiver receiver = new BundBroadcastReceiver();

   /* private MainActivity.MyTouchListener listener = new MainActivity.MyTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromInputMethod(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    };*/

    @Override
    public void onResume() {
        super.onResume();
        if (!hasRegister) {
            IntentFilter filter1 = new IntentFilter(Const.ACTION8);
            getActivity().registerReceiver(receiver, filter1);

            IntentFilter filter2 = new IntentFilter(Const.ACTION10);
            getActivity().registerReceiver(receiver, filter2);

//            ((MainActivity) getActivity()).registerMyTouchListenters(listener);

        }
    }

    public static BundingFragment newInstance() {
        if (fragment == null) {
            fragment = new BundingFragment();
            Log.e(TAG, "BundingFragment : newInstance");
        }
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.frag_bunding);

        TAG = this.getClass().getSimpleName();

        tv_region = getViewById(R.id.tv_region);
        et_plate_number = getViewById(R.id.et_plate_number);
        scb_yellow = getViewById(R.id.scb_yellow);
        scb_blue = getViewById(R.id.scb_blue);
        et_ip_1 = getViewById(R.id.et_ip_1);
        et_ip_2 = getViewById(R.id.et_ip_2);
        et_ip_3 = getViewById(R.id.et_ip_3);
        et_ip_4 = getViewById(R.id.et_ip_4);
        et_port = getViewById(R.id.et_port);

        btn_bund_license_plate = getViewById(R.id.btn_bund_license_plate);
        btn_bund_ip = getViewById(R.id.btn_bund_ip);

        privince = getResources().getStringArray(R.array.省份);
        for (int i = 0; i < privince.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("privince", privince[i]);
            privincelistItems.add(map);
        }
    }


    @Override
    protected void setListener() {
        tv_region.setOnClickListener(this);
        btn_bund_license_plate.setOnClickListener(this);
        btn_bund_ip.setOnClickListener(this);

        scb_blue.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    colorNUM = Const.BLUENUM;
                    scb_yellow.setChecked(!isChecked);
                }
            }
        });
        scb_yellow.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    colorNUM = Const.YELLOWNUM;
                    scb_blue.setChecked(!isChecked);
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_WORLD_WRITEABLE);
        license_plate = sp.getString("license_plate", "");
        if (license_plate != "") {
            tv_region.setText(license_plate.substring(0, 1));
            et_plate_number.setText(license_plate.substring(1));
        }
        license_color = sp.getString("license_color", "");

        switch (license_color) {
            case "黄色":
                colorNUM = Const.YELLOWNUM;
                scb_yellow.setChecked(true);
                scb_blue.setChecked(false);
                break;
            case "蓝色":
                colorNUM = Const.BLUENUM;
                scb_blue.setChecked(true);
                scb_yellow.setChecked(false);
                break;
            default:
                colorNUM = Const.UNKNOWNCOLOR;
        }
        ip = sp.getString("ip", "");
        if (ip != "") {
            String[] a = ip.split(":");
            String[] b = a[0].split("\\.");
            et_ip_1.setText(b[0]);
            et_ip_2.setText(b[1]);
            et_ip_3.setText(b[2]);
            et_ip_4.setText(b[3]);
        }
    }

    @Override
    protected void lazyLoad() {
       /* if (socket != null) {
            try {
                is = socket.getInputStream();
                os = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            materialDialog = new MaterialDialog.Builder(getContext())
                    .content("请先连接蓝牙设备再进行绑定！")
                    .positiveText("确定")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            EventBus.getDefault().post(0);
                        }
                    })
                    .build();
            materialDialog.show();
        }*/
    }

    @Override
    public void onClick(View v) {
        closeInput(getContext());
        if (Util.isServiceRunning(getContext(), Const.SERVICE_NAME)) {
            Log.e(TAG, "服务正在运行");
            switch (v.getId()) {
                case R.id.tv_region:
                    showPrivincePop();
                    break;
                case R.id.btn_bund_license_plate:
                    String car_number = et_plate_number.getText().toString();
                    if (car_number != null && car_number.length() == 6) {
                        if (scb_yellow.isChecked() || scb_blue.isChecked()) {
                            Intent intent = new Intent(Const.ACTION7);
                            intent.putExtra("license", tv_region.getText().toString() + car_number);
                            intent.putExtra("color", colorNUM == Const.YELLOWNUM ? "黄色" : "蓝色");
                            getActivity().sendBroadcast(intent);
                            showPromptDialog("正在绑定", 5);
                        } else {
                            Toast.makeText(mApp, "请选择车牌颜色", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mApp, "请重新输入车牌号", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_bund_ip:
                    String ip1 = et_ip_1.getText().toString();
                    String ip2 = et_ip_2.getText().toString();
                    String ip3 = et_ip_3.getText().toString();
                    String ip4 = et_ip_4.getText().toString();
                    String ip5 = et_port.getText().toString();
                    if (ip1 != null && ip2 != null && ip3 != null && ip4 != null && ip5 != null) {
                        if (ip1 != "" && ip2 != "" && ip3 != "" && ip4 != "" && ip5 != "") {
                            if (Integer.parseInt(ip1) <= 255 && Integer.parseInt(ip2) <= 255 && Integer.parseInt(ip3) <= 255 && Integer.parseInt(ip4) <= 255 && Integer.parseInt(ip5) <= 65535) {
                                Intent intent = new Intent(Const.ACTION9);
                                intent.putExtra("ip1", et_ip_1.getText().toString());
                                intent.putExtra("ip2", et_ip_2.getText().toString());
                                intent.putExtra("ip3", et_ip_3.getText().toString());
                                intent.putExtra("ip4", et_ip_4.getText().toString());
                                intent.putExtra("port", et_port.getText().toString());
                                getActivity().sendBroadcast(intent);
                                showPromptDialog("正在绑定", 5);
                            }
                        }
                    } else {
                        Toast.makeText(mApp, "IP地址/端口不符合规范，请重新填写", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } else {
            Log.e(TAG, "服务没有运行");
            Toast.makeText(mApp, "请连接蓝牙设备", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tv_region.setText(privince[position]);
        window.dismiss();
    }

    private class BundBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Const.ACTION8:
//                    Toast.makeText(mApp, "车牌号和车牌颜色设置成功", Toast.LENGTH_LONG).show();
                    dismissPromptDialog();
                    sp.edit().putString("license_plate", tv_region.getText().toString() + et_plate_number.getText().toString()).commit();
                    sp.edit().putString("license_color", colorNUM == Const.YELLOWNUM ? "黄色" : "蓝色").commit();
                    break;
                case Const.ACTION10:
//                    Toast.makeText(mApp, "IP地址和端口设置成功", Toast.LENGTH_LONG).show();
                    dismissPromptDialog();
                    sp.edit().putString("ip", et_ip_1.getText().toString() + "."
                            + et_ip_2.getText().toString() + "."
                            + et_ip_3.getText().toString() + "."
                            + et_ip_4.getText().toString() + ":"
                            + et_port.getText().toString()).commit();
                    break;
            }
        }
    }

    public void showPrivincePop() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.custom_sweet_sheet, null);
        GridView mGridview = (GridView) mView.findViewById(R.id.gview);
        mGridview.setNumColumns(10);

        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), privincelistItems, R.layout.custom_privince_popupwindow_item, new String[]{"privince"}, new int[]{R.id.tv_item});
        mGridview.setAdapter(simpleAdapter);
        mGridview.setOnItemClickListener(this);

        window = new PopupWindow(mView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // window.setBackgroundDrawable(new BitmapDrawable());

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        // window.showAsDropDown(MainActivity.this.findViewById(R.id.start));
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(getViewById(R.id.ll_a), Gravity.BOTTOM, 0, 0);
    }

    public void showPromptDialog(String str, int type) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(getContext(), type);
            mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
//            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText(str);
        }
        mLoadingDialog.show();
    }

    public void dismissPromptDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
