package com.java.chenxin.background;

import com.java.chenxin.data_struct.Constants;
import com.java.chenxin.data_struct.NewsList;
import com.java.chenxin.data_struct.SearchHistory;

import java.util.List;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Search {
    private static int _SEARCHHISTOYSIZE = 5;
    private static int _searchHistoryNum = 0;
    private static boolean isFirstTime = true;

    public static void clearSearchHistory(){
        _searchHistoryNum = 0;
        //删除表
       SearchHistory.deleteAll(SearchHistory.class);
    }

    public static void searchRefresh(Observer<NewsList> ob, String content, final String type){
        final String[] key = content.split(" ");
        Observable.create(new ObservableOnSubscribe<NewsList>() {
            @Override
            public void subscribe(ObservableEmitter<NewsList> emitter) throws Exception {
                NewsList list = NetWorkServer.searchExcuteNew_(key, type);
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
    public static void getSearchHistory(Observer<List<String>> ob){ //返回既定size的搜索记录
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                List<String> vec = getHistory();
//                    System.out.println("下拉: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
                emitter.onNext(vec);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
    private static List<String> getHistory(){
//        System.out.println(_searchHistoryNum + " " + isFirstTime);
        if(isFirstTime){
            isFirstTime = false;
            List<SearchHistory> list =  SearchHistory.listAll(SearchHistory.class);
            _searchHistoryNum = list.get(list.size() - 1).getTimestamp();
        }
        List<SearchHistory> list =  SearchHistory.listAll(SearchHistory.class);
        for(int i = 0; i < list.size(); i ++){
            System.out.println(list.get(i).getSearchhistory());
        }
        List<String> vec = new ArrayList<String>();
        int tmpid = _searchHistoryNum;
        while(tmpid > 0 && vec.size() < Constants.SEARCHHISTORYSIZE){
            List<SearchHistory> tmp = SearchHistory.find(SearchHistory.class,"timestamp = ?", String.valueOf(tmpid--));
            if(tmp == null || tmp.size() == 0) continue;
            vec.add(tmp.get(0).getSearchhistory());
        }
//        System.out.println("searchnum:" + _searchHistoryNum);
        return vec;
    }
    public static void search(Observer<NewsList> ob, String content, final String type){
        _addHistory(content);
        final String[] key = content.split(" ");
        Observable.create(new ObservableOnSubscribe<NewsList>() {
            @Override
            public void subscribe(ObservableEmitter<NewsList> emitter) throws Exception {
                NewsList list = NetWorkServer.search_(key, type);
//                    System.out.println("下拉: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
    private static void _addHistory(String s){
//        System.out.println(_searchHistoryNum + " " + isFirstTime);
        if(isFirstTime){
            List<SearchHistory> list =  SearchHistory.listAll(SearchHistory.class);
            _searchHistoryNum = list.get(list.size() - 1).getTimestamp();
        }
        SearchHistory sh = new SearchHistory(s, ++_searchHistoryNum);
        if(_searchHistoryNum == 1){
            sh.save();
//            System.out.println("searchnum:" + _searchHistoryNum);

            return;
        }
        List<SearchHistory> tmp = SearchHistory.find(SearchHistory.class,"searchhistory = ?", s);
        if(tmp == null || tmp.size() == 0) {
            sh.save();
        }
        else{//更新id
            tmp.get(0).set_timestamp(_searchHistoryNum);
            tmp.get(0).save();
        }
//        System.out.println("searchnum:" + _searchHistoryNum);
    }
}
