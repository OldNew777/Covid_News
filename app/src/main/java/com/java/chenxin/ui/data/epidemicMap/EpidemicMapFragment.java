package com.java.chenxin.ui.data.epidemicMap;

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

import com.java.chenxin.R;
import com.java.chenxin.background.DataServer;
import com.java.chenxin.background.EntityServer;
import com.java.chenxin.background.NetWorkServer;
import com.java.chenxin.background.Search;
import com.java.chenxin.data_struct.Entity;
import com.java.chenxin.ui.data.epidemicData.EpidemicDataFragment;
import com.java.chenxin.universal.StringListAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EpidemicMapFragment extends Fragment {
    // 组件
    // 搜索框与子组件
    private SearchView searchView;


    // data
    private List<Entity> entityList;
    private List<String> titleList;

    private Observer<List<Entity>> observerEntityList;
    private StringListAdapter titleAdapter;



    private void hideSoftInput(){
        ((InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_epidemic_map, container, false);

        // 找组件
        ListView listView = (ListView) root.findViewById(R.id.enetiy_list_view);
        searchView = (SearchView) root.findViewById(R.id.search_view);

        entityList = new ArrayList<>(20);
        titleList = new ArrayList<>(20);

        titleAdapter = new StringListAdapter(getContext(), R.layout.item_entitylist, titleList);
        listView.setAdapter(titleAdapter);

        // observer
        observerEntityList = new Observer<List<Entity>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(List<Entity> entities) {
                entityList = entities;
                titleList.clear();
                for (Entity entity : entityList)
                    titleList.add(entity.getLabel());
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                titleAdapter.notifyDataSetChanged();
            }
        };

        // 点击跳转详情
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long column) {
                searchView.clearFocus();
                hideSoftInput();
                Intent detailIntent = new Intent(getContext(), EntityActivity.class);
                detailIntent.putExtra("Entity", entityList.get(position));
                startActivityForResult(detailIntent, 100);
            }
        });

        // 监听搜索框
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 提交搜索结果
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                hideSoftInput();
                EntityServer.getEntityData(observerEntityList, query);
                Toast.makeText(getContext(), "正在搜索："+ query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && data != null) {
            String query = (String) data.getSerializableExtra("SearchQuery");
            searchView.setQuery(query, true);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
