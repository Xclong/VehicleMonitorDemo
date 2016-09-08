package com.xclong.vehiclemonitordemo.fragment;

import android.os.Bundle;

import com.xclong.vehiclemonitordemo.R;

/**
 * Created by xcl02 on 2016/5/10.
 */
public class OpinionFragment extends BaseFragment {

    public static OpinionFragment newInstance() {
        OpinionFragment fragment = new OpinionFragment();
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.frag_opinion);
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
