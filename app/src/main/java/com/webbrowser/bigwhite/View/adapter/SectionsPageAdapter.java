package com.webbrowser.bigwhite.View.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentInfos;

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
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
