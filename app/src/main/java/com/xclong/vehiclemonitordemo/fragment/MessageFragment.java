package com.xclong.vehiclemonitordemo.fragment;

import android.os.Bundle;
import android.util.Log;

import com.xclong.vehiclemonitordemo.R;

/**
 * Created by xcl02 on 2016/5/10.
 */
public class MessageFragment extends BaseFragment {

    private static String TAG = "MessageFragment";

    private static MessageFragment fragment;

    public synchronized static MessageFragment newInstance() {
        if (fragment == null) {
            fragment = new MessageFragment();
            Log.e(TAG, "MessageFragment : newInstance");
        }
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.frag_message);
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
