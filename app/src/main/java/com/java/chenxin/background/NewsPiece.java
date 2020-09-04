package com.java.chenxin.background;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Vector;

public class NewsPiece {
    private String _id, _title, _date, _content, _source;
    private double _influence;
    private NewsType _type;
    private Vector<String> _authors = new Vector<String>(), _region = new Vector<String>();
    public String getTitle(){
        return _title;
    }
    public String get_id(){
        return _id;
    }
    public String getDate(){
        return _date;
    }
    public String getContent(){
        return _content;
    }
    public String get_source(){
        return _source;
    }
    public double getInfluence(){
        return _influence;
    }
    public NewsType getType(){
        return _type;
    }
    public Vector<String> getAuthors(){
        return _authors;
    }
    public String getAuthorByIndex(final int i){
        return _authors.get(i);
    }
    public Vector<String> getRegion(){
        return _region;
    }
    public String getRegionsByIndex(final int i){
        return _region.get(i);
    }
    public NewsPiece(final String _id, final String title, final String date,
                     Vector<String> author, final String content){
        this._id = _id;
        this._title = title;
        this._date = date;
        this._content = content;
        this._authors = author;
    }
    public NewsPiece(JSONObject jsonObject){
        if(jsonObject == null){
            System.out.println("NULL!");
            return;
        }
        System.out.println(jsonObject.toString());
        try{
            _id = jsonObject.getString("_id");
//            System.out.println("id");
            _title = jsonObject.getString("title");
//            System.out.println("title");

            _content = jsonObject.getString("content");
//            System.out.println("content");

            _date = jsonObject.getString("date");
//            System.out.println("date");

            _source = jsonObject.getString("source");
//            System.out.println("source");

            _influence = jsonObject.getDouble("influence");
            System.out.println("influence: " + _influence);

            JSONArray authorArray = jsonObject.getJSONArray("authors");
            for(int i = 0; i < authorArray.length(); i++){
                _authors.add(authorArray.getString(i));
            }
            JSONArray regionArray = jsonObject.getJSONArray("regionIDs");
            for(int i = 0; i < regionArray.length(); i++){
                _region.add(regionArray.getString(i));
            }
            String tmps = jsonObject.getString("type");
            if(tmps.equals("paper")){
                _type = NewsType.PAPER;
            }
            else{
                _type = NewsType.NEWS;
            }
            System.out.println(_id + " " + _title);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

}

enum NewsType{
    NEWS, PAPER,
}