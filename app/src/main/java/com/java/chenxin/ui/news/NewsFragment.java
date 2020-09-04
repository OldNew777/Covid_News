package com.java.chenxin.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.chenxin.NewsPiece;
import com.java.chenxin.R;

import java.util.Vector;

public class NewsFragment extends Fragment {
    ListView news_listView;
    SwipeRefreshLayout news_refreshLayOut;
    Vector<NewsPiece> newsInfo = new Vector<>(100);
    ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        System.out.println("onCreateView");

        // 初始化、获取控件
        View root = inflater.inflate(com.java.chenxin.R.layout.fragment_news, container, false);
        news_listView = root.findViewById(com.java.chenxin.R.id.news_list);
        news_refreshLayOut = root.findViewById(com.java.chenxin.R.id.news_refreshLayOut);

        // 新闻列表
        if (newsInfo.isEmpty())
            newsInfo = expandNewsList();
        String[] titleList = getTitleList(newsInfo);
        arrayAdapter = new ArrayAdapter<String>(getContext(), com.java.chenxin.R.layout.list_item, titleList);
        news_listView.setAdapter(arrayAdapter);

        // 下拉刷新新闻列表
        news_refreshLayOut.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Vector<NewsPiece> expandList = expandNewsList();
                newsInfo.addAll(expandList);
                String[] titleList = getTitleList(expandList);
                arrayAdapter.addAll(titleList);
                news_refreshLayOut.setRefreshing(false);//刷新完成
            }

        });


        return root;
    }


    private Vector<NewsPiece> expandNewsList(){
        Vector<NewsPiece> expandList = new Vector<>(100);
        // TODO
        Vector<String> author = new Vector<>(5);
        NewsPiece news = new NewsPiece("01hofd", "北京", "2020/09/04",
                author, "北京人刘辟");
        NewsPiece news2 = new NewsPiece("01hofd", "河南", "2020/09/04",
                author, "河南人刘辟");
        expandList.add(news);
        expandList.add(news2);
        expandList.add(news);
        expandList.add(news2);
        expandList.add(news);
        expandList.add(news2);
        expandList.add(news);
        expandList.add(news2);
        expandList.add(news);
        expandList.add(news2);
        return expandList;
    }

    private String[] getTitleList(Vector<NewsPiece> newsInfo){
        String[] titleList = new String[newsInfo.size()];
        for (int i = 0; i < newsInfo.size(); ++i){
            titleList[i] = newsInfo.get(i).getTitle();
        }
        return titleList;
    }
}