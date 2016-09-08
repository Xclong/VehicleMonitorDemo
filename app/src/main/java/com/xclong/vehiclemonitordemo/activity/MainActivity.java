package com.xclong.vehiclemonitordemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.application.App;
import com.xclong.vehiclemonitordemo.constant.Const;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;
import com.xclong.vehiclemonitordemo.database.VehicledInfoDao;
import com.xclong.vehiclemonitordemo.fragment.BundingFragment;
import com.xclong.vehiclemonitordemo.fragment.FaultFragment;
import com.xclong.vehiclemonitordemo.fragment.HomePageFragment;
import com.xclong.vehiclemonitordemo.fragment.MessageFragment;
import com.xclong.vehiclemonitordemo.fragment.OpinionFragment;
import com.xclong.vehiclemonitordemo.fragment.UpgradeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGAViewPager;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.mviewpager)
    BGAViewPager mviewpager;
    @Bind(R.id.navigationView)
    NavigationView navigationView;
    @Bind(R.id.drawerlayout)
    DrawerLayout drawerlayout;

    private TextView tv_name;
    private ImageView img;
    private ActionBarDrawerToggle mDrawerToggle;


    private int itemNumber = 0;

    private List<VehicledInfo> vlist = new ArrayList<>();
    private VehicledInfoDao dao;
    private String LoginInfo;
    private String TAG;
    private SweetAlertDialog mLoadingDialog;

//    private PushAgent mPushAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        ButterKnife.bind(this);

        itemNumber = getIntent().getIntExtra("itemNumber", 0);
        Log.e(TAG, "itemNumber = " + itemNumber);

        EventBus.getDefault().register(this);

        App.getInstance().addActivity(this);
        TAG = this.getClass().getSimpleName();
        initView();
        setListener();
        initData();

//        mPushAgent = PushAgent.getInstance(this);

//        mPushAgent.onAppStart();
//        mPushAgent.enable();

