package com.xclong.vehiclemonitordemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.adapter.FaultMessageAdpater;
import com.xclong.vehiclemonitordemo.constant.AbnormalInfo;
import com.xclong.vehiclemonitordemo.database.AbnormalInfoDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcl02 on 2016/5/10.
 */
public class FaultFragment extends BaseFragment {

    private static String TAG = FaultFragment.class.getSimpleName();
    private LinearLayout ll_fault_null;
    private RecyclerView recyclerView;

    private ListView listview;

    private static FaultFragment fragment;

    private AbnormalInfoDao aDao;
    private List<AbnormalInfo> abnormalInfos = new ArrayList<>();
    //    private FaultAdapter adapter;
    private FaultMessageAdpater adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public synchronized static FaultFragment newInstance() {
        if (fragment == null) {
            fragment = new FaultFragment();
            Log.e(TAG, "FaultFragment : newInstance");
        }
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.frag_fault);

        ll_fault_null = getViewById(R.id.ll_fault_null);
//        recyclerView = getViewById(R.id.rv_fault);
        listview = getViewById(R.id.rv_fault);

//        showPromptDialog("正在加载", SweetAlertDialog.PROGRESS_TYPE);

        /*aDao = new AbnormalInfoDao(getContext());
        abnormalInfos = aDao.getAll();
        if (abnormalInfos != null && abnormalInfos.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            ll_fault_null.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            ll_fault_null.setVisibility(View.VISIBLE);
        }*/

//        recyclerView.setVisibility(View.VISIBLE);
        listview.setVisibility(View.VISIBLE);
        ll_fault_null.setVisibility(View.GONE);

       /* for (int i = 0; i < 10; i++) {
            abnormalInfos.add(0, new AbnormalInfo("鄂A11111", "尿素液位故障 NOx传感器故障", "2016/08/01", "14:45"));
        }*/

        insertTextData();

//        adapter = new FaultAdapter(getContext(), abnormalInfos);
        adapter = new FaultMessageAdpater(getContext(), R.layout.fault_item, abnormalInfos);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listview.setAdapter(adapter);

//        dismissPromptDialog();

    }

    private void insertTextData() {
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "尿素液位故障", "2016/05/30", "10:00:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "NOX超标故障", "2016/05/31", "11:20:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "尿素质量故障", "2016/05/31", "11:30:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "NOX传感器故障", "2016/05/31", "11:35:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "喷嘴堵，NOX超标故障", "2016/06/11", "11:40:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "两通阀故障", "2016/06/11", "11:50:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "NOX传感器故障", "2016/06/12", "12:00:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "NOX超标故障", "2016/06/12", "09:01:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "LQ84-86故障", "2016/06/12", "09:03:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "两通阀故障", "2016/06/12", "09:05:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "电源故障", "2016/06/13", "09:10:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "NOX传感器故障", "2016/06/13", "09:30:00"));
        abnormalInfos.add(new AbnormalInfo("鄂A11111", "SCR故障故障", "2016/06/13", "09:32:00"));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fault, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fault_empty:
                Log.e(TAG, "清空按钮被点击");
                new MaterialDialog.Builder(getActivity())
                        .iconRes(R.mipmap.ic_action_navigation_cancel)
                        .limitIconToDefaultSize()
                        .content("确定清空故障日志？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Toast.makeText(mApp, "清空故障日志", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void lazyLoad() {

    }
}
