package com.webbrowser.bigwhite.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.utils.editTextUtils;

public class register extends AppCompatActivity {
    private InputMethodManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
    }

    public void initView(){
        /*给manager初始化*/
        manager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void back(View view) {
        finish();
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
