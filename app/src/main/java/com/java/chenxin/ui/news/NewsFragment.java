package com.java.chenxin.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.java.chenxin.R;
import com.java.chenxin.universal.FragmentAdapter;
import com.wenhuaijun.easytagdragview.EasyTipDragView;
import com.wenhuaijun.easytagdragview.bean.SimpleTitleTip;
import com.wenhuaijun.easytagdragview.bean.Tip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsFragment extends Fragment {
    // 组件
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private EasyTipDragView easyTipDragView;

    private Tip tipAll;
    private Tip tipNews;
    private Tip tipPaper;
    private List<Tip> addedTipList;
    private List<Tip> toBeAddedTipList;

    private Map<String, String> titleMap;

    private List<Fragment> fragmentList;
    private List<String> titleList;
    private FragmentAdapter fragmentAdapter;

    // fragment
    private List<NewsListFragment> fragmentList_3 = new ArrayList<>(3);

    private List<String> addedList;
    private List<String> toBeAddedList;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        for (int i = 0; i < fragmentList.size(); ++i)
            fragmentList.get(i).onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setTitleList() {
        titleList.clear();
        for (String string : addedList)
            titleList.add(titleMap.get(string));
    }

    private void setFragmentList(){
        fragmentList.clear();
        for (int i = 0; i < addedList.size(); ++i) {
            fragmentList_3.get(i).setType(addedList.get(i));
            fragmentList.add(fragmentList_3.get(i));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewpager_newslist, container, false);

        // 找组件
        tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        ImageView optionImageView = (ImageView) root.findViewById(R.id.option_image);
        easyTipDragView = (EasyTipDragView) root.findViewById(R.id.easytipdrag_view);


        titleMap = new HashMap<String, String>();
        titleMap.put("all", getResources().getString(R.string.all));
        titleMap.put("news", getResources().getString(R.string.news));
        titleMap.put("paper", getResources().getString(R.string.paper));

        fragmentList_3.add(new NewsListFragment("all"));
        fragmentList_3.add(new NewsListFragment("news"));
        fragmentList_3.add(new NewsListFragment("paper"));
        for (int i = 0; i < 3; ++i)
            fragmentList_3.get(i).setEasyTipDragView(easyTipDragView);
        addedList = new ArrayList<>(3);
        toBeAddedList = new ArrayList<>(3);
        addedList.add("all");
        addedList.add("news");
        addedList.add("paper");
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        setFragmentList();
        setTitleList();

        tipAll = new SimpleTitleTip(1, "all");
        tipNews = new SimpleTitleTip(2, "news");
        tipPaper = new SimpleTitleTip(3, "paper");

        // 点击option，跳出EasyTipDragView
        optionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedTipList = new ArrayList<>(3);
                toBeAddedTipList = new ArrayList<>(3);
                for (String string : addedList){
                    if (string.equals("all"))
                        addedTipList.add(tipAll);
                    else if (string.equals("news"))
                        addedTipList.add(tipNews);
                    else if (string.equals("paper"))
                        addedTipList.add(tipPaper);
                }
                for (String string : toBeAddedList){
                    if (string.equals("all"))
                        toBeAddedTipList.add(tipAll);
                    else if (string.equals("news"))
                        toBeAddedTipList.add(tipNews);
                    else if (string.equals("paper"))
                        toBeAddedTipList.add(tipPaper);
                }
                easyTipDragView.setDragData(addedTipList);
                easyTipDragView.setAddData(toBeAddedTipList);
                easyTipDragView.open();
            }
        });

        // 设置点击“确定”按钮后最终数据的回调
        easyTipDragView.setOnCompleteCallback(new EasyTipDragView.OnCompleteCallback() {
            @Override
            public void onComplete(ArrayList<Tip> tips) {
                addedTipList = easyTipDragView.getDragData();
                toBeAddedTipList = easyTipDragView.getAddData();
                addedList.clear();
                toBeAddedList.clear();
                for (Tip tip : addedTipList) {
                    addedList.add(((SimpleTitleTip) tip).getTip());
                }
                for (Tip tip : toBeAddedTipList){
                    toBeAddedList.add(((SimpleTitleTip) tip).getTip());
                }

                // TODO
                // viewpager的页面缓存不能清除！
                setFragmentList();
                setTitleList();

//                fragmentAdapter = new FragmentAdapter(
//                        getChildFragmentManager(), fragmentList, titleList);
//                fragmentAdapter.notifyDataSetChanged();
                viewPager.setAdapter(fragmentAdapter);
            }
        });

        viewPager.setOffscreenPageLimit(fragmentList.size());
        fragmentAdapter = new FragmentAdapter(
                getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            // 点击返回键
            case KeyEvent.KEYCODE_BACK:
                // 判断easyTipDragView是否已经显示出来
                if(easyTipDragView.isOpen()){
                    easyTipDragView.onKeyBackDown();
                    return true;
                }
                break;
        }
        return false;
    }
}
