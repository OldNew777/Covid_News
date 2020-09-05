package com.java.chenxin.background;
import com.java.chenxin.background.NewsPiece;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsList {
    private int _total = 0;
    private int _page = 0;
    private int _size = 0;
    private List<NewsPiece> _list = null;
    public int getTotal(){
        return _total;
    }
    public int getPage(){
        return _page;
    }
    public int getSize(){
        return _size;
    }
    public List<NewsPiece> getNewsList(){
        return _list;
    }
    public NewsPiece getPiece(final int i){
        return _list.get(i);
    }
    public NewsList(){
        _list = new ArrayList<NewsPiece>();
    }
    public NewsList(JSONObject jsonObject, String str){
        if(jsonObject == null){
            return;
        }
        try {
            JSONObject pagination = (JSONObject) jsonObject.get("pagination");
            _list = new ArrayList<NewsPiece>();
            _total = pagination.getInt("total");
            _page = pagination.getInt("page");
            _size = pagination.getInt("size");
//            System.out.println(_total);
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
//            System.out.println(jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                NewsPiece tmpPiece = new NewsPiece(jsonArray.getJSONObject(i));
                _list.add(tmpPiece);
            }
        }catch (JSONException e){
            e.printStackTrace();

        }
    }
    public boolean merge(NewsList n){
        if(n == null)return false;
        this._list.addAll(n.getNewsList());
        return true;
    }
    public boolean add(NewsPiece p){
        if(p == null) return false;
        this._list.add(p);
        return true;
    }
}
