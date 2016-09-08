package com.xclong.vehiclemonitordemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.Util;
import com.xclong.vehiclemonitordemo.application.App;
import com.xclong.vehiclemonitordemo.constant.Const;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by xcl02 on 2016/3/3.
 */
public class PersonalDataActivity extends AppCompatActivity {

    public String TAG;
    @Bind(R.id.img_profile)
    ImageView imgProfile;
    @Bind(R.id.cv_images)
    CardView cvImages;
    @Bind(R.id.et_cv_name)
    EditText etCvName;
    @Bind(R.id.cv_name)
    CardView cvName;
    @Bind(R.id.et_cv_email)
    EditText etCvEmail;
    @Bind(R.id.cv_email)
    CardView cvEmail;
    @Bind(R.id.et_cv_phone)
    EditText etCvPhone;
    @Bind(R.id.cv_phone)
    CardView cvPhone;
    @Bind(R.id.et_cv_mobilephone)
    EditText etCvMobilephone;
    @Bind(R.id.cv_mobilephone)
    CardView cvMobilephone;
    @Bind(R.id.cv_cancellation)
    CardView cvCancellation;
    @Bind(R.id.btn_commit)
    Button btn_commit;

    private boolean isEditState = false;
    private ActionBar actionBar;


    private String name, email, phone, mobilephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_personal_data);
        actionBar = getSupportActionBar();

        TAG = this.getClass().getSimpleName();
        App.getInstance().addActivity(this);
        ButterKnife.bind(this);
        hideView();

        initData();
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences(Const.LOGININFO, Context.MODE_WORLD_WRITEABLE); //私有数据)
        String JSESSIONID = sp.getString("JSESSIONID", "");
        Log.e(TAG, "JSESSIONID =" + JSESSIONID);
        if (JSESSIONID != null && JSESSIONID.trim().length() > 0) {
            String url = "http://localhost:8080/jeeplus/a/sys/user/infoData;JSESSIONID=" + JSESSIONID + "?__ajax=true&mobileLogin=true";
            Log.e(TAG, "url = " + url);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject obj = new JSONObject(new String(responseBody));
                        name = obj.getString("name");
                        email = obj.getString("email");
                        phone = obj.getString("phone");
                        mobilephone = obj.getString("mobile");

                        Message msg = Message.obtain();
                        msg.what = 0x01;
                        handler.sendMessage(msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

    }

    private void hideView() {
        etCvName.setFocusable(false);
        etCvName.setFocusableInTouchMode(false);

        etCvEmail.setFocusable(false);
        etCvEmail.setFocusableInTouchMode(false);

        etCvPhone.setFocusable(false);
        etCvPhone.setFocusableInTouchMode(false);

        etCvMobilephone.setFocusable(false);
        etCvMobilephone.setFocusableInTouchMode(false);

        btn_commit.setVisibility(View.GONE);
        cvImages.setClickable(false);
        cvName.setClickable(false);
        cvEmail.setClickable(false);
        cvPhone.setClickable(false);
        cvMobilephone.setClickable(false);
    }

    private void showView() {
        etCvName.setFocusable(true);
        etCvName.setFocusableInTouchMode(true);

        etCvEmail.setFocusable(true);
        etCvEmail.setFocusableInTouchMode(true);

        etCvPhone.setFocusable(true);
        etCvPhone.setFocusableInTouchMode(true);

        etCvMobilephone.setFocusable(true);
        etCvMobilephone.setFocusableInTouchMode(true);

        btn_commit.setVisibility(View.VISIBLE);

        cvImages.setClickable(true);
        cvName.setClickable(true);
        cvEmail.setClickable(true);
        cvPhone.setClickable(true);
        cvMobilephone.setClickable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_private, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                if (!isEditState) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_action_content_clear);
                    showView();
                }
                break;

            case android.R.id.home:
                hideView();
                isEditState = false;
                actionBar.setDisplayHomeAsUpEnabled(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.cv_images)
    public void changeImage() {

    }

    @OnClick(R.id.cv_name)
    public void changeName() {
        etCvName.requestFocus();
    }

    @OnClick(R.id.cv_email)
    public void changeEmail() {
        etCvEmail.requestFocus();
    }

    @OnClick(R.id.cv_phone)
    public void changePhone() {
        etCvPhone.requestFocus();
    }

    @OnClick(R.id.cv_mobilephone)
    public void changeMobilePhone() {
        etCvMobilephone.requestFocus();
    }

    @OnClick(R.id.cv_cancellation)
    public void cancellation() {
        if (Util.isServiceRunning(this, "com.xclong.vehiclemonitordemo.service.CommunicationService")) {
            Intent intent = new Intent();
            intent.setAction("com.xclong.vehiclemonitordemo.COMMUNICATION_SERVICE");
            intent.setPackage(getPackageName());
            stopService(intent);
        }
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("确认注销后重新登录？")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        SharedPreferences sp = getSharedPreferences(Const.LOGININFO, Context.MODE_WORLD_WRITEABLE); //私有数据
                        sp.edit().putString(Const.LOGINSTATE, Const.NOTLOGIN).commit();
                        sp.edit().putString(Const.LOGINNAME, "").commit();

                        Intent intent = new Intent(PersonalDataActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        App.getInstance().exit();
                    }
                }).build();
        dialog.show();
    }

    @OnClick(R.id.btn_commit)
    public void commit() {
        boolean isRunning = Util.isServiceRunning(this, "com.xclong.vehiclemonitordemo.service.CommunicationService");
        Log.e(TAG, isRunning ? "服务正在运行" : "服务没有运行");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    etCvName.setText(name);
                    etCvEmail.setText(email);
                    etCvPhone.setText(phone);
                    etCvMobilephone.setText(mobilephone);
                    break;
            }
        }
    };
}
