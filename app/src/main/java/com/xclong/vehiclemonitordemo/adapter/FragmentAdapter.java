package com.xclong.vehiclemonitordemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by xcl02 on 2015/12/20.
 */
public abstract class FragmentAdapter extends FragmentStatePagerAdapter {

    //    List<String> mTitles;
    List<Fragment> mFragments;

    public FragmentAdapter(FragmentManager fm/*, List<String> titles*/, List<Fragment> framgents) {
        super(fm);
//        mTitles = titles;
        mFragments = framgents;
    }

    @Override
    public Fragment getItem(int position) {
        notifyBar(position);
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return mTitles.get(position);
        return "";
    }

    protected abstract void notifyBar(int position);
}
