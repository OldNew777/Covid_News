package com.java.chenxin.data_struct;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EpidemicDataMap {
    private Map<String, EpidemicData> _map = new LinkedHashMap<String, EpidemicData>();
    private static List<String> _nameList = new ArrayList<String>();
    public EpidemicDataMap(){
    }
    public EpidemicDataMap(JSONObject jsonObject){//由json构造epidemicDataMap
        JSONArray nameList = jsonObject.names();
        for(int i = 0; i < nameList.length(); i ++){
            try{
                _nameList.add(nameList.getString(i));
                _map.put(nameList.getString(i), new EpidemicData(jsonObject.getJSONObject(nameList.getString(i))));//由名字对应的jsonobj创建epidemmicdata
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public List<DataPerDay> getData(String country, int days){
        if(_map.containsKey(country)){
            return _map.get(country).getData(days);
        }
        return null;
    }
    public DataPerDay getLatestData(String country){
        if(_map.containsKey(country)){
            return _map.get(country).getLatestData();
        }
        return null;
    }

    public List<String> getNameList(){return _nameList;}
    public Map<String, EpidemicData> getMap(){return _map;}
    public EpidemicData getEpidemicData(String s){return _map.get(s);}
    public void showName(){
        for(String s : _nameList){
            System.out.println(s);
        }
    }
    public void writeNameListJSON(){

        File file = new File(Constants.NAMELISTDATAPATH);
        if(!file.getParentFile().exists()){
            boolean flag  = file.getParentFile().mkdirs();
            System.out.println("making file path:" + file.toString() + " " + flag);

        }
        Map<String, Map<String, List<String>>> map = new HashMap<String,Map<String, List<String>>>();
        for(String name : _nameList){
            String[] s = name.split("\\|");
//            System.out.println(s[0]);
            if(!map.containsKey(s[0])){
                map.put(s[0], new HashMap<String, List<String>>());
            }
            if(s.length < 2) continue;//如果没有省份
            if(!map.get(s[0]).containsKey(s[1])){
                map.get(s[0]).put(s[1], new ArrayList<String>());
            }
            if(s.length < 3) continue;//如果没有区
            map.get(s[0]).get(s[1]).add(s[2]);
        }
        try {
            JSONObject jsonObject = new JSONObject(map);
//            JSONObject jsonObject = new JSONObject();
            PrintStream ps = new PrintStream(new FileOutputStream(file));
//            System.out.println(jsonObject.getJSONObject("China").getJSONArray("Beijing").getString(0));
            ps.print(jsonObject.toString());
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}
