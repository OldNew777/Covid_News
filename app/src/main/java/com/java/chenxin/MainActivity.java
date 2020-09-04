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
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textview1);

    }
    public void onClick(View view){
        if(view.getId() == R.id.button1){
            sendGet();

        }
        else if(view.getId() == R.id.button2){
            sendGet1();
        }
        else if(view.getId() == R.id.button3){
            sendGet2();
        }
    }
    private void sendGet1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                NetWorkServer netWorkServer = new NetWorkServer();
                try{
                    NewsList list = NetWorkServer.viewNewExcute("paper");
                    System.out.println("下拉: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
                    showResponse(list.getNewsList().get(0).getTitle());
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
                    NewsList list = NetWorkServer.excute("paper");
                    showResponse(list.getNewsList().get(0).getTitle());
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
                    NewsList list = NetWorkServer.viewOldExcute("paper");
                    System.out.println("上拉: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
                    showResponse(list.getNewsList().get(0).getTitle());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(response);
            }
        });

    }
}