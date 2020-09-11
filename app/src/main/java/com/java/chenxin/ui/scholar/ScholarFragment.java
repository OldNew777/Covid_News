package com.java.chenxin.ui.scholar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.java.chenxin.R;
import com.java.chenxin.background.ClusterServer;
import com.java.chenxin.background.DataServer;
import com.java.chenxin.background.NetWorkServer;
import com.java.chenxin.background.ScholarServer;
import com.java.chenxin.data_struct.DataPerDay;
import com.java.chenxin.data_struct.Entity;
import com.java.chenxin.data_struct.NewsPiece;
import com.java.chenxin.data_struct.Scholar;
import com.java.chenxin.ui.news.newspiece.NewsPieceActivity;
import com.java.chenxin.universal.DoubleClickCheck;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//import com.java.chenxin.ui.news.RefreshMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ScholarFragment extends Fragment{
    List<Scholar> scholarList = new ArrayList<Scholar>();
    Observer<List<Scholar>> observer = null;

    private ScholarAdapter scholarAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scholar, container, false);
        ListView scholarListView = root.findViewById(R.id.scholar_list);

        observer = new Observer<List<Scholar>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Scholar> scholars) {
                scholarList.clear();
                scholarList.addAll(scholars);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                scholarAdapter.notifyDataSetChanged();
            }
        };

        scholarAdapter = new ScholarAdapter(getContext(), R.layout.item_scholar, scholarList);
        scholarListView.setAdapter(scholarAdapter);

        scholarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (DoubleClickCheck.isDoubleClick())
                    return;
                Scholar scholar = scholarList.get(position);
                Intent intent = new Intent(getContext(), ScholarActivity.class);
                intent.putExtra("Scholar", scholar);
                startActivity(intent);
            }
        });

        ScholarServer.getScholarList(observer);
        return root;
    }


}
