package com.xclong.vehiclemonitordemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.xclong.vehiclemonitordemo.activity.BaseActivity;
import com.xclong.vehiclemonitordemo.application.App;

/**
 * Created by xcl02 on 2016/5/10.
 */
public abstract class BaseFragment extends Fragment {

    protected boolean isVisible;
    protected View mContentView;
    protected App mApp;

//    private ActionBarDrawerToggle mDrawerToggle;

    protected BaseActivity mainActivity;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mApp = App.getInstance();
//        mainActivity = (BaseActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        return mContentView;
    }

    //TODO 初始化控件
    protected abstract void initView(Bundle savedInstanceState);

    //TODO 给控件添加事件监听器
    protected abstract void setListener();

    //TODO 处理业务逻辑、恢复状态等操作
    protected abstract void processLogic(Bundle savedInstanceState);

    //TODO 当Fragment对用户可见时，调用该方法
    protected void onVisible() {
        lazyLoad();
    }

    protected void setContentView(@LayoutRes int layoutResID) {
        mContentView = LayoutInflater.from(mApp).inflate(layoutResID, null);
    }

    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mContentView.findViewById(id);
    }

    protected abstract void lazyLoad();

    protected void onInVisible() {

    }

    public void showPromptDialog(String str, int mode) {
        mainActivity.showLoadingDialog(str, mode);
    }

    public void dismissPromptDialog() {
        if (isVisible())
            mainActivity.dismissLoadingDialog();
    }

    public void closeInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
