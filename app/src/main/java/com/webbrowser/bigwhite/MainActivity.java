package com.webbrowser.bigwhite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.webbrowser.bigwhite.activity.TakePhotoPopWin;
import com.webbrowser.bigwhite.activity.bookmark;
import com.webbrowser.bigwhite.activity.history;
import com.webbrowser.bigwhite.activity.login;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /*log的标签*/
    private static final String TAG = "main";
    private Context mContext;

    /*onCreate方法*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*打印成功显示在logcat*/
        Log.d(TAG, "onCreate: 创建成功");
        initView();
    }
    private void initView(){
        /*对mContext初始化*/
        mContext = MainActivity.this;
        /*底部导航栏的id获取*/
        ImageView backleft, backright, home, morewindows, my;
        backleft = findViewById(R.id.backLeft);
        backright = findViewById(R.id.backRight);
        home = findViewById(R.id.home);
        morewindows = findViewById(R.id.more_windows);
        my = findViewById(R.id.my);
        /*绑定点击事件*/
        backleft.setOnClickListener(this);
        backright.setOnClickListener(this);
        home.setOnClickListener(this);
        morewindows.setOnClickListener(this);
        my.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.backLeft){
            Toast.makeText(mContext, "功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.backRight){
            Toast.makeText(mContext, "功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.home){
            Toast.makeText(mContext, "功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.more_windows){
            Toast.makeText(mContext, "功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.my){
            showPopFormBottom();
        }

    }

    public void showPopFormBottom(){
        TakePhotoPopWin takePhotoPopWin = new TakePhotoPopWin(this,onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.my), Gravity.CENTER,0,0);
    }
    private final View.OnClickListener onClickListener = v -> {
        int id = v.getId();
        if(id == R.id.login){
            startActivity(new Intent(MainActivity.this, login.class));
        }else if(id == R.id.add_bookmark){
            Toast.makeText(mContext, "添加书签功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.bookmark){
            startActivity(new Intent(MainActivity.this, bookmark.class));
        }else if(id == R.id.history){
            startActivity(new Intent(MainActivity.this, history.class));
        }else if(id == R.id.exit){
            Toast.makeText(mContext, "退出登录功能开发中", Toast.LENGTH_SHORT).show();
        }
    };
}