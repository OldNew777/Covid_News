package com.java.chenxin.data_struct;

import com.orm.SugarRecord;

public class SearchHistory extends SugarRecord {
    private String searchhistory;
    private int timestamp = 0;
    public SearchHistory(){}
    public SearchHistory(String s, int i){
        searchhistory = s;
        timestamp = i;
    }
    
    public String getSearchhistory(){
        return searchhistory;
    }
    public int getTimestamp(){return timestamp;}
    public void set_timestamp(int i){
        timestamp = i;}
}
