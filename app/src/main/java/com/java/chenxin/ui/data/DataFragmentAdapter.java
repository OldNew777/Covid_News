package com.java.chenxin.ui.data;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class DataFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = null;

    public DataFragmentAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        if (position == 0)
//            return "疫情数据";
//        else if (position == 1)
//            return "疫情图谱";
//        else if (position == 2)
//            return "新闻聚类";
        return super.getPageTitle(position);
    }
}