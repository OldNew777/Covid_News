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
import com.java.chenxin.background.NetWorkServer;
import com.java.chenxin.data_struct.NewsList;
import com.java.chenxin.data_struct.NewsListAdapter;
import com.java.chenxin.data_struct.NewsPiece;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PaperListFragment extends Fragment {
    // 记录刷新/结束刷新的layout
    private RefreshLayout refreshLayout;
    // 记录refresh/loadMore
    private RefreshMode refreshMode;

    private List<NewsPiece> paperInfo = new Vector<>(100);
    private NewsListAdapter arrayAdapter;
    private Observer<NewsList> paperRefreshObserver = new Observer<NewsList>() {
        @Override
        // 绑定激活函数
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onNext(NewsList newsList) {
            if (refreshMode == RefreshMode.REFRESH)
                paperInfo.clear();
            paperInfo.addAll(newsList.getNewsList());
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("refresh error");
            refreshLayout.finishRefresh();
        }

        @Override
        public void onComplete() {
            arrayAdapter.notifyDataSetChanged();
            if (refreshMode == RefreshMode.REFRESH)
                refreshLayout.finishRefresh();
            else if (refreshMode == RefreshMode.LOADMORE)
                refreshLayout.finishLoadMore();
        }


    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 初始化、获取控件
        View root = inflater.inflate(com.java.chenxin.R.layout.fragment_newslist, container, false);
        ListView news_listView = root.findViewById(com.java.chenxin.R.id.news_list);
        SmartRefreshLayout news_refreshLayOut = root.findViewById(com.java.chenxin.R.id.news_refreshLayOut);

        // 论文列表
        paperInfo = new ArrayList<>(0);
        arrayAdapter = new NewsListAdapter(getContext(), com.java.chenxin.R.layout.list_item, paperInfo);
        news_listView.setAdapter(arrayAdapter);
        news_refreshLayOut.autoRefresh();
        // 点击跳转论文详情页
        news_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NewsPiece paperPiece = paperInfo.get(position);
                Intent intent = new Intent(getContext(), NewsActivity.class);
                intent.putExtra("NewsPiece", paperPiece);
                startActivity(intent);
            }
        });

        // 下拉刷新新闻列表监听
        news_refreshLayOut.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout_tmp) {
                refreshMode = RefreshMode.REFRESH;
                refreshLayout = refreshLayout_tmp;
                NetWorkServer.reFresh(paperRefreshObserver, "paper");
            }

        });

        // 上拉加载更多历史新闻
        news_refreshLayOut.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout_tmp) {
                refreshMode = RefreshMode.LOADMORE;
                refreshLayout = refreshLayout_tmp;
                NetWorkServer.loadMore(paperRefreshObserver, "paper");
            }
        });

        return root;
    }
}
