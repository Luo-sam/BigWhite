package com.webbrowser.bigwhite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.webbrowser.bigwhite.View.adapter.SectionsPageAdapter;
import com.webbrowser.bigwhite.View.fragment.SearchFragment;
import com.webbrowser.bigwhite.activity.popWindows.myPopWin;
import com.webbrowser.bigwhite.activity.bookmark;
import com.webbrowser.bigwhite.activity.chooseLoginRegister;
import com.webbrowser.bigwhite.activity.history;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /*log的标签*/
//    private static final String TAG = "main";

    private Context mContext;
    /*个人栏弹窗*/
    private myPopWin myPopWin;

    /*搜索栏*/
    private ViewPager viewPager;
    public List<Fragment> fragments;

    /*onCreate方法*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止底部导航栏上移
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        /*对viewPager进行初始化*/
        viewPager = findViewById(R.id.viewPager);
        initViewPager();
        /*对mContext初始化*/
        mContext = MainActivity.this;
        /*底部导航栏的id获取*/
        ImageView backleft = findViewById(R.id.backLeft);
        ImageView backright = findViewById(R.id.backRight);
        ImageView home = findViewById(R.id.home);
        ImageView morewindows = findViewById(R.id.more_windows);
        ImageView my = findViewById(R.id.my);
        /*绑定点击事件*/
        backleft.setOnClickListener(this);
        backright.setOnClickListener(this);
        home.setOnClickListener(this);
        morewindows.setOnClickListener(this);
        my.setOnClickListener(this);
    }

    private void initViewPager() {
        /*配置viewPager*/
        /*可以有多少个搜索fragment*/
//        viewPager.setOffscreenPageLimit(10);
        fragments = new ArrayList<>();
        fragments.add(new SearchFragment());

        viewPager.setAdapter
                (new SectionsPageAdapter(getSupportFragmentManager(),fragments));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.backLeft){
            SearchFragment.goToBack();
        }else if(id == R.id.backRight){
            SearchFragment.goToForward();
        }else if(id == R.id.home){
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(id == R.id.more_windows){
            Toast.makeText(mContext, "功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.my){
            showPopFormBottom();
        }
    }






//    @Override
//    public void onBackPressed(){
//        //返回上一页
//        if(webView.canGoBack()){
//            webView.goBack();
//        }else{
//            if((System.currentTimeMillis()-exitTime)>PRESS_BACK_EXIT_GAP){
//                //点击两次就退出
//                Toast.makeText(mContext,"再按一次退出浏览器",Toast.LENGTH_SHORT).show();
//                exitTime=System.currentTimeMillis();
//            }else {
//                super.onBackPressed();
//            }
//        }
//    }

    /*my弹窗*/
    public void showPopFormBottom(){

        myPopWin = new myPopWin(this,onClickListener);
        myPopWin.showAtLocation(findViewById(R.id.my), Gravity.CENTER,0,0);
    }
    private final View.OnClickListener onClickListener = v -> {
        int id = v.getId();
        if(id == R.id.login){
            myPopWin.dismiss();
            startActivity(new Intent(MainActivity.this, chooseLoginRegister.class));
        }else if(id == R.id.add_bookmark){
            Toast.makeText(mContext, "添加书签功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.bookmark){
            startActivity(new Intent(MainActivity.this, bookmark.class));
        }else if(id == R.id.history){
            myPopWin.dismiss();
            startActivity(new Intent(MainActivity.this, history.class));
        }else if(id == R.id.exit){
            Toast.makeText(mContext, "退出登录功能开发中", Toast.LENGTH_SHORT).show();
        }
    };


}