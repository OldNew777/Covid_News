package com.java.chenxin.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.java.chenxin.R;
import com.java.chenxin.ui.data.DataFragmentAdapter;
import com.java.chenxin.ui.data.EpidemicDataFragment;
import com.java.chenxin.ui.data.EpidemicMapFragment;
import com.java.chenxin.ui.data.NewsClusteringFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private ViewPager viewPager;
    private List<Fragment> fragmentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewpager, container, false);

        fragmentList = new ArrayList<>();
        fragmentList.add(new NewsListFragment());
        fragmentList.add(new PaperListFragment());

        viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        DataFragmentAdapter adapter = new DataFragmentAdapter(
                getActivity().getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        return root;
    }
}
