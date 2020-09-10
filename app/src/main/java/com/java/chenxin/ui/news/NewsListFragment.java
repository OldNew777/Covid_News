package com.java.chenxin.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.java.chenxin.ui.news.newspiece.NewsPieceActivity;
import com.java.chenxin.R;
import com.java.chenxin.background.NetWorkServer;
import com.java.chenxin.background.Search;
import com.java.chenxin.data_struct.NewsList;
import com.java.chenxin.data_struct.NewsPiece;
import com.java.chenxin.ui.news.search.SearchActivity;
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
    // 搜索框与子组件
    private SearchView searchView;

    // 记录refresh/loadMore
    private RefreshMode refreshMode;
    // 记录all/news/paper
    private String type;

    // 新闻列表
    private List<NewsPiece> newsInfo = new ArrayList<>(100);
    private NewsListAdapter arrayAdapter;


    // 列表的observer
    private Observer<NewsList> newsListObserver;
    // 新闻正文的observer
    private Observer<NewsPiece> newsDetailsObserver;


    public NewsListFragment(final String type){
        super();
        this.type = type;
    }

    private void hideSoftInput(){
        ((InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
            if (    (requestCode == 1 && type.equals("all")) ||
                    (requestCode == 2 && type.equals("news")) ||
                    (requestCode == 3 && type.equals("paper"))     ){
                searchView.setQuery((String) data.getSerializableExtra("SearchQuery"), true);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 获取控件
        View root = inflater.inflate(com.java.chenxin.R.layout.fragment_newslist, container, false);
        ListView news_listView = root.findViewById(com.java.chenxin.R.id.news_list);
        SmartRefreshLayout news_refreshLayOut = root.findViewById(com.java.chenxin.R.id.news_refreshLayOut);
        searchView = (SearchView) root.findViewById(R.id.search_view);

        // 各observer
        // 列表的observer
        newsListObserver = new Observer<NewsList>() {
            @Override
            // 绑定激活函数
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(NewsList newsList) {
                if (refreshMode == RefreshMode.REFRESH) {
                    newsInfo.clear();
                    if (newsList.getNewsList().isEmpty())
                        Toast.makeText(getContext(), "无搜索结果", Toast.LENGTH_SHORT).show();
                }
                newsInfo.addAll(newsList.getNewsList());
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
                for (NewsPiece news : newsInfo) {
                    if (news.get_uid().equals(newsPiece.get_uid())){
                        news.setIsRead(true);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                Intent intent = new Intent(getContext(), NewsPieceActivity.class);
                intent.putExtra("NewsPiece", newsPiece);
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
        arrayAdapter = new NewsListAdapter(getContext(), com.java.chenxin.R.layout.item_newslist, newsInfo);
        news_listView.setAdapter(arrayAdapter);
        news_refreshLayOut.autoRefresh();

        // 点击跳转新闻详情页
        news_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long column) {
                searchView.clearFocus();
                hideSoftInput();
                String id = newsInfo.get(position).get_uid();
                NetWorkServer.loadNewsPiece(newsDetailsObserver, id);
            }
        });

        // 搜索框
        searchView.findViewById(R.id.search_edit_frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.callOnClick();
            }
        });
        searchView.findViewById(R.id.search_src_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.callOnClick();
            }
        });
        searchView.findViewById(R.id.search_plate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.callOnClick();
            }
        });
        // 监听搜索框
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 提交搜索结果
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                hideSoftInput();
                Search.search(newsListObserver, query, type);
                Toast.makeText(getContext(), "正在搜索："+ query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        // 监听搜索图标的点击
        searchView.setOnClickListener(new SearchView.OnClickListener(){
            @Override
            public void onClick(View view) {
                searchView.clearFocus();
                hideSoftInput();
                int requestCode = 1;
                if (type.equals("all"))
                    requestCode = 1;
                else if (type.equals("news")){
                    requestCode = 2;
                }
                else if (type.equals("paper")){
                    requestCode = 3;
                }
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("type", type.toUpperCase());
                getActivity().startActivityForResult(intent, requestCode);
            }
        });

        // 下拉刷新
        news_refreshLayOut.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout_tmp) {
                searchView.clearFocus();
                hideSoftInput();
                String searchQuery = String.valueOf(searchView.getQuery());
                // 如果搜索框有内容，则重新搜索
                if (!searchQuery.isEmpty()){
                    refreshLayout = refreshLayout_tmp;
                    searchView.setQuery(searchView.getQuery(), true);
                }

                // 如果搜索框没有内容，则刷新新闻列表
                else{
                    Toast.makeText(getContext(), "正在刷新", Toast.LENGTH_SHORT).show();
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
                searchView.clearFocus();
                hideSoftInput();
                String searchQuery = String.valueOf(searchView.getQuery());

                // 如果搜索框有内容，则加载更多搜索内容
                if (!searchQuery.isEmpty()){
                    Toast.makeText(getContext(), "正在加载", Toast.LENGTH_SHORT).show();
                    refreshMode = RefreshMode.LOADMORE;
                    refreshLayout = refreshLayout_tmp;
                    Search.searchRefresh(newsListObserver, searchQuery, type);
                }

                // 如果搜索框没有内容，则加载更多历史新闻
                else{
                    Toast.makeText(getContext(), "正在加载", Toast.LENGTH_SHORT).show();
                    refreshMode = RefreshMode.LOADMORE;
                    refreshLayout = refreshLayout_tmp;
                    NetWorkServer.loadMore(newsListObserver, type);
                }
            }
        });


        return root;
    }

}