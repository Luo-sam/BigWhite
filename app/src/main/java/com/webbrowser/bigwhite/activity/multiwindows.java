package com.webbrowser.bigwhite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.adapter.SectionsPageAdapter;
import com.webbrowser.bigwhite.View.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class multiwindows extends AppCompatActivity implements View.OnClickListener {

    /*搜索栏*/
    private ViewPager viewPager;
    public List<Fragment> fragments;
    private int currentItem = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止底部导航栏上移
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.multiwindows);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Bundle bundle = getIntent().getExtras();
//        int fragment_id = bundle.getInt("fragment_id");
        viewPager.setCurrentItem(currentItem);
        //刷新数据适配器
        viewPager.getAdapter().notifyDataSetChanged();
    }

    private void initView(){
        /*对viewPager进行初始化*/
        viewPager = findViewById(R.id.viewPager2);


        /*底部导航栏的id获取*/
        ImageView additon = findViewById(R.id.add_win);
        /*绑定点击事件*/
        additon.setOnClickListener(this);

        initViewPager();
        scaleWindow();
    }

    private void initViewPager() {
        /*配置viewPager*/
        /*可以有多少个搜索fragment*/
//        viewPager.setOffscreenPageLimit(10);

//        Bundle bundle = getIntent().getExtras();
//        int itemId = bundle.getInt("itemId");

        fragments = new ArrayList<>();


        //viewpager获取后没有内容
//        setContentView(R.layout.activity_main);
//
//        ViewPager viewPager2 = findViewById(R.id.viewPager);
//        int count = viewPager2.getAdapter().getCount();
//
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + findViewById(R.id.viewPager) + ":0");
//
//        setContentView(R.layout.multiwindows);

        viewPager.setAdapter
                (new SectionsPageAdapter(getSupportFragmentManager(), this.fragments));
    }

    private void scaleWindow() {

        /** 设置缩放动画 */
        final ScaleAnimation animation = new ScaleAnimation(1f, 0.8f, 1f, 0.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);// 从相对于自身0.5倍的位置开始缩放，也就是从控件的位置缩放
        animation.setDuration(2000);//设置动画持续时间

        /** 常用方法 */
        //animation.setRepeatCount(int repeatCount);//设置重复次数
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        //animation.setStartOffset(long startOffset);//执行前的等待时间

        viewPager.setAnimation(animation);
        /** 开始动画 */
        animation.startNow();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.add_win){
            addWin();
        }
    }

    private void addWin(){
        Fragment fragment = new SearchFragment();
        int currentFragmentId = fragment.getId();
        fragments.add(fragment);



        //获取并设置新添加的item
        currentItem = fragments.size() - 1;

        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("fragment_id", currentFragmentId);
        startActivity(intent);
    }
}