package com.java.chenxin.data_struct;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Entity {
    private double _hot;
    private String _lable;
    private String _url;
    private String _enwiki;
    private String _baidu;
    private String _zhwiki;
    private Map<String, String> _properties = new HashMap<String, String>();
    private List<Relation> _relations = new ArrayList<Relation>();
    private String _imgUrl;

    public Entity(JSONObject jsonObject){
        try {
            _hot = jsonObject.getDouble("hot");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            _lable = jsonObject.getString("label");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            _url = jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject abstractInfo = null;
        try {
            abstractInfo = jsonObject.getJSONObject("abstractinfo");
            try {
                _enwiki = abstractInfo.getString("enwiki");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                _baidu = abstractInfo.getString("baidu");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                _zhwiki = abstractInfo.getString("zhwiki");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject covid = null;
            try {
                covid = abstractInfo.getJSONObject("COVID");
                JSONObject properties = null;
                try {
                    properties = covid.getJSONObject("properties");
                    Iterator<String> it =  properties.keys();
                    while(it.hasNext()){
                        String tmp = it.next();
                        try {
                            _properties.put(tmp, properties.getString(tmp));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    JSONArray jsonArray = covid.getJSONArray("relations");
                    for(int i = 0; i < jsonArray.length(); i ++){
                        JSONObject tmpjs = jsonArray.getJSONObject(i);
                        boolean flag = false;
                        if(tmpjs.getString("relation").equals("父类")){
                            flag = true;
                        }
                        _relations.add(new Relation(flag, tmpjs.getString("url"),
                                tmpjs.getString("label"), tmpjs.getBoolean("forward")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try{
                _imgUrl = jsonObject.getString("img");
            }catch (JSONException e){
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}


class Relation{
    public boolean isFather = false;
    public String url;
    public String label;
    public boolean forward;

    public Relation(boolean isFather, String url, String label, boolean forward){
        this.isFather = isFather;
        this.forward = forward;
        this.url = url;
        this.label = label;
    }


}