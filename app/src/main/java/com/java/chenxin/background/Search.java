package com.java.chenxin.background;

import com.java.chenxin.data_struct.NewsList;

import java.util.Vector;

public class Search {
    private static Vector<String> _searchHistory = new Vector<String>();

    public static Vector<String>  getSearchHistory(){
        return _searchHistory;
    }
    public static void cleatSearchHistory(){
        _searchHistory = new Vector<String>();
    }
    public static NewsList searchRefresh(String content, String type){
        _searchHistory.add(content);
        String[] searchArray = content.split(" ");

//        System.out.println(searchArray[0]);
        return NetWorkServer.searchExcuteNew(searchArray, type);
    }
    public static NewsList search(String content, String type){
        _searchHistory.add(content);
        String[] searchArray = content.split(" ");

//        System.out.println(searchArray[0]);
        return NetWorkServer.search(searchArray, type);
    }
}
