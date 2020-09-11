package com.java.chenxin.ui.data.epidemicMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.java.chenxin.R;
import com.java.chenxin.background.EntityServer;
import com.java.chenxin.data_struct.Entity;
import com.java.chenxin.data_struct.Relation;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EntityActivity extends AppCompatActivity {
    private Entity entity;
    private List<Relation> relationList;

    private Observer<Entity> entityObserver;

    // 组件
    TextView title_textView;
    TextView description_textView;
    ImageView image_view;
    ListView relationListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);

        // 找控件
        title_textView = (TextView) findViewById(R.id.title);
        description_textView = (TextView) findViewById(R.id.description);
        image_view = (ImageView) findViewById(R.id.image);
        relationListView = (ListView) findViewById(R.id.relations_listview);

        // 获取数据
        String entityLabel = getIntent().getStringExtra("EntityLabel");

        // 设置observer
        entityObserver = new Observer<Entity>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Entity entity_in) {
                if (entity_in == null)
                    EntityActivity.this.finish();
                entity = entity_in;
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(EntityActivity.this, "加载失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void onComplete() {
                setUpPage();
            }
        };
        EntityServer.getEntity(entityObserver, entityLabel);
    }

    private void setUpPage(){
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
