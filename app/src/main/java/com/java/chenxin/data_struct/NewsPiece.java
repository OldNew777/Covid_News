package com.java.chenxin.data_struct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class NewsPiece {
    private String _id = "", _title = "", _date = "", _content = "", _source = "";
    private double _influence = -1.0;
    private NewsType _type;
    private Vector<String> _authors = null, _region = null;

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
    public String getAuthorString(){
        String s = "";
        if(_authors == null || _authors.size() == 0) return "";
        for(int i = 0; i < _authors.size() - 1; i ++){
            s += _authors.get(i) + ", ";
        }
        s += _authors.get(_authors.size() - 1);
        return s;
    }

    public NewsPiece(final String _id, final String title, final String date,
                     Vector<String> author, final String content){
        this._id = _id;
        this._title = title;
        this._date = date;
        this._content = content;
        this._authors = author;
    }
    public boolean searchWithHighLight(String[] key){//查找几个关键词并把他们高亮
        boolean flag = false;
        for(int i = 0; i < key.length; i ++){
            String rep = "";
            //title
            rep = _title.replace(key[i], ("<highlight>" + key[i]+"</highlight>"));
            if(rep.length() != _title.length()){
                flag = true;
                _title = rep;
            }
            //content
            rep = _content.replace(key[i], ("<highlight>" + key[i]+"</highlight>"));
            if(rep.length() != _content.length()){
                flag = true;
                _content = rep;
            }
            //source
            rep = _source.replace(key[i], ("<highlight>" + key[i]+"</highlight>"));
            if(rep.length() != _source.length()){
                flag = true;
                _source = rep;
            }
            //date
            rep = _date.replace(key[i], ("<highlight>" + key[i]+"</highlight>"));
            if(rep.length() != _date.length()){
                flag = true;
                _date = rep;
            }
        }
        return flag;
    }
    public boolean search(String reg){
//        System.out.println(_content);
        return _title.contains(reg) || _content.contains(reg) || _date.contains(reg) || _source.contains(reg) ;
//        return _title.matches(reg) || _content.matches(reg) || _date.matches(reg) || _source.matches(reg);
    }
    public NewsPiece(JSONObject jsonObject){
        if(jsonObject == null){
            System.out.println("NULL!");
            return;
        }
//        System.out.println(jsonObject.toString());
        try {
            _id = jsonObject.getString("_id");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        try {
            _title = jsonObject.getString("title");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        try {
            _content = jsonObject.getString("content");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        try {
            _date = jsonObject.getString("date");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        try {
            _source = jsonObject.getString("source");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        try {
            _influence = jsonObject.getDouble("influence");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        try {
            JSONArray authorArray = jsonObject.getJSONArray("authors");
            _authors = new Vector<String>();
            for(int i = 0; i < authorArray.length(); i++){
                _authors.add(authorArray.getJSONObject(i).getString("name"));
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        try {
            JSONArray regionArray = jsonObject.getJSONArray("regionIDs");
            _region = new Vector<String>();
            for(int i = 0; i < regionArray.length(); i++){
                _region.add(regionArray.getJSONObject(i).getString("name"));
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        try{
            String tmps = jsonObject.getString("type");
            if(tmps.equals("paper")){
                _type = NewsType.PAPER;
            }
            else{
                _type = NewsType.NEWS;
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

//        System.out.println(_id + " " + _title);


    }

}

enum NewsType{
    NEWS, PAPER,
}