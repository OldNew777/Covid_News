package com.java.chenxin.ui.scholar;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
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
        Resources res = getResources();
        scholar = (Scholar) getIntent().getSerializableExtra("Scholar");
        ((TextView) findViewById(R.id.scholar_name)).setText(scholar.getName());
        ((TextView) findViewById(R.id.scholar_name_zh)).setText(scholar.getNameZh());
        if(scholar.getBio() != null){
            ((TextView) findViewById(R.id.bio)).setText(scholar.getBio());
            ((TextView) findViewById(R.id.bio_title)).setText(res.getString(R.string.scholar_bio));
        }
        if(scholar.getEdu() != null){
            ((TextView) findViewById(R.id.edu)).setText(scholar.getEdu());
            ((TextView) findViewById(R.id.edu_title)).setText(res.getString(R.string.scholar_edu));
        }
        if(scholar.getWork() != null){
            ((TextView) findViewById(R.id.work)).setText(scholar.getWork());
            ((TextView) findViewById(R.id.work_title)).setText(res.getString(R.string.scholar_work));
        }
        if(scholar.getAffiliation() != null){
            ((TextView) findViewById(R.id.affiliation)).setText(scholar.getAffiliation());
            ((TextView) findViewById(R.id.affiliation_title)).setText(res.getString(R.string.scholar_affiliation));
        }
        if(scholar.getHomepage() != null){
            ((TextView) findViewById(R.id.homepage)).setText(scholar.getHomepage());
            ((TextView) findViewById(R.id.homepage_title)).setText(res.getString(R.string.scholar_homepage));
        }

        ImageView imageView = (ImageView) findViewById(R.id.image_viewer_sa);
        Glide.with(this).load(scholar.getImgUrl()).into(imageView);
        // actionbar
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
