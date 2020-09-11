package com.java.chenxin.data_struct;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class EpidemicData implements Serializable {
//    private String _outbreakDate;
    protected List<DataPerDay> _data = new ArrayList<DataPerDay>();
    protected String _outbreakDate;
    protected int _total;

//    public String getOutbreakDate(){return _outbreakDate;}
    public void show(){
        System.out.println("begin: " + _outbreakDate);
    }
    public List<DataPerDay> getData(int days){
        int head = (days > _total) ? 0 : (_total - days);
        return _data.subList(head, _total);
    }
    public DataPerDay getLatestData(){
        return _data.get(_total - 1);
    }
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("outbreakDate", _outbreakDate);
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < _data.size(); i++){
                jsonArray.put(_data.get(i).toJSONArray());
            }
            jsonObject.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public String getDate(){return _outbreakDate;}
    public EpidemicData(){}
    public EpidemicData(JSONObject jsonObject){
        //初始化开始日期
        try {
            _outbreakDate= jsonObject.getString("begin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //初始化所有数据
        try {
            JSONArray numList = jsonObject.getJSONArray("data");
            _total = numList.length();
            for(int i = 0; i < _total; i ++){
                JSONArray num = numList.getJSONArray(i);
                _data.add(new DataPerDay(_dateToString(_addDate(_stringToDate(_outbreakDate), i)),num.getInt(0), num.getInt(2), num.getInt(3)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Date _addDate(Date d, int num){
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(d);
            cd.add(Calendar.DAY_OF_YEAR, num);//增加NUM
            return cd.getTime();

        } catch (Exception e) {
            return null;
        }
    }
    private Date _minusDate(Date d, int num){
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(d);
            cd.add(Calendar.DAY_OF_YEAR, -1 * num);//增加NUM
            return cd.getTime();

        } catch (Exception e) {
            return null;
        }
    }
    private String _dateToString(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }
    private Date _stringToDate(String s){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}

