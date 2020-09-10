package com.java.chenxin.ui.data.epidemicMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.java.chenxin.R;
import com.java.chenxin.data_struct.Entity;
import com.java.chenxin.data_struct.Relation;
import com.java.chenxin.ui.news.search.SearchActivity;

import java.util.List;
import java.util.zip.Inflater;

public class EntityActivity extends AppCompatActivity {
    private Entity entity;
    private List<Relation> relationList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);

        // 找控件
        TextView title_textView = (TextView) findViewById(R.id.title);
        TextView description_textView = (TextView) findViewById(R.id.description);
        ImageView image_view = (ImageView) findViewById(R.id.image);
        ListView relationListView = (ListView) findViewById(R.id.relations_listview);

        // 获取数据
        entity = (Entity) getIntent().getSerializableExtra("Entity");

        // 设置页面
        setTitle(getTitle() + " : " + entity.getLabel());
        title_textView.setText(entity.getLabel());
        description_textView.setText(entity.getDiscription());
        Glide.with(this).load(entity.getImgUrl()).into(image_view);

        relationList = entity.getRelationList();
        RelationAdapter relationAdapter = new RelationAdapter(this, R.layout.item_relation, relationList);
        relationListView.setAdapter(relationAdapter);

        // 监听relation连接
        relationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long column) {
                String query = relationList.get(position).getLabel();
                Intent returnIntent = new Intent(EntityActivity.this, EpidemicMapFragment.class);

                returnIntent.putExtra("SearchQuery", query);
                setResult(0, returnIntent);

                EntityActivity.this.finish();
            }
        });
    }

}
