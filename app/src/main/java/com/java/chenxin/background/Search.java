package com.java.chenxin.background;

import java.util.Vector;

public class Search {
    private static Vector<String> _searchHistory = new Vector<String>();

    public static Vector<String>  getSearchHistory(){
        return _searchHistory;
    }
    public static void cleatSearchHistory(){
        _searchHistory = new Vector<String>();
    }
    public static NewsList search(String content, String type){
        _searchHistory.add(content);
        String[] searchArray = content.split(" ");

//        System.out.println(searchArray[0]);
        return NetWorkServer.searchExcute(searchArray, type);
    }
}