//        Log.e(TAG, "Device token : " + UmengRegistrar.getRegistrationId(this));


    }

    private void initData() {
        dao = new VehicledInfoDao(this);
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(Const.PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int currentVersion = info.versionCode;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Const.LOGININFO, Context.MODE_WORLD_WRITEABLE); //私有数据
        LoginInfo = sharedPreferences.getString(Const.LOGINSTATE, Const.NOTLOGIN);
        int lastVersion = prefs.getInt(Const.VERSION_KEY, 0);
        if (currentVersion > lastVersion) {
            insertData();
            //如果当前版本大于上次版本，该版本属于第一次启动
            for (int i = 0; i < vlist.size(); i++) {
                dao.add(vlist.get(i));
            }

            prefs.edit().putInt(Const.VERSION_KEY, currentVersion).commit();
            if (LoginInfo.equals(Const.ADMINLOGIN)) {
                Log.e(TAG, "管理员首次登陆");
                navigationView.setCheckedItem(R.id.nav_main);
                mviewpager.setCurrentItem(0, true);
                setTitle(R.string.navigation_main);
            } else if (LoginInfo.equals(Const.NORMALLOGIN)) {
                Log.e(TAG, "普通用户首次登陆");
                navigationView.setCheckedItem(R.id.nav_bunding);
                mviewpager.setCurrentItem(3, true);
                setTitle(R.string.navigation_bunding);
            }
        } else {
            Log.e(TAG, "用户非首次登陆");
            if (itemNumber == 0) {
                navigationView.setCheckedItem(R.id.nav_main);
                mviewpager.setCurrentItem(itemNumber, true);
                setTitle(R.string.navigation_main);
            } else if (itemNumber == 1) {
                navigationView.setCheckedItem(R.id.nav_message);
                mviewpager.setCurrentItem(itemNumber, true);
                setTitle(R.string.navigation_message);
            }
        }
    }

    private void insertData() {
        Log.v("xcl-insert", "insertData");
        VehicledInfo vi1 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "100", "80", "80", "70", "65", "20", "50", "45", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "异常", "正常", "正常", "2016/05/30 10:00:00", "1");
        VehicledInfo vi2 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "78", "56", "64", "64", "53", "43", "53", "46", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/05/30 10:05:00", "2");
        VehicledInfo vi3 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "67", "76", "44", "63", "42", "53", "74", "40", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/05/30 10:10:00", "0");
        VehicledInfo vi4 = new VehicledInfo("鲁A11111", "黄色", "100", "100", "5000", "100", "26", "54", "55", "74", "53", "65", "42", "40", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/05/31 10:15:00", "2");
        VehicledInfo vi5 = new VehicledInfo("鄂B11111", "黄色", "100", "100", "5000", "100", "78", "67", "55", "75", "54", "54", "44", "40", "正常", "正常", "正常", "正常", "正常", "异常", "正常", "正常", "正常", "正常", "正常", "2016/05/31 11:20:00", "1");
        VehicledInfo vi6 = new VehicledInfo("鄂A22222", "黄色", "100", "100", "5000", "100", "55", "43", "77", "66", "75", "53", "55", "48", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/05/31 11:25:00", "2");
        VehicledInfo vi7 = new VehicledInfo("鄂A11131", "黄色", "100", "100", "5000", "100", "84", "78", "56", "55", "90", "67", "56", "36", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "异常", "正常", "正常", "正常", "2016/05/31 11:30:00", "1");
        VehicledInfo vi8 = new VehicledInfo("鲁A11111", "黄色", "100", "100", "5000", "100", "68", "68", "44", "64", "100", "64", "33", "53", "正常", "正常", "正常", "正常", "正常", "正常", "异常", "正常", "正常", "正常", "正常", "2016/05/31 11:35:00", "1");
        VehicledInfo vi9 = new VehicledInfo("鄂A22222", "黄色", "100", "100", "5000", "100", "45", "35", "56", "84", "62", "56", "64", "45", "正常", "正常", "正常", "异常", "正常", "异常", "正常", "正常", "正常", "正常", "正常", "2016/06/11 11:40:00", "1");
        VehicledInfo vi10 = new VehicledInfo("鲁A11111", "黄色", "100", "100", "5000", "100", "89", "85", "25", "63", "55", "63", "65", "65", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/11 11:45:00", "2");
        VehicledInfo vi11 = new VehicledInfo("鲁A11111", "黄色", "100", "100", "5000", "100", "75", "84", "84", "62", "66", "53", "43", "45", "正常", "异常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/11 11:50:00", "1");
        VehicledInfo vi12 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "80", "64", "64", "52", "64", "54", "44", "30", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/11 11:55:00", "0");
        VehicledInfo vi13 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "90", "74", "74", "86", "86", "78", "85", "65", "正常", "正常", "正常", "正常", "正常", "正常", "异常", "正常", "正常", "正常", "正常", "2016/06/12 12:00:00", "1");
        VehicledInfo vi14 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "45", "32", "88", "47", "43", "64", "46", "46", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/12 08:55:00", "2");
        VehicledInfo vi15 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "53", "67", "55", "46", "87", "86", "63", "47", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/12 09:00:00", "2");
        VehicledInfo vi16 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "12", "27", "85", "87", "46", "35", "75", "37", "正常", "正常", "正常", "正常", "正常", "异常", "正常", "正常", "正常", "正常", "正常", "2016/06/12 09:01:00", "1");
        VehicledInfo vi17 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "47", "27", "89", "65", "89", "75", "35", "95", "正常", "正常", "正常", "正常", "异常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/12 09:03:00", "1");
        VehicledInfo vi18 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "35", "81", "33", "58", "33", "36", "44", "64", "正常", "异常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/12 09:05:00", "1");
        VehicledInfo vi19 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "75", "111", "84", "48", "47", "73", "85", "75", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/13 09:08:00", "0");
        VehicledInfo vi20 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "22", "85", "66", "67", "87", "74", "36", "66", "异常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/13 09:10:00", "1");
        VehicledInfo vi21 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "66", "16", "98", "99", "47", "86", "85", "64", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "2016/06/13 09:12:00", "2");
        VehicledInfo vi22 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "24", "86", "56", "68", "98", "35", "54", "74", "正常", "正常", "正常", "正常", "正常", "正常", "异常", "正常", "正常", "正常", "正常", "2016/06/13 09:30:00", "1");
        VehicledInfo vi23 = new VehicledInfo("鄂A11111", "黄色", "100", "100", "5000", "100", "76", "83", "87", "45", "25", "86", "65", "75", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "正常", "异常", "正常", "2016/06/13 09:32:00", "1");
        vlist.add(vi1);
        vlist.add(vi2);
        vlist.add(vi3);
        vlist.add(vi4);
        vlist.add(vi5);
        vlist.add(vi6);
        vlist.add(vi7);
        vlist.add(vi8);
        vlist.add(vi9);
        vlist.add(vi10);
        vlist.add(vi11);
        vlist.add(vi12);
        vlist.add(vi13);
        vlist.add(vi14);
        vlist.add(vi15);
        vlist.add(vi16);
        vlist.add(vi17);
        vlist.add(vi18);
        vlist.add(vi19);
        vlist.add(vi20);
        vlist.add(vi21);
        vlist.add(vi22);
        vlist.add(vi23);
     /*   dbhelper.insertData(vi1);
        dbhelper.insertData(vi2);
        dbhelper.insertData(vi3);
        dbhelper.insertData(vi4);
        dbhelper.insertData(vi5);
        dbhelper.insertData(vi6);
        dbhelper.insertData(vi7);
        dbhelper.insertData(vi8);
        dbhelper.insertData(vi9);
        dbhelper.insertData(vi10);
        dbhelper.insertData(vi11);
        dbhelper.insertData(vi12);*/


    }

    private void initView() {
        setSupportActionBar(toolbar);
        setTitle(R.string.navigation_bunding);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.app_name, R.string.app_name);
        drawerlayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mviewpager.setAllowUserScrollable(false);
        mviewpager.setAdapter(new ContentViewPagerAdapter(getSupportFragmentManager(), this));

        View headerView = navigationView.getHeaderView(0);
        img = (ImageView) headerView.findViewById(R.id.img);
        Glide.with(this).load(R.drawable.p2).asBitmap().fitCenter().into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });
        tv_name = (TextView) headerView.findViewById(R.id.tv_device_name);

        SharedPreferences sp = getSharedPreferences(Const.LOGININFO, Context.MODE_WORLD_WRITEABLE);
        tv_name.setText(sp.getString(Const.LOGINNAME, ""));

        img.setOnClickListener(this);

    }

    private void setListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                hideDrawer();
                setTitle(item.getTitle());
               /* if (item.getItemId() == R.id.nav_upgrade) {
                    startActivity(new Intent(MainActivity.this, BluetoothActivity.class));
                } else {*/
                switch (item.getItemId()) {
                    case R.id.nav_main:
                        mviewpager.setCurrentItem(0, true);
                        break;
                    case R.id.nav_bunding:
                        mviewpager.setCurrentItem(1, true);
                        break;
                    case R.id.nav_message:
                        mviewpager.setCurrentItem(2, true);
                        break;
                    case R.id.nav_upgrade:
                        mviewpager.setCurrentItem(3, true);
                        break;
                    case R.id.nav_abnormal:
                        mviewpager.setCurrentItem(4, true);
                        break;
                    case R.id.nav_opinion:
                        mviewpager.setCurrentItem(5, true);
                        break;
                    default:
                        break;
                }
