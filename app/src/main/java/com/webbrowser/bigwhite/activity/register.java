package com.webbrowser.bigwhite.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.webbrowser.bigwhite.Model.data.simpleResponse;
import com.webbrowser.bigwhite.Model.data.verify_res;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.utils.editTextUtils;
import com.webbrowser.bigwhite.utils.httpUtils;
import com.webbrowser.bigwhite.utils.stringUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class register extends BaseActivity implements View.OnKeyListener, View.OnClickListener {
    private InputMethodManager manager;
    private EditText acc, key, email, verify;
    public static final String RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
    }

    /*初始化界面*/
    private void initView() {
        /*给manager初始化*/
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        /*初始化4个editText*/
        acc = findViewById(R.id.register_acc);
        key = findViewById(R.id.register_pwd);
        email = findViewById(R.id.register_email);
        verify = findViewById(R.id.register_verify);

        email.setOnKeyListener(this);
        email.setOnClickListener(this);
        verify.setOnClickListener(this);
        verify.setOnKeyListener(this);
        acc.setOnKeyListener(this);
        acc.setOnClickListener(this);
        key.setOnClickListener(this);
        key.setOnKeyListener(this);
    }

    public void verifyGet(View view) {
        String emailAddress = email.getText().toString().trim();
        String verifyAddress = "http://139.196.180.89:8137/api/v1/users/verifyCode/register?email=" + emailAddress;
        showToast(emailAddress);
        verifyToBack(verifyAddress, emailAddress);
    }

    public void verifyToBack(String address, String emailAddress) {
        if (stringUtils.isEmpty(emailAddress)) {
            showToast("请输入邮箱");
        } else {
            httpUtils.verifyWithOkHttp(address, emailAddress, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    /*异常处理*/
                    runOnUiThread(() -> showToast("网络错误"));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    /*得到的服务器返回值具体内容*/
                    assert response.body() != null;
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        Log.d("verify_response", responseData);
                        Gson gson = new Gson();
                        verify_res res = gson.fromJson(responseData, verify_res.class);

                        if (res.getState().getCode() == 0) {
                            showToast("成功发送到邮箱");
                        } else if (res.getState().getMsg().equals("已发送验证码")) {
                            showToast("已发送验证码");
                        } else {
                            showToast("无效的地址");
                        }
                    });
                }
            });
        }
    }

    public void registerGet(View view) {

        String registerAddress = "http://139.196.180.89:8137/api/v1/users";
        String account = acc.getText().toString().trim();
        String password = key.getText().toString().trim();
        String emailAddress = email.getText().toString().trim();
        String verifyCode = verify.getText().toString().trim();


        if (!password.equals("")) {
            password = httpUtils.encrypt(password);
        }
        registerToBack(registerAddress, emailAddress, verifyCode, account, password);
    }

    public void registerToBack(String address, String email, String verify, String acc, String key) {

        if (stringUtils.isEmpty(email)) {
            showToast("请输入邮箱");
            return;
        }
        if (stringUtils.isEmpty(verify)) {
            showToast("请输入邮箱验证码");
            return;
        }
        if (stringUtils.isEmpty(acc)) {
            showToast("请输入账号");
            return;
        }
        if (stringUtils.isEmpty(key)) {
            showToast("请输入密码");
            return;
        }
        if (!stringUtils.isEmpty(acc) && !stringUtils.isEmpty(key) && !stringUtils.isEmpty(verify) && !stringUtils.isEmpty(email)) {
            httpUtils.registerWithOkHttp(address, email, verify, acc, key, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    /*异常处理*/
                    showToast("网络错误");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    /*得到的服务器返回值具体内容*/
                    assert response.body() != null;
                    final String responseData = response.body().string();
//                    final String responseHeader = response.header("Authorization");

                    runOnUiThread(() -> {

                        Log.d("register_response", responseData);
                        Gson gson = new Gson();
                        simpleResponse res = gson.fromJson(responseData, simpleResponse.class);

                        if (res.getState().getCode() == 0) {
                            showToast("成功注册");
                            finish();
                        } else if (res.getState().getMsg().equals("用户名已被占用")) {
                            showToast("用户名已被占用");
                        } else if (res.getState().getMsg().equals("验证码不正确")) {
                            showToast("验证码不正确");
                        } else if (res.getState().getMsg().equals("addUser.username: 长度需要在4和12之间")) {
                            showToast("用户名长度需要在4和12之间");
                        } else if (res.getState().getMsg().equals("密码须包含数字和字母，长度至少为6位")) {
                            showToast("密码须包含数字和字母，长度至少为6位");
                        }

                    });

                }
            });
        }

    }


    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            editTextUtils edit = new editTextUtils();
            if (acc.hasFocus() || key.hasFocus() || email.hasFocus() || verify.hasFocus()) {
                if (edit.isShouldHideKeyboard(v, ev)) {
                    Log.d("hide", String.valueOf(edit.isShouldHideKeyboard(v, ev)));
                    manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    acc.clearFocus();
                    key.clearFocus();
                    email.clearFocus();
                    verify.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        int id = v.getId();
        if (id == R.id.enter_key) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                registerGet(v);
                manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return false;
    }
}
