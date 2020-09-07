package com.java.chenxin.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.java.chenxin.NewsPieceActivity;
import com.java.chenxin.R;
import com.java.chenxin.background.NetWorkServer;
import com.java.chenxin.background.Search;
import com.java.chenxin.data_struct.NewsList;
import com.java.chenxin.data_struct.NewsPiece;
import com.java.chenxin.ui.search.SearchActivity;
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

public class NewsListFragment extends Fragment {
    // 记录刷新/结束刷新的layout
    private RefreshLayout refreshLayout;
    // 搜索框与其文本框
    private SearchView searchView;
    private TextView textView;

    // 记录refresh/loadMore
    private RefreshMode refreshMode;
    // 记录all/news/paper
    private String type;

    // 新闻列表
    private List<NewsPiece> newsInfo = new ArrayList<>(100);
    private NewsListAdapter arrayAdapter;
    // 新闻正文的NewsPiece
    private NewsPiece newsPieceDetails;


    // 列表的observer
    private Observer<NewsList> newsListObserver;
    // 新闻正文的observer
    private Observer<NewsPiece> newsDetailsObserver;


    public NewsListFragment(final String type){
        super();
        this.type = type;
    }

    public void setSearchQuery(Intent intent){
        searchView.clearFocus();
        String searchQuery = (String) intent.getSerializableExtra("SearchQuery");
        Search.search(newsListObserver, searchQuery, type);
//        textView.setText(searchQuery);
        Toast.makeText(getActivity(), "正在搜索："+ searchQuery, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 初始化、获取控件
        View root = inflater.inflate(com.java.chenxin.R.layout.fragment_newslist, container, false);
        ListView news_listView = root.findViewById(com.java.chenxin.R.id.news_list);
        SmartRefreshLayout news_refreshLayOut = root.findViewById(com.java.chenxin.R.id.news_refreshLayOut);
        searchView = (SearchView) root.findViewById(R.id.search_view);
        textView = (TextView) searchView.findViewById(androidx.appcompat.R.id.text);

        // 各observer
        newsListObserver = new Observer<NewsList>() {
            @Override
            // 绑定激活函数
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(NewsList newsList) {
                if (refreshMode == RefreshMode.REFRESH)
                    newsInfo.clear();
                newsInfo.addAll(newsList.getNewsList());
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
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
        newsDetailsObserver = new Observer<NewsPiece>() {
            @Override
            // 绑定激活函数
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(NewsPiece newsPiece) {
                newsPieceDetails = newsPiece;
                for (NewsPiece news : newsInfo) {
                    if (news.get_uid().equals(newsPiece.get_uid())){
                        news.setIsRead(true);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Intent intent = new Intent(getContext(), NewsPieceActivity.class);
                intent.putExtra("NewsPiece", newsPieceDetails);
                startActivity(intent);
            }
        };

        // 新闻列表
        newsInfo = new ArrayList<>(0);
        arrayAdapter = new NewsListAdapter(getContext(), com.java.chenxin.R.layout.item_newslist, newsInfo);
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

        // 搜索框
        searchView.setSubmitButtonEnabled(true);
        // 监听搜索框
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 点击搜索按钮
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Search.search(newsListObserver, query, type);
                Toast.makeText(getActivity(), "正在搜索："+ query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { return true; }
        });
        // 监听点击
        searchView.setOnClickListener(new SearchView.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("onClick");
                Intent intent = new Intent(getContext(), SearchActivity.class);

                int requestCode = 1;
                if (type.equals("all"))
                    requestCode = 1;
                else if (type.equals("news")){
                    requestCode = 2;
                }
                else if (type.equals("paper")){
                    requestCode = 3;
                }
                startActivityForResult(intent, requestCode);
            }
        });

        // 下拉刷新
        news_refreshLayOut.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout_tmp) {
                String searchQuery = String.valueOf(searchView.getQuery());

                // 如果搜索框有内容，则重新搜索
                if (!searchQuery.isEmpty()){
                    Toast.makeText(getActivity(), "正在搜索："+ searchQuery, Toast.LENGTH_SHORT).show();
                    refreshMode = RefreshMode.REFRESH;
                    refreshLayout = refreshLayout_tmp;
                    Search.search(newsListObserver, searchQuery, type);
                }

                // 如果搜索框没有内容，则刷新新闻列表
                else{
                    Toast.makeText(getActivity(), "正在刷新", Toast.LENGTH_SHORT).show();
                    refreshMode = RefreshMode.REFRESH;
                    refreshLayout = refreshLayout_tmp;
                    NetWorkServer.reFresh(newsListObserver, type);
                }
            }
        });

        // 上拉加载
        news_refreshLayOut.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout_tmp) {
                String searchQuery = String.valueOf(searchView.getQuery());

                // 如果搜索框有内容，则加载更多搜索内容
                if (!searchQuery.isEmpty()){
                    Toast.makeText(getActivity(), "正在加载", Toast.LENGTH_SHORT).show();
                    refreshMode = RefreshMode.LOADMORE;
                    refreshLayout = refreshLayout_tmp;
                    Search.searchRefresh(newsListObserver, searchQuery, type);
                }

                // 如果搜索框没有内容，则加载更多历史新闻
                else{
                    Toast.makeText(getActivity(), "正在加载", Toast.LENGTH_SHORT).show();
                    refreshMode = RefreshMode.LOADMORE;
                    refreshLayout = refreshLayout_tmp;
                    NetWorkServer.loadMore(newsListObserver, type);
                }
            }
        });


        return root;
    }
}