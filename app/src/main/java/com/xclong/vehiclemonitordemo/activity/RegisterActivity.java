package com.xclong.vehiclemonitordemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xclong.vehiclemonitordemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by xcl02 on 2016/5/26.
 */
public class RegisterActivity extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.iv_register_back)
    ImageView ivRegisterBack;
    @Bind(R.id.et_register_name)
    EditText etRegisterName;
    @Bind(R.id.et_register_pass)
    EditText etRegisterPass;
    @Bind(R.id.et_register_repass)
    EditText etRegisterRepass;
    @Bind(R.id.cb_agree_protocol)
    CheckBox cbAgreeProtocol;
    @Bind(R.id.tv_protocol)
    TextView tvProtocol;
    @Bind(R.id.btn_register)
    Button btnRegister;

    private boolean nameNotNull, passNotNull, repassNotNull, hasAgree;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_register);
        ButterKnife.bind(this);

        btnRegister.setClickable(false);
        etRegisterName.addTextChangedListener(this);
        etRegisterPass.addTextChangedListener(this);
        etRegisterRepass.addTextChangedListener(this);
        cbAgreeProtocol.setOnCheckedChangeListener(this);

    }

    @OnClick(R.id.iv_register_back)
    public void back() {
        String name = etRegisterName.getText().toString();
        Intent intent = new Intent();
        if (name != null && name.trim().length() > 0) {
            intent.putExtra("name", name);
        } else {
            intent.putExtra("name", "");
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.btn_register)
    public void register() {
        String name = etRegisterName.getText().toString();
        String pass = etRegisterPass.getText().toString();
        String repass = etRegisterRepass.getText().toString();

        RequestParams params = new RequestParams();

        if (repass.equals(pass)) {
            params.put("name", name);
            params.put("pass", pass);
            invokeWS(params);
        } else {
            Toast.makeText(this, "密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
        }
    }

    private void invokeWS(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://172.16.7.4:8080/jeeplus/a/login?__ajax", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    if (obj.getBoolean("status")) {
                        //TODO 注册成功
                        back();
                    } else {
                        //TODO 注册失败
                        Toast.makeText(RegisterActivity.this, obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       /* if (etRegisterName.getText().length()>0) nameNotNull=true;
        else nameNotNull = false;

        if (etRegisterPass.getText().length()>0) passNotNull=true;
        else passNotNull = false;

        if (etRegisterRepass.getText().length()>0) repassNotNull=true;
        else repassNotNull = false;*/
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etRegisterName.getText().length() > 0) nameNotNull = true;
        else nameNotNull = false;

        if (etRegisterPass.getText().length() > 0) passNotNull = true;
        else passNotNull = false;

        if (etRegisterRepass.getText().length() > 0) repassNotNull = true;
        else repassNotNull = false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && nameNotNull && passNotNull && repassNotNull) {
            btnRegister.setClickable(true);
        }
    }
}
