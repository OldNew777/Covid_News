package com.java.chenxin.data_struct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DataPerDay implements Serializable {
    public String date;
    public int confirmed;
    public int cured;
    public int dead;
    public DataPerDay(){}
    public DataPerDay(String s, int confirmed, int cured, int dead){
        this.date = s;
        this.confirmed = confirmed;
        this.cured = cured;
        this.dead = dead;
    }
    public void show(){
        System.out.println("date: " + date + " confirmed: " + confirmed + " cured: " + cured + " dead: " + dead);
    }
    public JSONObject toJSONArray(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date", date);
            jsonObject.put("confirmed", confirmed);
            jsonObject.put("cured", cured);
            jsonObject.put("dead", dead);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
