package com.java.chenxin.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.java.chenxin.NewsActivity;
import com.java.chenxin.data_struct.NewsListAdapter;
import com.java.chenxin.data_struct.NewsPiece;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Vector;

public class NewsFragment extends Fragment {
    private ListView news_listView;
    private SmartRefreshLayout news_refreshLayOut;
    private List<NewsPiece> newsInfo = new Vector<>(100);
    private NewsListAdapter arrayAdapter;;

    int a = 0;
    public String getTitle(){
        return "新闻";
    }

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
        // 点击跳转新闻详情页
        news_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NewsPiece newsPiece = newsInfo.get(position);
                Intent intent = new Intent(getContext(), NewsActivity.class);
                intent.putExtra("NewsPiece", newsPiece);
                startActivity(intent);
            }
        });

        // 下拉刷新新闻列表监听
        news_refreshLayOut.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                // TODO
                List<NewsPiece> expandList = expandNewsList();
                newsInfo.addAll(0, expandList);
                arrayAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh(300);
            }

        });

        // 上拉加载更多历史新闻
        news_refreshLayOut.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                // TODO
                refreshlayout.finishLoadMore(300);
            }
        });

        return root;
    }


    private List<NewsPiece> expandNewsList(){
        List<NewsPiece> expandList = new Vector<>(100);
        // TODO
        Vector<String> author = new Vector<>(5);
        NewsPiece news = new NewsPiece("01hofd", "北京" + String.valueOf(a++), "2020/09/04",
                author, "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国" +
                "北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国北京人刘辟啥打咯发和红花沟爱问郭如彬哈利挂还是我国", "新华网");
        NewsPiece news2 = new NewsPiece("01hofd", "河南" + String.valueOf(a++), "2020/01/27",
                author, "河南人刘辟", "新华网");
        expandList.add(news);
        expandList.add(news2);
        return expandList;
    }
}