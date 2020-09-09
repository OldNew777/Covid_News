package com.java.chenxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.chenxin.background.DataServer;
import com.java.chenxin.ui.data.DataFragment;
import com.java.chenxin.ui.news.NewsFragment;
import com.java.chenxin.ui.scholar.ScholarFragment;
import com.orm.SugarContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    // 底部导航栏
    private BottomNavigationView navView;
    // fragment管理器
    private FragmentManager fm;
    // 三个fragment
    private NewsFragment newsFragment;
    private DataFragment dataFragment;
    private ScholarFragment scholarFragment;
    // 正在使用的fragment
    private Fragment activeFragment = null;
    // 底部导航栏的listener
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNaviSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 建立数据库
        SugarContext.init(this);


        super.onCreate(savedInstanceState);
        setContentView(com.java.chenxin.R.layout.activity_main);

        // 初始化Fragment及底部导航栏监听器
        initFragment(savedInstanceState);
        bottomNaviSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {     // 分支
                            case com.java.chenxin.R.id.navigation_news:
                                if (newsFragment == null) {
                                    newsFragment = new NewsFragment();
                                }
                                switchContent(activeFragment, newsFragment);
                                return true;
                            case com.java.chenxin.R.id.navigation_data:
                                if (dataFragment == null) {
                                    dataFragment = new DataFragment();
                                }
                                switchContent(activeFragment, dataFragment);
                                return true;
                            case com.java.chenxin.R.id.navigation_scholar:
                                if (scholarFragment == null) {
                                    scholarFragment = new ScholarFragment();
                                }
                                switchContent(activeFragment, scholarFragment);
                                return true;
                        }
                        return false;
                    }
                };

        // 底部导航栏添加监听器
        navView = findViewById(com.java.chenxin.R.id.bottom_navi);
        navView.setOnNavigationItemSelectedListener(bottomNaviSelectedListener);

    }

    // 初始化第一个fragment
    private void initFragment(Bundle savedInstanceState){
        // 判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (newsFragment == null){
                newsFragment = new NewsFragment();
            }
            activeFragment = newsFragment;
            ft.replace(com.java.chenxin.R.id.main_container, newsFragment).commit();
        }
    }

    // 切换Fragment
    public void switchContent(Fragment from, Fragment to) {
        if (from != to) {
            activeFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                // 隐藏当前的fragment，add下一个到Activity中
                ft.hide(from).add(com.java.chenxin.R.id.main_container, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                ft.hide(from).show(to).commit();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        newsFragment.onActivityResult(requestCode, resultCode, data);
    }
}
