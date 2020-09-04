package com.java.chenxin;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.chenxin.ui.data.DataFragment;
import com.java.chenxin.ui.news.NewsFragment;
import com.java.chenxin.ui.scholar.ScholarFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    // 底部导航栏
    BottomNavigationView navView;
    // 三个fragment
    private NewsFragment newsFragment;
    private DataFragment dataFragment;
    private ScholarFragment scholarFragment;
    // 正在使用的fragment
    private Fragment activeFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化Fragment及底部导航栏
        initFragment(savedInstanceState);
        navView = findViewById(R.id.bottom_navi);

        // 底部导航栏添加监听器
        navView.setOnNavigationItemSelectedListener(bottomNaviSelectedListener);
    }

    private void initFragment(Bundle savedInstanceState){
        // 判断activity是否新建/重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (newsFragment == null) {
                newsFragment = new NewsFragment();
            }
            activeFragment = newsFragment;
            ft.replace(R.id.container, newsFragment).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNaviSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {     // 分支
                        case R.id.navigation_news:
                            if (newsFragment == null) {
                                newsFragment = new NewsFragment();
                            }
                            switchContent(activeFragment, newsFragment);
                            return true;
                        case R.id.navigation_data:
                            if (dataFragment == null) {
                                dataFragment = new DataFragment();
                            }
                            switchContent(activeFragment, dataFragment);
                            return true;
                        case R.id.navigation_scholar:
                            if (scholarFragment == null) {
                                scholarFragment = new ScholarFragment();
                            }
                            switchContent(activeFragment, scholarFragment);
                            return true;
                    }
                    return false;
                }
            };

    public void switchContent(Fragment from, Fragment to) {
        if (activeFragment != to) {
            activeFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                ft.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}