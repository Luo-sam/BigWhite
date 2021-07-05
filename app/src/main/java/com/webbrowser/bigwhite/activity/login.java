package com.webbrowser.bigwhite.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.utils.editTextUtils;
import com.webbrowser.bigwhite.utils.httpUtils;
import com.webbrowser.bigwhite.utils.stringUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class login extends BaseActivity implements View.OnClickListener, View.OnKeyListener {
    private InputMethodManager manager;
    private EditText acc,key;
    public static final String RESULT = "result";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
    }
    /*初始化界面*/
    private void initView(){
        /*给manager初始化*/
        manager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        /*初始化两个editText*/
        acc = findViewById(R.id.enter_account);
        key = findViewById(R.id.enter_key);

        acc.setOnKeyListener(this);
        acc.setOnClickListener(this);
        key.setOnClickListener(this);
        key.setOnKeyListener(this);

    }
    public void loginGet(View view) {

        String loginAddress = "http://139.196.180.89:8137/api/v1/users/login/password";
        String account = acc.getText().toString().trim();
        String password = key.getText().toString().trim();
        if(!password.equals("")){
            password =  httpUtils.encrypt(password);
        }
        loginToBack(loginAddress,account,password);
    }
    public void loginToBack(String address,String acc,String key) {

        if (stringUtils.isEmpty(acc)) {
            showToast("请输入账号");
            return;
        }
        if (stringUtils.isEmpty(key)) {
            showToast("请输入密码");
            return;
        }
        if(!stringUtils.isEmpty(acc)&&!stringUtils.isEmpty(key)){
            httpUtils.loginWithOkHttp(address, acc, key, new Callback() {
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
                    final String responseHeader = response.header("Authorization");

                    runOnUiThread(() -> {
                        Log.d("his", responseData);
                        if (responseHeader == null) {
                            showToast("账号密码错误");
                        }else{
                            showToast("登录成功");
                            saveToSp("token",responseHeader);
                            toActivity(MainActivity.class);
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

        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            editTextUtils edit = new editTextUtils();
            if(acc.hasFocus()||key.hasFocus()){
                if(edit.isShouldHideKeyboard(v,ev)){
                    Log.d("hide", String.valueOf(edit.isShouldHideKeyboard(v,ev)));
                    manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    acc.clearFocus();
                    key.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        int id = v.getId();

        if (id == R.id.enter_key) {
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                loginGet(v);
                manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return false;
    }
}
