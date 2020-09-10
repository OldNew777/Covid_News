package com.java.chenxin.background;


import android.content.Context;
import android.content.res.AssetManager;

import com.java.chenxin.data_struct.Constants;
import com.java.chenxin.data_struct.NewsPiece;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ClusterServer {
    private static int _pageNum = 0;
    private static int _virusNum = 0;
    private static int _scientificNum = 0;
    private static int _medicineNum = 0;
    private static void _setPageNum(String type){
        if(type.equals("medicine")){
            _pageNum = _medicineNum;
        }else if(type.equals("scientific")){
            _pageNum = _scientificNum;
        }else{
            _pageNum = _virusNum;
        }
    }
    private static void _setTypeNum(String type){
        if(type.equals("medicine")){
            _medicineNum = _pageNum;
        }else if(type.equals("scientific")){
            _scientificNum = _pageNum;
        }else{
            _virusNum = _pageNum;
        }
    }

    public static void getCluster(Observer<List<NewsPiece>> ob, final String type, final Context context){//第一次获得聚类事件列表，type是聚类的种类，contxt是当前上下文
        _pageNum = 0;
        _setTypeNum(type);
        refreshCluster(ob, type, context);
    }
    public static void refreshCluster(Observer<List<NewsPiece>> ob, final String type, final Context context){//refresh
        Observable.create(new ObservableOnSubscribe<List<NewsPiece>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NewsPiece>> emitter) throws Exception {
                _setPageNum(type);
                List<NewsPiece> pieces = _getCluster(type, context);
                _setTypeNum(type);
//                    System.out.println("下拉: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
                emitter.onNext(pieces);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
    private static List<NewsPiece> _getCluster(String type, Context context){
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(type + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner s = new Scanner(is);
        List<String> id = new ArrayList<String>();
        while(s.hasNext()){
            id.add(s.next());
        }
        s.close();
        List<NewsPiece> list = new ArrayList<NewsPiece>();
        if(_pageNum * Constants.PAGESIZE > id.size()){
            return list;
        }
        int head = (_pageNum++ * Constants.PAGESIZE);
        int tail = (_pageNum * Constants.PAGESIZE > id.size()) ? id.size() : _pageNum * Constants.PAGESIZE ;
        for(int i = head; i < tail; i ++){
            System.out.println(id.get(i));
            list.add(NetWorkServer._searchById(id.get(i)));
        }
        return list;
    }
}
