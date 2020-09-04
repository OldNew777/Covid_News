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
    public NewsList(JSONObject jsonObject){
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
            for(int i = 0; i < jsonArray.length(); i ++){
                NewsPiece tmpPiece = new NewsPiece(jsonArray.getJSONObject(i));
                _list.add(tmpPiece);
            }
        }catch (JSONException e){
            e.printStackTrace();

        }
    }
    public void merge(NewsList n){
        if(n == null)return;
        this._list.addAll(n.getNewsList());
    }

}
