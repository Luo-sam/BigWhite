package com.webbrowser.bigwhite.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.utils.editTextUtils;
import com.webbrowser.bigwhite.utils.httpUtils;
import com.webbrowser.bigwhite.utils.stringUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class login extends BaseActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText acc,key;

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
        acc.setOnClickListener(this);

    }
    public void loginGet(View view) {
        String loginAddress = "";
        String account = acc.getText().toString().trim();
        String password = key.getText().toString().trim();
        loginToBack(loginAddress,account,password);
    }
    public void loginToBack(String address,String acc,String key) {
        if (stringUtils.isEmpty(acc)) {
            showToast("请输入账号");
            return;
        }
        if (stringUtils.isEmpty(key)) {
            showToast("请输入密码");
        }
        if(stringUtils.isEmpty(acc)&&stringUtils.isEmpty(key)){
            httpUtils.loginWithOkHttp(address, acc, key, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    /*异常处理*/
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    /*得到的服务器返回值具体内容*/
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(responseData);
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
            if(edit.isShouldHideKeyboard(v,ev)){
                Log.d("hide", String.valueOf(edit.isShouldHideKeyboard(v,ev)));
                manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
