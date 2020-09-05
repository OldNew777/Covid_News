package com.java.chenxin.ui.data;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.java.chenxin.ui.news.NewsFragment;

import java.util.ArrayList;
import java.util.List;

public class DataFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = null;

    public DataFragmentAdapter(FragmentManager fm){
        super(fm);
        fragments = new ArrayList<Fragment>();
        fragments.add(new NewsFragment());
        fragments.add(new NewsFragment());
        fragments.add(new NewsFragment());
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "疫情数据";
        else if (position == 1)
            return "疫情图谱";
        else if (position == 2)
            return "新闻聚类";
        return super.getPageTitle(position);
    }
}