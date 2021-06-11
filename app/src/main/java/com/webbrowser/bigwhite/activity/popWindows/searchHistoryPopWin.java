package com.webbrowser.bigwhite.activity.popWindows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.webbrowser.bigwhite.R;

public class searchHistoryPopWin extends PopupWindow {
    private final View view;
    public searchHistoryPopWin(Context mContext,View.OnClickListener itemOnClick) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.search_pop,null);

    }
}
