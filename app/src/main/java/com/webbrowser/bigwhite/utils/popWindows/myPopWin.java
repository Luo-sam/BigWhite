package com.webbrowser.bigwhite.utils.popWindows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.webbrowser.bigwhite.R;

public class myPopWin extends PopupWindow {

    private final View view;
    @SuppressLint("ClickableViewAccessibility")
    public myPopWin(Context mContext, View.OnClickListener itemsOnClick) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.my_pop, null);

        Button login, addBookmark,bookMark,history,exit;
        LinearLayout layout;
        login = view.findViewById(R.id.login);
        addBookmark = view.findViewById(R.id.add_bookmark);
        bookMark = view.findViewById(R.id.bookmark);
        history = view.findViewById(R.id.history);
        exit = view.findViewById(R.id.exit);
        layout = view.findViewById(R.id.my_pop_layout);
        // 设置按钮监听
        login.setOnClickListener(itemsOnClick);
        addBookmark.setOnClickListener(itemsOnClick);
        bookMark.setOnClickListener(itemsOnClick);
        history.setOnClickListener(itemsOnClick);
        exit.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener((v, event) -> {


            int height = layout.getTop();
            int bottom = layout.getBottom();

            int yH = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (yH < height) {
                    dismiss();
                }
                if (yH > bottom) {
                    dismiss();
                }
            }
            return true;
        });


        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.take_photo_anim);

    }
}

