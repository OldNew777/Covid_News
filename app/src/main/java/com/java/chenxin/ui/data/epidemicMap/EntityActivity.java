package com.java.chenxin.ui.data.epidemicMap;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.java.chenxin.R;
import com.java.chenxin.data_struct.Entity;

import java.util.zip.Inflater;

public class EntityActivity extends AppCompatActivity {
    private Entity entity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("in create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);

        // 找控件
        TextView title_textView = (TextView) findViewById(R.id.title);
        TextView description_textView = (TextView) findViewById(R.id.description);
//        ImageView image_view = (ImageView) findViewById(R.id.image);
        ListView relationListView = (ListView) findViewById(R.id.relations_listview);
        System.out.println("1");
        entity = (Entity) getIntent().getSerializableExtra("Entity");
        System.out.println("2");

        setTitle(getTitle() + " : " + entity.getLabel());
        title_textView.setText(entity.getLabel());
        description_textView.setText(entity.getDiscription());
//        Glide.with(this).load(entity.getImgUrl()).into(image_view);

        RelationAdapter relationAdapter = new RelationAdapter(this, R.layout.item_relation,
                entity.getRelationList());
        relationListView.setAdapter(relationAdapter);
    }

}
