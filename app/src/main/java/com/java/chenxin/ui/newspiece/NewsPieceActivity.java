package com.java.chenxin.ui.newspiece;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;

import com.java.chenxin.R;
import com.java.chenxin.data_struct.Constants;
import com.java.chenxin.data_struct.NewsPiece;
import com.java.chenxin.ui.share.ShareActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class NewsPieceActivity extends AppCompatActivity {
    private NewsPiece newsPiece;


    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(Constants.APP_ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        // 微信注册
        regToWx();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspiece);

        // 设置新闻内容
        newsPiece = (NewsPiece) getIntent().getSerializableExtra("NewsPiece");
        ((TextView) findViewById(R.id.title)).setText(newsPiece.getTitle());
        ((TextView) findViewById(R.id.source)).setText(newsPiece.getSource());
        ((TextView) findViewById(R.id.date)).setText(newsPiece.getDate());
        ((TextView) findViewById(R.id.content)).setText(newsPiece.getContent());
        ((TextView) findViewById(R.id.author)).setText(newsPiece.getAuthorString());

        // actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);

        // 方法1：intent调用
//        MenuItem shareItem = menu.findItem(R.id.action_share);
//        ShareActionProvider mShareActionProvider =
//                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
//        //通过setShareIntent调用getDefaultIntent()获取所有具有分享功能的App
//        mShareActionProvider.setShareIntent(getShareIntent());


        // 方法2：微信SDK
        menu.findItem(R.id.action_share).
                setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        System.out.println("onPrepareOptionsMenu.onMenuItemClick");
                        shareByWeChat();
                        return true;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }

    private Intent getShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, newsPiece.getTitle() + "\n" +
                newsPiece.getAuthorString() + "\n" + newsPiece.getSource() + "\n" +
                newsPiece.getDate() + "\n" + newsPiece.getContent());
        intent.setType("text/plain");
        Intent.createChooser(intent, getResources().getString(R.string.share));
        return intent;
    }

    private void shareByWeChat(){
        System.out.println("share by wechat");

        // 初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObject = new WXTextObject();
        textObject.text = newsPiece.getContent();

        // 用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = "Content";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());  //transaction字段用与唯一标示一个请求
        req.message = msg;

        // 调用api接口，发送数据到微信
        api.sendReq(req);
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
