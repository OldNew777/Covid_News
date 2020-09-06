package com.java.chenxin.data_struct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Vector;

public class NewsPiece implements Serializable {
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
    public String getSource(){
        return _source;
    }
    public double getInfluence(){
        return _influence;
    }
    public NewsType getType(){
        return _type;
    }
    public String getAuthors(){
        String authors = "";
        if (_authors == null || _authors.isEmpty())
            return authors;
        else
            authors += _authors.get(0);
        for (int i = 1; i < _authors.size(); ++i)
            authors = authors + " " + _authors.get(i);

        System.out.println(authors);
        return authors;
    }
    public Vector<String> getRegion(){
        return _region;
    }
    public String getRegionsByIndex(final int i){
        return _region.get(i);
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
        return _title.contains(reg) || _content.contains(reg) || _date.contains(reg) || _source.contains(reg);
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
                _authors.add(authorArray.getString(i));
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        try {
            JSONArray regionArray = jsonObject.getJSONArray("regionIDs");
            _region = new Vector<String>();
            for(int i = 0; i < regionArray.length(); i++){
                _region.add(regionArray.getString(i));
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
    NEWS, PAPER
}