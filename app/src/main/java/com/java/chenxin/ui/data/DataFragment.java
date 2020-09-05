package com.java.chenxin.ui.data;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.java.chenxin.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataFragment extends Fragment {
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(com.java.chenxin.R.layout.fragment_data, container, false);

        viewPager = (ViewPager) root.findViewById(R.id.data_ViewPager);
        DataFragmentAdapter adapter = new DataFragmentAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        return root;
    }
}