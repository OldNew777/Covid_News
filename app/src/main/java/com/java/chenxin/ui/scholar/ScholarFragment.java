package com.java.chenxin.ui.scholar;

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

public class ScholarFragment extends Fragment {
    private ScholarListFragment allScholarListFragment;
    private ScholarListFragment passawayScholarListFragment;
    private ScholarListFragment highViewedScholarListFragment;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        allScholarListFragment.onActivityResult(requestCode, resultCode, data);
        passawayScholarListFragment.onActivityResult(requestCode, resultCode, data);
        highViewedScholarListFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewpager, container, false);

        List<Fragment> fragmentList = new ArrayList<>();
        allScholarListFragment = new ScholarListFragment("all");
        passawayScholarListFragment = new ScholarListFragment("passaway");
        highViewedScholarListFragment = new ScholarListFragment("high");
        fragmentList.add(allScholarListFragment);
        fragmentList.add(passawayScholarListFragment);
        fragmentList.add(highViewedScholarListFragment);
        List<String> titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.all_scholar));
        titleList.add(getResources().getString(R.string.passaway_scholar));
        titleList.add(getResources().getString(R.string.high_scholar));

        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        FragmentAdapter adapter = new FragmentAdapter(
                getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);

        return root;
    }
}
