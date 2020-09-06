package com.java.chenxin.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.java.chenxin.MainActivity;
import com.java.chenxin.R;
import com.java.chenxin.background.Search;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SearchActivity extends AppCompatActivity {
    private TextView textView;
    private SearchView searchView;
    private ListView suggestionList;
    private List<String> searchSuggestionList = new ArrayList<>(30);
    private SuggestionListAdapter arrayAdapter;
    private int requestCode;

    // 搜索建议（搜索历史）的observer
    private Observer<List<String>> searchSuggestionObserver;

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode   请求码
     * @param resultCode    回调码
     * @param data          Intent数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.requestCode = requestCode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 找组件
        searchView = (SearchView) findViewById(R.id.search_view);
        suggestionList = (ListView) findViewById(R.id.suggestion_list);
        suggestionList.setTextFilterEnabled(true);
        textView = (TextView) searchView.findViewById(androidx.appcompat.R.id.text);

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
                Toast.makeText(SearchActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                arrayAdapter.notifyDataSetChanged();
            }
        };

        // 获取搜索历史
        arrayAdapter = new SuggestionListAdapter(
                // TODO 多条只能显示一条
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
                textView.setText(content);
                searchView.clearFocus();
            }
        });

        // 监听搜索
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 点击搜索按钮
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Intent intent = new Intent(getParent(), MainActivity.class);
                intent.putExtra("SearchQuery", query);
                setResult(requestCode, intent);
                getParent().finish();
                return false;
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
