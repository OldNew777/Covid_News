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

import com.java.chenxin.data_struct.NewsListAdapter;
import com.java.chenxin.data_struct.NewsPiece;

import java.util.List;
import java.util.Vector;

public class NewsFragment extends Fragment {
    private ListView news_listView;
    private SwipeRefreshLayout news_refreshLayOut;
    private List<NewsPiece> newsInfo = new Vector<>(100);
    private NewsListAdapter arrayAdapter;;

    int a = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 初始化、获取控件
        View root = inflater.inflate(com.java.chenxin.R.layout.fragment_news, container, false);
        news_listView = root.findViewById(com.java.chenxin.R.id.news_list);
        news_refreshLayOut = root.findViewById(com.java.chenxin.R.id.news_refreshLayOut);

        // 新闻列表
        if (newsInfo.isEmpty())
            newsInfo = expandNewsList();
        arrayAdapter = new NewsListAdapter(getContext(), com.java.chenxin.R.layout.list_item, newsInfo);
        news_listView.setAdapter(arrayAdapter);

        // 下拉刷新新闻列表
        news_refreshLayOut.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<NewsPiece> expandList = expandNewsList();
                newsInfo.addAll(0, expandList);
                arrayAdapter.notifyDataSetChanged();
                news_refreshLayOut.setRefreshing(false);//刷新完成
            }

        });

        return root;
    }


    private List<NewsPiece> expandNewsList(){
        List<NewsPiece> expandList = new Vector<>(100);
        // TODO
        Vector<String> author = new Vector<>(5);
        NewsPiece news = new NewsPiece("01hofd", "北京" + String.valueOf(a++), "2020/09/04",
                author, "北京人刘辟", "新华网");
        NewsPiece news2 = new NewsPiece("01hofd", "河南" + String.valueOf(a++), "2020/01/27",
                author, "河南人刘辟", "新华网");
        expandList.add(news);
        expandList.add(news2);
        return expandList;
    }
}