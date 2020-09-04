package com.java.chenxin.background;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Vector;

public class NewsPiece {
    private String _id, _title, _date, _content, _source;
    private double _influence;
    private NewsType _type;
    private Vector<String> _author, _region;

    public NewsPiece(final String _id, final String title, final String date,
                     Vector<String> author, final String content){
        this._id = _id;
        this._title = title;
        this._date = date;
        this._content = content;
        this._author = author;
    }
    public NewsPiece(JSONObject jsonObject){
        try{
            _id = jsonObject.getString("_id");
            _title = jsonObject.getString("title");
            _content = jsonObject.getString("content");
            _date = jsonObject.getString("date");
            _source = jsonObject.getString("source");
            JSONArray authorArray = jsonObject.getJSONArray("author");
            for(int i = 0; i < authorArray.length(); i++){
                _author.add(authorArray.getString(i));
            }
            JSONArray regionArray = jsonObject.getJSONArray("regionIDs");
            for(int i = 0; i < regionArray.length(); i++){
                _author.add(authorArray.getString(i));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }
    public String getTitle(){
        return _title;
    }
}

enum NewsType{
    NEWS, PAPER,
}