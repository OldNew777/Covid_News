package com.java.chenxin;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//import com.java.chenxin.background.T;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.chenxin.background.JSONProcesser;
import com.java.chenxin.background.NewsList;
import com.java.chenxin.background.Test;
import com.java.chenxin.background.NetWorkServer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.textview1);

    }
    public void onClick(View view){
        if(view.getId() == R.id.button1){
            sendGet();
//            sendGet2();
        }
    }
    private void sendGet1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                NetWorkServer netWorkServer = new NetWorkServer();
                try{
                    NewsList list = NetWorkServer.viewNewExcute("all");
                    System.out.println("1: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
//                    list.merge(NetWorkServer.getInstance().viewOldExcute("all"));
//                    System.out.println("2: " + NetWorkServer.getInstance().getPageNum() + " "+ NetWorkServer.getInstance().getCount());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private void sendGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                NetWorkServer netWorkServer = new NetWorkServer();
                try{
                    NewsList list = NetWorkServer.excute();
                    showResponse(list.getNewsList().toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private void sendGet2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                NetWorkServer netWorkServer = new NetWorkServer();
                try{
                    NetWorkServer.getInstance().viewOldExcute("all");
                    System.out.println("3: " + NetWorkServer.getInstance().getPageNum() + " " + NetWorkServer.getInstance().getCount());
                    NetWorkServer.getInstance().viewOldExcute("all");
                    System.out.println("4: " + NetWorkServer.getInstance().getPageNum() + " " + NetWorkServer.getInstance().getCount());

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private void showResponse(final String response)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(response);
            }
        });
    }

}