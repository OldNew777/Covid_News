package com.java.chenxin.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.java.chenxin.R;

import java.util.ArrayList;
import java.util.List;

public class DataFragment extends Fragment {
    private ViewPager viewPager;
    private List<Fragment> fragmentList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(com.java.chenxin.R.layout.fragment_viewpager, container, false);

        fragmentList = new ArrayList<>();
        fragmentList.add(new EpidemicDataFragment());
        fragmentList.add(new EpidemicMapFragment());
        fragmentList.add(new NewsClusteringFragment());

        viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        DataFragmentAdapter adapter = new DataFragmentAdapter(
                getActivity().getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        return root;
    }
}