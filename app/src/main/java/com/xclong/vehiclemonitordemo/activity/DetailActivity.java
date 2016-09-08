package com.xclong.vehiclemonitordemo.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gigamole.library.NavigationTabBar;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.adapter.FragmentAdapter;
import com.xclong.vehiclemonitordemo.constant.VehicledInfo;
import com.xclong.vehiclemonitordemo.fragment.CurveChartFragment;
import com.xclong.vehiclemonitordemo.fragment.ListDataFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcl02 on 2016/5/9.
 */
public class DetailActivity extends AppCompatActivity {

    public String TAG = this.getClass().getSimpleName();
    private Toolbar toolbar;
    private AppBarLayout appbarlayout;
    //    private TabLayout tablayout;
    private ViewPager viewpager;

    private VehicledInfo vehicledInfo;
    private List<String> titles;
    private List<Fragment> fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_detail);

        vehicledInfo = (VehicledInfo) this.getIntent().getSerializableExtra("VehicledInfo");
        if (vehicledInfo.getCar_number() != null) {
            setTitle(vehicledInfo.getCar_number());
        }

        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
//        tablayout = (TabLayout) findViewById(R.id.detail_tab_layout);
        viewpager = (ViewPager) findViewById(R.id.detail_viewpager);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_action_hardware_keyboard_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.navigationtabbar);
        /*titles = new ArrayList<>();
        titles.add("列表");
        titles.add("曲线");
        fragments = new ArrayList<>();
        fragments.add(new ListDataFragment(vehicledInfo));
        fragments.add(new CurveChartFragment(vehicledInfo.getCar_number()));

        for (int i = 0; i < titles.size(); i++) {
            tablayout.addTab(tablayout.newTab().setText(titles.get(i)));
        }

        FragmentAdapter fragmentAdpater = new FragmentAdapter(getSupportFragmentManager(), titles, fragments);
        viewpager.setAdapter(fragmentAdpater);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabsFromPagerAdapter(fragmentAdpater);*/

        fragments = new ArrayList<>();
        fragments.add(ListDataFragment.newInstance(vehicledInfo));
        fragments.add(CurveChartFragment.newInstance(vehicledInfo.getCar_number()));
        FragmentAdapter fragmentAdpater = new FragmentAdapter(getSupportFragmentManager(), fragments) {
            @Override
            protected void notifyBar(int position) {
                navigationTabBar.setModelIndex(position);
            }
        };
        viewpager.setAdapter(fragmentAdpater);
        viewpager.setCurrentItem(0, true);

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_third), getResources().getColor(R.color.colorPrimary)));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_seventh), getResources().getColor(R.color.colorPrimary)));
        navigationTabBar.setModels(models);
        navigationTabBar.setModelIndex(0, true);
        navigationTabBar.setViewPager(viewpager);
    }
}
