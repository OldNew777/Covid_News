package com.java.chenxin.ui.scholar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.java.chenxin.R;
import com.java.chenxin.data_struct.NewsPiece;
import com.java.chenxin.data_struct.Scholar;

public class ScholarActivity extends AppCompatActivity {
    Scholar scholar;
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholar);

        // 设置新闻内容

        scholar = (Scholar) getIntent().getSerializableExtra("Scholar");
        ((TextView) findViewById(R.id.scholar_name)).setText(scholar.getName());
        ((TextView) findViewById(R.id.scholar_name_zh)).setText(scholar.getNameZh());
        ((TextView) findViewById(R.id.bio)).setText(scholar.getBio());
        ((TextView) findViewById(R.id.edu)).setText(scholar.getEdu());
        ((TextView) findViewById(R.id.work)).setText(scholar.getWork());
        ImageView imageView = (ImageView) findViewById(R.id.image_viewer_sa);
        Glide.with(this).load(scholar.getImgUrl()).into(imageView);
        // actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
}
