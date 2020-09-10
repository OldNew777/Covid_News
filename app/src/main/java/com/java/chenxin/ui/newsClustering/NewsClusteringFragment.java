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
    private EventListFragment spreadFragment;
    private EventListFragment theoryFragment;
    private EventListFragment treatFragment;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        spreadFragment.onActivityResult(requestCode, resultCode, data);
        theoryFragment.onActivityResult(requestCode, resultCode, data);
        treatFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewpager, container, false);

        List<Fragment> fragmentList = new ArrayList<>();
        spreadFragment = new EventListFragment("spread");
        theoryFragment = new EventListFragment("theory");
        treatFragment = new EventListFragment("treat");
        fragmentList.add(spreadFragment);
        fragmentList.add(theoryFragment);
        fragmentList.add(treatFragment);
        List<String> titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.spread));
        titleList.add(getResources().getString(R.string.theory));
        titleList.add(getResources().getString(R.string.treat));

        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        FragmentAdapter adapter = new FragmentAdapter(
                getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);

        return root;
    }
}
