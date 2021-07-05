package com.webbrowser.bigwhite.View.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.webbrowser.bigwhite.View.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentInfos;



    public SectionsPageAdapter(FragmentManager fm, List<Fragment> fragmentInfos) {
        super(fm);
        this.fragmentInfos = fragmentInfos;
    }

    public Fragment getItem(int position) {
        return fragmentInfos.get(position);
    }

    @Override
    public int getCount() {
        return fragmentInfos == null ? 0 : fragmentInfos.size();
    }

    /**
     * 使用这个方式，让页面不缓存，能够在清除fragment的时候对其做了删除
     *
     * @param object
     * @return
     */
    @Override
//    public int getItemPosition(Object object) {
//        return PagerAdapter.POSITION_NONE;
//    }
    public int getItemPosition(@NonNull Object object) {
        if (fragmentInfos.contains(object)) {
            return fragmentInfos.indexOf(object);
        } else {
            return POSITION_NONE;
        }
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    public void updatePage(List<Fragment> fragmentList) {
//        fragmentInfos = new ArrayList<>();
        fragmentInfos.clear();
        fragmentInfos.addAll(fragmentList);
        notifyDataSetChanged();
    }


}
