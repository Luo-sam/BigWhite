package com.webbrowser.bigwhite.View.viewpager;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.utils.WebPageHelper;

public class MyViewPager extends ViewPager {
    private boolean isFullScreen=true;
    private boolean canDel=true;

    public MyViewPager(Context context) {
        this(context,null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //防止viewpager在滚动中item仍可以上下滑动
                canDel = state == SCROLL_STATE_IDLE;
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("MotionEvent_viewpager", ev.toString());
        return !isFullScreen;
    }

    private FrameLayout frameLayout;
    protected float point_x, point_y; //手指按下的位置
    protected float mov_x, mov_y; //手指按下的位置
    private int left, right, bottom;
    private int measureWidth,measureHeight;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:

                point_x = ev.getRawX();
                point_y = ev.getRawY();
                if(!isFullScreen){

                }

                break;
            case MotionEvent.ACTION_MOVE:
                mov_x = ev.getRawX() - point_x;
                mov_y = ev.getRawY() - point_y;

                //上滑删除窗口
                if(!isFullScreen) {
                    if (Math.abs(mov_y) >= 25) {

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!isFullScreen){
                    if(Math.abs(mov_x) >= 25 && Math.abs(mov_x) > Math.abs(mov_y)) {
                        mov_x = 0;
                        mov_y = 0;
                        break;
                    }
                    else if(Math.abs(mov_y) >= 25){
                        mov_x = 0;
                        mov_y = 0;

                        if (getAdapter().getCount() == 1) {
                            Fragment fragment = WebPageHelper.webpagelist.get(getCurrentItem());
//                            pageTransform();


                            MainActivity activity = (MainActivity) fragment.getActivity();
                            fragment.onDestroy();
                            WebPageHelper.webpagelist.remove(getCurrentItem());
                            activity.addWin();
                            return true;
                        }
                        else if (getCurrentItem() < WebPageHelper.webpagelist.size() - 1) {
                            int curItem = getCurrentItem();
                            Fragment fragment = WebPageHelper.webpagelist.get(getCurrentItem());
                            fragment.onDestroy();
                            WebPageHelper.webpagelist.remove(curItem);
                            getAdapter().notifyDataSetChanged();
                            setCurrentItem(curItem);
                            getAdapter().notifyDataSetChanged();
                        }
                        else {
                            int curItem = getCurrentItem();
                            Fragment fragment = WebPageHelper.webpagelist.get(getCurrentItem());
                            fragment.onDestroy();
                            WebPageHelper.webpagelist.remove(curItem);
                            getAdapter().notifyDataSetChanged();
                            setCurrentItem(curItem-1);
                            getAdapter().notifyDataSetChanged();
                        }
                    }
                    else{
                        Fragment fragment = WebPageHelper.webpagelist.get(getCurrentItem());
                        MainActivity activity = (MainActivity) fragment.getActivity();
                        activity.toWindow();
                    }
                }

                break;
        }
        if(!isFullScreen)
        return super.onTouchEvent(ev);
        return false;
    }


    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    private void pageTransform(){
        ObjectAnimator animation = ObjectAnimator.ofFloat(this, "translationY", 10f);
        animation.setDuration(1000);
        animation.start();



        float position = -0.5f;
        setAlpha(1 + position);
        setPivotX(getWidth() / 2);
        setPivotY(0);
        setCameraDistance(60000);/*调整摄像机的位置，避免出现糊脸的感觉*/
        setRotationX((position * 180));
        setTranslationY(position * -getHeight());
    }
}