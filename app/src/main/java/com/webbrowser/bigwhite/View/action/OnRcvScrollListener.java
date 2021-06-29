package com.webbrowser.bigwhite.View.action;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author Jack Tony
 * @brief recyle view 滚动监听器
 * @date 2015/4/6
 */
public class OnRcvScrollListener extends RecyclerView.OnScrollListener{

    private String TAG = getClass().getSimpleName();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//        super.onScrollStateChanged(recyclerView, newState);
        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

    }

}