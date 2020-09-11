package com.java.chenxin.data_struct;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public Double getHot() {
        return _hot;
    }

    public String getLabel() {
        return _lable;
    }

    public String getUrl() {
        return _url;
    }

    public String getDiscription() {
        if (!_baidu.equals(""))
            return _baidu;
        if (!_zhwiki.equals(""))
            return _zhwiki;
        return _enwiki;
    }

    public Map<String, String> getPropertyMap() {
        return _properties;
    }

    public List<Relation> getRelationList() {
        return _relations;
    }

    public String getImgUrl() {
        return _imgUrl;
    }


    public void show() {
        System.out.println("hot:" + _hot);
        System.out.println("label: " + _lable);
        System.out.println("url: " + _url);
        System.out.println("abstract info:");
        System.out.println(_enwiki);
        System.out.println(_baidu);
        System.out.println(_zhwiki);
        Set<String> keys = _properties.keySet();
        System.out.println("properties: ");
        for (String key : keys) {
            System.out.println(key + " " + _properties.get(key));
        }
        System.out.println(_relations.size());
//        for(int i = 0; i < _relations.size(); i ++){
//            _relations.get(i).show();
//        }
        System.out.println(_imgUrl);
    }

    public Entity(JSONObject jsonObject) {
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
            abstractInfo = jsonObject.getJSONObject("abstractInfo");
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
                    Iterator<String> it = properties.keys();
                    while (it.hasNext()) {
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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tmpjs = jsonArray.getJSONObject(i);
                        boolean flag = false;
                        if (tmpjs.getString("relation").equals("父类")) {
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
            try {
                String url = jsonObject.getString("img");
                //先返回url
                _imgUrl = url;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


