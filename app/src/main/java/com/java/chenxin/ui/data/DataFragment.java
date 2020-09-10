package com.java.chenxin.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.java.chenxin.R;
import com.java.chenxin.ui.data.epidemicData.EpidemicDataFragment;
import com.java.chenxin.ui.data.epidemicMap.EpidemicMapFragment;
import com.java.chenxin.ui.data.newsClustering.NewsClusteringFragment;
import com.java.chenxin.universal.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class DataFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(com.java.chenxin.R.layout.fragment_viewpager, container, false);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new EpidemicDataFragment());
        fragmentList.add(new EpidemicMapFragment());
        fragmentList.add(new NewsClusteringFragment());
        List<String> titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.epidemic_data));
        titleList.add(getResources().getString(R.string.epidemic_map));
        titleList.add(getResources().getString(R.string.news_clustering));

        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        FragmentAdapter adapter = new FragmentAdapter(
                getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);

        return root;
    }
}