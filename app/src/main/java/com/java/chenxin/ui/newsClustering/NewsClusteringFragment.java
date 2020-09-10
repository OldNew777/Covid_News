package com.java.chenxin.ui.newsClustering;

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

public class NewsClusteringFragment extends Fragment {
    private EventListFragment medicineFragment;
    private EventListFragment virusFragment;
    private EventListFragment scientificFragment;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        medicineFragment.onActivityResult(requestCode, resultCode, data);
        virusFragment.onActivityResult(requestCode, resultCode, data);
        scientificFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewpager, container, false);

        List<Fragment> fragmentList = new ArrayList<>();
        medicineFragment = new EventListFragment("medicine");
        virusFragment = new EventListFragment("virus");
        scientificFragment = new EventListFragment("scientific");
        fragmentList.add(medicineFragment);
        fragmentList.add(virusFragment);
        fragmentList.add(scientificFragment);
        List<String> titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.medicine));
        titleList.add(getResources().getString(R.string.virus));
        titleList.add(getResources().getString(R.string.scientific));

        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        FragmentAdapter adapter = new FragmentAdapter(
                getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);

        return root;
    }
}
