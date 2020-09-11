package com.java.chenxin.ui.newsClustering;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.java.chenxin.R;
import com.java.chenxin.background.ClusterServer;
import com.java.chenxin.background.NetWorkServer;
import com.java.chenxin.data_struct.NewsPiece;
import com.java.chenxin.ui.news.newspiece.NewsPieceActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

enum RefreshMode{
    REFRESH, LOADMORE
}

public class EventListFragment extends Fragment {
    // 记录刷新/结束刷新的layout
    private RefreshLayout refreshLayout;

    // 记录refresh/loadMore
    private RefreshMode refreshMode;
    // 记录all/news/paper
    private String type;

    // 新闻列表
    private List<NewsPiece> newsInfo = new ArrayList<>(100);
    private EventListAdapter arrayAdapter;


    // 列表的observer
    private Observer<List<NewsPiece>> eventOb;
    // 新闻正文的observer
    private Observer<NewsPiece> newsDetailsObserver;


    public EventListFragment(final String type){
        super();
        this.type = type;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 获取控件
        View root = inflater.inflate(R.layout.fragment_news_clustering, container, false);
        ListView news_listView = root.findViewById(R.id.event_list);
        SmartRefreshLayout news_refreshLayOut = root.findViewById(R.id.event_refreshLayOut);

        // 各observer
        // 列表的observer
        eventOb = new Observer<List<NewsPiece>>() {
            @Override
            // 绑定激活函数
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(List<NewsPiece> newsList) {
                newsInfo.addAll(newsList);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                if (refreshMode == RefreshMode.REFRESH)
                    refreshLayout.finishRefresh();
                if (refreshMode == RefreshMode.LOADMORE)
                    refreshLayout.finishLoadMore();
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
        // 详情的observer
        newsDetailsObserver = new Observer<NewsPiece>() {
            @Override
            // 绑定激活函数
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(NewsPiece newsPiece) {
                Intent intent = new Intent(getContext(), NewsPieceActivity.class);
                intent.putExtra("NewsPiece", newsPiece);
                System.out.println(newsPiece.get_uid());
                startActivity(intent);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "无网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
            }
        };

        // 新闻列表
        newsInfo = new ArrayList<>(0);
        arrayAdapter = new EventListAdapter(getContext(), R.layout.item_eventlist, newsInfo);
        news_listView.setAdapter(arrayAdapter);
        news_refreshLayOut.autoRefresh();

        // 点击跳转新闻详情页
        news_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long column) {
                String id = newsInfo.get(position).get_uid();
                NetWorkServer.loadNewsPiece(newsDetailsObserver, id);
            }
        });



        // 下拉刷新
        news_refreshLayOut.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout_tmp) {
                Toast.makeText(getContext(), "正在刷新", Toast.LENGTH_SHORT).show();
                refreshMode = RefreshMode.REFRESH;
                refreshLayout = refreshLayout_tmp;
                ClusterServer.getCluster(eventOb, type, getContext());
            }
        });

        // 上拉加载
        news_refreshLayOut.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout_tmp) {
                Toast.makeText(getContext(), "正在加载", Toast.LENGTH_SHORT).show();
                refreshMode = RefreshMode.LOADMORE;
                refreshLayout = refreshLayout_tmp;
                ClusterServer.refreshCluster(eventOb, type, getContext());
            }
        });


        return root;
    }

}