//                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img:
                startActivity(new Intent(MainActivity.this, PersonalDataActivity.class));
                break;
        }
    }


    private static class ContentViewPagerAdapter extends FragmentPagerAdapter {
        private Context context;
        private Fragment[] fragments = new Fragment[]{HomePageFragment.newInstance(),
                BundingFragment.newInstance(),
                MessageFragment.newInstance(),
                UpgradeFragment.newInstance(),
                FaultFragment.newInstance(),
                OpinionFragment.newInstance()};

        public ContentViewPagerAdapter(FragmentManager fm, Context mcontext) {
            super(fm);
            this.context = mcontext;
        }

        @Override
        public Fragment getItem(int position) {
//            return Fragment.instantiate(context, fragments[position].getName());
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

    }

    private void hideDrawer() {
        drawerlayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            hideDrawer();
        } else {
//            super.onBackPressed();

            App.getInstance().exit();
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        sendBroadcast(new Intent(Const.ACTION6));
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventBackgroundThread(int i) {
        if (i == 0) {
            Log.e(TAG, "onEvent");
            navigationView.setCheckedItem(R.id.nav_main);
            mviewpager.setCurrentItem(0, true);
            setTitle(R.string.navigation_main);
        }
    }

    public void showPromptDialog(String str, int type) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, type);
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

    //回调接口
    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MyTouchListener>();

    public void registerMyTouchListenters(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
