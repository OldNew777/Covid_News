package com.java.chenxin.ui.news.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.java.chenxin.R;
import com.java.chenxin.background.Search;
import com.java.chenxin.ui.news.NewsListFragment;
import com.java.chenxin.universal.StringListAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private ListView suggestionList;
    private List<String> searchSuggestionList = new ArrayList<>(30);
    private StringListAdapter arrayAdapter;

    // 搜索建议（搜索历史）的observer
    private Observer<List<String>> searchSuggestionObserver;

    private void hideSoftInput(){
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("requestCode = " + requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 设置标题
        getSupportActionBar().setTitle(getSupportActionBar().getTitle() + " : " + getIntent().getStringExtra("type"));

        // 找组件
        searchView = (SearchView) findViewById(R.id.search_view);
        suggestionList = (ListView) findViewById(R.id.suggestion_list);
        suggestionList.setTextFilterEnabled(true);

        searchView.setSubmitButtonEnabled(true);

        // 搜索建议（搜索历史）的observer
        searchSuggestionObserver = new Observer<List<String>>() {
            @Override
            // 绑定激活函数
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(List<String> searchSuggestionList) {
                // 搜索建议列表
                SearchActivity.this.searchSuggestionList.clear();
                SearchActivity.this.searchSuggestionList.addAll(searchSuggestionList);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(SearchActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                arrayAdapter.notifyDataSetChanged();
            }
        };

        // 获取搜索历史
        arrayAdapter = new StringListAdapter(
                this, R.layout.item_suggestionlist, searchSuggestionList
        );
        ListView suggestion_listView = findViewById(R.id.suggestion_list);
        suggestion_listView.setAdapter(arrayAdapter);
        Search.getSearchHistory(searchSuggestionObserver);

        // 点击搜索历史，填充搜索框
        suggestion_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long column) {
                String content = searchSuggestionList.get(position);
                searchView.setQuery(content, true);
            }
        });

        // 监听搜索
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 点击搜索按钮
            @Override
            public boolean onQueryTextSubmit(final String query) {
                searchView.clearFocus();

                Intent intent = new Intent(SearchActivity.this, NewsListFragment.class);
                intent.putExtra("SearchQuery", query);
                setResult(0, intent);

                SearchActivity.this.finish();
                return true;
            }

            @Override
            // 过滤搜索建议
            public boolean onQueryTextChange(String newText) {
                //如果newText不是长度为0的字符串
                if (newText.isEmpty()) {
                    //清除ListView的过滤
                    suggestionList.clearTextFilter();
                } else {
                    //使用用户输入的内容对ListView的列表项进行过滤
                    suggestionList.setFilterText(newText);
                }
                return true;
            }
        });

        // 返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:     // return
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
