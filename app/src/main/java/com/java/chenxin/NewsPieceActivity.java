package com.java.chenxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;

import com.java.chenxin.data_struct.NewsPiece;

public class NewsPieceActivity extends AppCompatActivity {
    private NewsPiece newsPiece;
    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspiece);

        // 设置新闻内容
        newsPiece = (NewsPiece) getIntent().getSerializableExtra("NewsPiece");
        ((TextView) findViewById(R.id.title)).setText(newsPiece.getTitle());
        ((TextView) findViewById(R.id.source)).setText(newsPiece.getSource());
        ((TextView) findViewById(R.id.date)).setText(newsPiece.getDate());
        ((TextView) findViewById(R.id.content)).setText(newsPiece.getContent());
        ((TextView) findViewById(R.id.author)).setText(newsPiece.getAuthorString());

        // 返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        //通过setShareIntent调用getDefaultIntent()获取所有具有分享功能的App
        mShareActionProvider.setShareIntent(getShareIntent());
        return super.onCreateOptionsMenu(menu);
    }

    private Intent getShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "这里是要分享的文字");
        intent.setType("text/plain");
        Intent.createChooser(intent, "Share");
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
