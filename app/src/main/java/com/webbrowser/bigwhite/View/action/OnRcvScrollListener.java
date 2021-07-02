package com.webbrowser.bigwhite.View.action;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * @author Jack Tony
 * @brief recyle view 滚动监听器
 * @date 2015/4/6
 */
public class OnRcvScrollListener extends RecyclerView.OnScrollListener{


    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        }

    }

}