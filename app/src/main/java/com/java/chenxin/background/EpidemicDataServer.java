package com.java.chenxin.background;

import com.java.chenxin.data_struct.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EpidemicDataServer {

    public static Map<String, Map<String, List<String>>> readLocalJSON(){
        File file = new File(Constants.NAMELISTDATAPATH);
        if(!file.exists()){
            System.out.println("菜狗写错了");
        }
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = fis.read(buffer);
            //读取文件内容
            while(len > 0){
                sb.append(new String(buffer,0,len));
                //继续将数据放到buffer中
                len = fis.read(buffer);
            }
            //关闭输入流
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(sb.toString());
            Map<String, Map<String, List<String>>> map = new HashMap<String, Map<String, List<String>>>();
            Iterator<String> it = jsonObject.keys();
            while(it.hasNext()){
                String country = it.next();
                map.put(country, new HashMap<String, List<String>>());
                JSONObject obj = jsonObject.getJSONObject(country);
                Iterator<String> key = obj.keys();
                while(key.hasNext()){
                    String province = key.next();
                    List<String> tmplist = new ArrayList<String>();
                    map.get(country).put(province, tmplist);
                    JSONArray jsonArray = jsonObject.getJSONObject(country).getJSONArray(province);
                    for(int i = 0; i < jsonArray.length(); i ++){
                        tmplist.add(jsonArray.getString(i));
                    }
                }
            }
            return map;
        } catch (JSONException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }


        return null;
    }
}
