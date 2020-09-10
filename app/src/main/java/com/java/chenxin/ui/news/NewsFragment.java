package com.java.chenxin.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.java.chenxin.R;
import com.java.chenxin.universal.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private NewsListFragment allListFragment;
    private NewsListFragment newsListFragment;
    private NewsListFragment paperListFragment;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        allListFragment.onActivityResult(requestCode, resultCode, data);
        newsListFragment.onActivityResult(requestCode, resultCode, data);
        paperListFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewpager, container, false);

        List<Fragment> fragmentList = new ArrayList<>();
        allListFragment = new NewsListFragment("all");
        newsListFragment = new NewsListFragment("news");
        paperListFragment = new NewsListFragment("paper");
        fragmentList.add(allListFragment);
        fragmentList.add(newsListFragment);
        fragmentList.add(paperListFragment);
        List<String> titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.all));
        titleList.add(getResources().getString(R.string.news));
        titleList.add(getResources().getString(R.string.paper));

        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        FragmentAdapter adapter = new FragmentAdapter(
                getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);

        return root;
    }
}
