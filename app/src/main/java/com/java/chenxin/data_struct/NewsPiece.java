package com.java.chenxin.data_struct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Vector;

public class NewsPiece implements Serializable {
    private String _id, _title, _date, _content, _source;
    private double _influence;
    private NewsType _type;
    private Vector<String> _author, _region;

    public NewsPiece(final String _id, final String title, final String date,
                     Vector<String> author, final String content, final String source){
        this._id = _id;
        this._title = title;
        this._date = date;
        this._content = content;
        this._author = author;
        this._source = source;
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
    public String getSource(){
        return _source;
    }
    public String getDate() { return _date; }
    public String getContent() { return _content; }
    public void setTitle(final String title){ _title = title; }
}

enum NewsType{
    NEWS, PAPER,
}