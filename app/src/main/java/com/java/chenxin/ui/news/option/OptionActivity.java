package com.java.chenxin.ui.news.option;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.java.chenxin.R;
import com.java.chenxin.ui.news.NewsFragment;
import com.wenhuaijun.easytagdragview.EasyTipDragView;
import com.wenhuaijun.easytagdragview.bean.Tip;

import java.util.ArrayList;
import java.util.List;

public class OptionActivity extends AppCompatActivity {
    private EasyTipDragView easyTipDragView;
    private List<Tip> addedData;
    private List<Tip> toBeAddedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        // 找组件
        easyTipDragView = findViewById(R.id.easytipdrag_view);
        easyTipDragView.open();

        //设置点击“确定”按钮后最终数据的回调
        easyTipDragView.setOnCompleteCallback(new EasyTipDragView.OnCompleteCallback() {
            @Override
            public void onComplete(ArrayList<Tip> tips) {
                Intent intent = new Intent(OptionActivity.this, NewsFragment.class);
                intent.putExtra("Added", "all|paper");
                intent.putExtra("ToBeAdded", "news");
                setResult(0, intent);
                OptionActivity.this.finish();
            }
        });

    }
}