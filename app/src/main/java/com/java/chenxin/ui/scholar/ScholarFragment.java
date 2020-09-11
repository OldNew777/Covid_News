package com.java.chenxin.ui.scholar;

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
import com.java.chenxin.background.ScholarServer;
import com.java.chenxin.data_struct.NewsPiece;
import com.java.chenxin.data_struct.Scholar;
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

public class ScholarFragment extends Fragment {
    // 记录刷新/结束刷新的layout
    private RefreshLayout refreshLayout;

    // 记录refresh/loadMore
    private RefreshMode refreshMode;
    // 记录all/news/paper
    private String type;

    // 新闻列表
    private List<Scholar> scholarInfo = new ArrayList<>(100);
    private ScholarAdapter arrayAdapter;


    // 列表的observer
    private Observer<List<Scholar>> scholarOb;
//    // 新闻正文的observer
//    private Observer<NewsPiece> newsDetailsObserver;


    public ScholarFragment(final String type){
        super();
        this.type = type;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 获取控件
        View root = inflater.inflate(R.layout.fragment_scholar, container, false);
        ListView scholar_listView = root.findViewById(R.id.scholar_list);

        // 各observer
        // 列表的observer
        scholarOb = new Observer<List<Scholar>>() {
            @Override
            // 绑定激活函数
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(List<Scholar> list) {
                scholarInfo.clear();
                scholarInfo.addAll(list);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                arrayAdapter.notifyDataSetChanged();
            }
        };

        // 新闻列表
        scholarInfo = new ArrayList<>(0);
        arrayAdapter = new ScholarAdapter(getContext(), R.layout.item_scholar, scholarInfo);
        scholar_listView.setAdapter(arrayAdapter);

        if(type.equals("all")){
            ScholarServer.getScholarList(scholarOb);
        }
        else if(type.equals("passaway")){
            ScholarServer.getPassawayScholarList(scholarOb);
        }
        else{
            ScholarServer.getHighViewScholarList(scholarOb);
        }
        // 点击跳转新闻详情页
        scholar_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long column) {
                Scholar s = scholarInfo.get(position);
                Intent intent = new Intent(getContext(), ScholarActivity.class);
                intent.putExtra("scholar", s);
                startActivity(intent);
            }
        });
        return root;
    }

}