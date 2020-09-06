package com.java.chenxin;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//import com.java.chenxin.background.T;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.chenxin.data_struct.NewsList;
import com.java.chenxin.background.Search;
import com.java.chenxin.data_struct.NewsPiece;
import com.orm.SugarContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textView;
    String str = "not changed";
    NewsList list = null;
    String type = "news";
    String searchContext = "习";
    NewsPiece p = null;
    List<String> v = null;
    Observer<NewsList> listObserver = new Observer<NewsList>() {
        @Override
        public void onSubscribe(Disposable d) {
            System.out.println("开始了");
            Log.d("----","开始了");
        }

        @Override
        public void onNext(NewsList n) {
//            System.out.println(s);
            list = n;
//            Log.d("----", s);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            System.out.println("ob error");
        }

        @Override
        public void onComplete() {
            str = "";
            for(int i = 0; i < list.getNewsList().size(); i ++){
                str += "num: " + (i + 1) + " " + list.getNewsList().get(i).getTitle() + "\n";
            }
            showResponse(str);
            Log.d("----", "complete");
        }
    };

    Observer<List<String>> searchHistoryObserver = new Observer<List<String>>() {
        @Override
        public void onSubscribe(Disposable d) {
            System.out.println("开始了");
            Log.d("----","开始了");
        }

        @Override
        public void onNext(List<String> n) {
//            System.out.println(s);
             v = n;
//            Log.d("----", s);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            System.out.println("ob error");
        }

        @Override
        public void onComplete() {
            if(v == null) return;
            str = "Search History: \n";
            for(int i = 0; i < v.size(); i ++){
                str += v.get(i) + "\n";
            }
            str+="\n";
            textView.setText(str);
            Log.d("----", "complete");
        }
    };

    Observer<NewsPiece> pieceObserver = new Observer<NewsPiece>() {
        @Override
        public void onSubscribe(Disposable d) {
            System.out.println("开始了");
            Log.d("----","开始了");
        }

        @Override
        public void onNext(NewsPiece n) {
//            System.out.println(s);
            p = n;
//            Log.d("----", s);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            System.out.println("ob error");
        }

        @Override
        public void onComplete() {
            str =  p.getContent() + "\n";
            showResponse(str);
            Log.d("----", "complete");
        }
    };

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
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textview1);

        //sugarOrm
        SugarContext.init(this);
    }
    public void onClick(View view){
        if(view.getId() == R.id.button1){
//            sendGet();
//            NetWorkServer.loadNewsPiece(pieceObserver, "5f05f3f69fced0a24b2f84ee");
            Search.getSearchHistory(searchHistoryObserver);
        }
        else if(view.getId() == R.id.button2){
            Search.search(listObserver, "习近平",type);
//            NetWorkServer.reFresh(listObserver, type);
        }
        else if(view.getId() == R.id.button3){
            Search.search(listObserver, "上海",type);
//            NetWorkServer.loadMore(listObserver, type);
        }
        else if(view.getId() == R.id.button4){

            Search.search(listObserver,"广州",type);
        }
        else if(view.getId() == R.id.button5){
            Search.search(listObserver, "天津",type);
//            Search.searchRefresh(listObserver, searchContext,type);
        }
        else if(view.getId() == R.id.button6){
            Search.search(listObserver,"河北",type);
        }
        else if(view.getId() == R.id.button7){
            Search.search(listObserver, "习",type);
//            if(list == null){
//                textView.setText("NULL");
//            }
//            String msg = "";
//            for(int i = 0; i < list.getNewsList().size(); i ++){
//                msg += "num: " + (i + 1) + list.getNewsList().get(i).getTitle() + "\n";
//            }
//            textView.setText(msg);
        }
        else if(view.getId() == R.id.button8){
            Search.clearSearchHistory();
        }
    }

//    private void search(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                NewsList list = Search.search("北京 新发地", "all");
//                System.out.println("size: " + list.getNewsList().size());
//                System.out.println("content: " + list.getNewsList().get(0).getContent());
//                System.out.println(Search.getSearchHistory().toString());
//                String msg = "";
//                for(int i = 0; i < list.getNewsList().size(); i ++){
//                    msg += list.getNewsList().get(i).getTitle() + "\n";
//                }
//                showResponse(msg);
//            }
//        }).start();
//    }
//    private void searchRefresh(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                NewsList list = Search.searchRefresh("北京 新发地", "all");
//                System.out.println("size: " + list.getNewsList().size());
//                System.out.println("content: " + list.getNewsList().get(0).getContent());
//                System.out.println(Search.getSearchHistory().toString());
//                String msg = "";
//                for(int i = 0; i < list.getNewsList().size(); i ++){
//                    msg += list.getNewsList().get(i).getTitle() + "\n";
//                }
//                showResponse(msg);
//            }
//        }).start();
//    }
//    private void sendGet1() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                NetWorkServer netWorkServer = new NetWorkServer();
//                try{
//                    NewsList list = NetWorkServer.viewNewExcuteNew("news");
//                    System.out.println("下拉: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
//                    String msg = "";
//                    for(int i = 0; i < list.getNewsList().size(); i ++){
//                        msg += list.getNewsList().get(i).getTitle() + "\n";
//                    }
//                    showResponse(msg);
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }
//    private void sendGet() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    NewsList list = NetWorkServer.excute("news");
//                    showResponse(list.getNewsList().get(0).getContent());
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }
//    private void sendGet2() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                NetWorkServer netWorkServer = new NetWorkServer();
//                try{
//                    NewsList list = NetWorkServer.viewOldExcuteNew("news");
//                    System.out.println("上拉: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
//
////                    showResponse(msg);
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(response);
            }
        });

    }
}