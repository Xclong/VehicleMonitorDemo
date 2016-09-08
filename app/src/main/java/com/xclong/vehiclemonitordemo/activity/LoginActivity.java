package com.xclong.vehiclemonitordemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.Util;
import com.xclong.vehiclemonitordemo.application.App;
import com.xclong.vehiclemonitordemo.constant.BluetoothMsg;
import com.xclong.vehiclemonitordemo.constant.Const;
import com.xclong.vehiclemonitordemo.constant.LoginUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

/**
 * Created by xcl02 on 2016/5/17.
 */
public class LoginActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Bind(R.id.login_img_photo)
    ImageView loginImgPhoto;
    @Bind(R.id.login_view_line)
    View loginViewLine;
    @Bind(R.id.login_img_slide)
    ImageView loginImgSlide;
    @Bind(R.id.login_edit_username)
    EditText loginEditUsername;
    @Bind(R.id.login_btn_clear_username)
    Button loginBtnClearUsername;
    @Bind(R.id.login_edit_password)
    EditText loginEditPassword;
    @Bind(R.id.login_btn_clear_password)
    Button loginBtnClearPassword;
    @Bind(R.id.tv_forget_pass)
    TextView tv_forget_pass;
    @Bind(R.id.tv_register)
    TextView tv_register;

    private SweetAlertDialog mLoadingDialog;

    private Handler moHandler;
    private boolean mbIsSlidingBack;
    private int miSliderMinX, miSliderMaxX, miLastX;

    public static final int PASSWORD_MIN_LENGTH = 3;
    public static final int ADMIN_LOGIN_SUCCESS = 1; // 管理员登录成功
    public static final int NORMAL_LOGIN_SUCCESS = 2; // 普通登录成功
    public static final int LOGIN_FAILED = 3; // 登录失败
    public static final int LOGIN_SLIDER_TIP = 4; // 登录页面滑块向左自动滑动
    public static final int LOGIN_PHOTO_ROTATE_TIP = 5; // 登录页面加载图片转动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_login);
        ButterKnife.bind(this);

        App.getInstance().addActivity(this);

        mbIsSlidingBack = false;
        miLastX = 0;
        miSliderMinX = 0;
        miSliderMaxX = 0;

        setHandler();
        setEventListeners();
    }

    private void setHandler() {
        moHandler = new Handler() {
            @Override
            public void handleMessage(Message poMsg) {
                switch (poMsg.what) {
                    case NORMAL_LOGIN_SUCCESS:
                    case ADMIN_LOGIN_SUCCESS:
                        dismissDialog();
                        slideBack();
                        if (BluetoothMsg.socket == null) {
                            startActivity(new Intent(LoginActivity.this, BluetoothActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        break;
                    /*case NORMAL_LOGIN_SUCCESS:
                        dismissDialog();
                        slideBack();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        break;*/
                    case LOGIN_FAILED:
                        dismissDialog();
                        slideBack();
                        loginEditPassword.setText("");
                        break;
                    case LOGIN_SLIDER_TIP:
                        loginImgSlide.layout(miLastX, loginImgSlide.getTop(), miLastX
                                + loginImgSlide.getWidth(), loginImgSlide.getTop()
                                + loginImgSlide.getHeight());
                        break;
                    case LOGIN_PHOTO_ROTATE_TIP:
                        loginImgPhoto.setImageBitmap((Bitmap) poMsg.obj);
                        break;
                }
            }
        };
    }

    // 触摸登录界面收回键盘
    public boolean onTouchEvent(android.view.MotionEvent poEvent) {
        try {
            InputMethodManager loInputMgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return loInputMgr.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);
        } catch (Exception e) {
            return false;
        }
    }

    private void setEventListeners() {
        loginEditUsername.addTextChangedListener(new OnEditUsername());
        loginEditPassword.addTextChangedListener(new OnEditPassword());
        loginBtnClearUsername.setOnClickListener(new OnClearEditText());
        loginBtnClearPassword.setOnClickListener(new OnClearEditText());
        loginImgSlide.setOnClickListener(new OnSliderClicked());
        loginImgSlide.setOnTouchListener(new OnSliderDragged());
//		moBtnRegister.setOnClickListener(new OnRegister());
//		moBtnTraveller.setOnClickListener(new OnTravell());
    }

    // 处理用户名编辑事件
    private class OnEditUsername implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // 1. 处理右侧清除按钮隐藏/显示
            if (s.length() >= 1)
                loginBtnClearUsername.setVisibility(View.VISIBLE);
            else
                loginBtnClearUsername.setVisibility(View.GONE);

            // 2. 处理滑块是否可滑动
            initWidgetForCanLogin();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    // 处理密码编辑事件
    private class OnEditPassword implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // 1. 处理右侧清空按钮显示/隐藏
            if (s.length() >= 1)
                loginBtnClearPassword.setVisibility(View.VISIBLE);
            else if (s.length() == 0
                    && loginBtnClearPassword.getVisibility() == View.VISIBLE)
                loginBtnClearPassword.setVisibility(View.GONE);

            // 2. 处理滑块是否可滑动
            initWidgetForCanLogin();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }

    // 清除输入控件中的文字的事件处理
    private class OnClearEditText implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_clear_username:
                    // 如果清除帐号则密码一并清除
                    loginEditUsername.setText("");
                    loginEditPassword.setText("");
                    break;
                case R.id.login_btn_clear_password:
                    // 清除已输密码
                    loginEditPassword.setText("");
                    break;
                default:
                    break;
            }
        }
    }

    // 滑动图标点击事件
    private class OnSliderClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 如果不符合登录条件 则跳转到忘记密码界面
            if (!canLogin()) {

            }

        }
    }

    // 滑动图标滑动事件
    private class OnSliderDragged implements View.OnTouchListener {
        @SuppressWarnings("unused")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            LoginUtils.closeKeybord(loginEditUsername, LoginActivity.this);
            LoginUtils.closeKeybord(loginEditPassword, LoginActivity.this);
            if (canLogin() && !mbIsSlidingBack) {
                if (miSliderMaxX == 0) {
                    miSliderMinX = loginViewLine.getLeft()
                            - loginImgSlide.getWidth() / 2;
                    miSliderMaxX = loginViewLine.getRight()
                            - loginImgSlide.getWidth() / 2;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        miLastX = (int) event.getRawX();
                    case MotionEvent.ACTION_MOVE:
                        int liX = (int) event.getRawX();
                        if (liX > miSliderMaxX)
                            liX = miSliderMaxX;
                        else if (liX < miSliderMinX)
                            liX = miSliderMinX;
                        if (liX != miLastX) {
                            loginImgSlide.layout(liX, loginImgSlide.getTop(), liX
                                    + loginImgSlide.getWidth(), loginImgSlide.getTop()
                                    + loginImgSlide.getHeight());
                            miLastX = liX;
                            if (miLastX == miSliderMaxX) {
                                Log.e(TAG, "showLoadingDialog");
                                showLoadingDialog();
                                try {
                                    new Thread().sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                int i = (int) (Math.random() * 10) % 2;
                                Log.e(TAG, "i = " + i);
                                changeDialogType(i == 0);
//                                logining();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((int) event.getRawX() < miSliderMaxX)
                            slideBack();
                        break;
                }
            }
            return false;
        }
    }

    // 根据是否可以登录，初始化相关控件
    private void initWidgetForCanLogin() {
        if (canLogin())
            loginImgSlide.setImageResource(R.drawable.ic_arrow_circle_right);
        else
            loginImgSlide.setImageResource(R.drawable.ic_ask_circle);
    }

    // 判断当前用户输入是否合法，是否可以登录
    private boolean canLogin() {
        Editable loUsername = loginEditUsername.getText();
        Editable loPassword = loginEditPassword.getText();
        return !LoginUtils.isStrEmpty(loUsername)
                && loPassword.length() >= PASSWORD_MIN_LENGTH;
    }

    // 滑块向左自动滑动
    private void slideBack() {
        new Thread() {
            @Override
            public void run() {
                mbIsSlidingBack = true;
                while (miLastX > miSliderMinX) {
                    miLastX -= 5;
                    if (miLastX < miSliderMinX)
                        miLastX = miSliderMinX;
                    Message loMsg = new Message();
                    loMsg.what = LOGIN_SLIDER_TIP;
                    moHandler.sendMessage(loMsg);

                }
                mbIsSlidingBack = false;
            }
        }.start();
    }

    private void logining() {
        String name = loginEditUsername.getText().toString();
        String pass = loginEditPassword.getText().toString();
        RequestParams params = new RequestParams();
        if (Util.isNotNull(name) && Util.isNotNull(pass)) {
            params.put("username", name);
            params.put("password", pass);
            params.put("mobileLogin", true);
            // Invoke RESTful Web Service with Http parameters
            invokeWS(params);
        }
    }

    private void invokeWS(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Const.LOGINURL_POST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    Log.e(TAG, object.toString());
                    if (object.getBoolean("success")) {
                        Log.e(TAG, "JSESSIONID =" + object.getString("JSESSIONID"));
                        SharedPreferences sp = getSharedPreferences(Const.LOGININFO, Context.MODE_WORLD_WRITEABLE); //私有数据)
                        sp.edit().putString("JSESSIONID", object.getString("JSESSIONID")).commit();
                        changeDialogType(true);
                    } else {
                        changeDialogType(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dismissDialog();

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void loginSuccess() {
        SharedPreferences sp = getSharedPreferences(Const.LOGININFO, Context.MODE_WORLD_WRITEABLE); //私有数据
        Message mesg = Message.obtain();
        if (loginEditUsername.getText().toString().equals("admin") || loginEditUsername.getText().toString().equals("Admin")) {
            sp.edit().putString(Const.LOGINSTATE, Const.ADMINLOGIN).commit();
            mesg.what = ADMIN_LOGIN_SUCCESS;
        } else {
            sp.edit().putString(Const.LOGINSTATE, Const.NORMALLOGIN).commit();
            mesg.what = NORMAL_LOGIN_SUCCESS;
        }
        sp.edit().putString(Const.LOGINNAME, loginEditUsername.getText().toString()).commit();
        moHandler.sendMessageDelayed(mesg, 300);
    }

    private void loginFailed() {
        Message mesg = Message.obtain();
        mesg.what = LOGIN_FAILED;
        moHandler.sendMessageDelayed(mesg, 1000);
    }

    private void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        } else {
            mLoadingDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        }
        mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setTitleText("登陆进行中...");
        mLoadingDialog.show();
    }

    private void dismissDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    private void changeDialogType(boolean success) {
        if (mLoadingDialog != null) {
            if (success) {
                Log.e(TAG, "login success");
                mLoadingDialog.setTitleText("登陆成功");
                mLoadingDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                loginSuccess();
            } else {
                Log.e(TAG, "login failed");
                mLoadingDialog.setTitleText("请检查账号密码是否正确");
                mLoadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                loginFailed();
            }
        }
    }

    @OnClick(R.id.tv_register)
    public void jump() {
        startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_OK:
                String name = (String) data.getExtras().get("name");
                Log.e(TAG, "name = " + name);
                loginEditUsername.setText(name);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        App.getInstance().exit();
    }
}
