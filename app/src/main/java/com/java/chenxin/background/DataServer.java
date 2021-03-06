package com.java.chenxin.background;

import android.provider.ContactsContract;

import com.java.chenxin.data_struct.Constants;
import com.java.chenxin.data_struct.DataPerDay;
import com.java.chenxin.data_struct.Entity;
import com.java.chenxin.data_struct.EpidemicData;
import com.java.chenxin.data_struct.EpidemicDataMap;
import com.java.chenxin.data_struct.NewsList;
import com.java.chenxin.data_struct.NewsPiece;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DataServer {
    private static int count = 0;

    public static Map<String, Map<String, List<String>>> readNameListJSON(){
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


    public static void initializeEpidemicData(){//这个讲道理只在第一次使用软件的时候调用就可以了
        new Thread(new Runnable() {
            @Override
            public void run() {

                NetWorkServer.downloadEpidemicDataMap();
                System.out.println("next");
                writeNameListJSON();
            }
        }).start();
    }
    public static void refreshEpidemicData(){//这个更新数据的 不会重写namelist.java
        new Thread(new Runnable() {
            @Override
            public void run() {

                NetWorkServer.downloadEpidemicDataMap();

            }
        });
    }

    public static void getDataPerDay(Observer<List<DataPerDay>> ob, final String districtName, final int daySum){
        Observable.create(new ObservableOnSubscribe<List<DataPerDay>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DataPerDay>> emitter) throws Exception {
                File file = new File(Constants.EPIDEMICDATAPATH);
                if(!file.exists()){
                    System.out.println("菜狗写错了: " + Constants.EPIDEMICDATAPATH);
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
                JSONObject jsonObject = new JSONObject(sb.toString());

                EpidemicData tmp = new EpidemicData(jsonObject.getJSONObject(districtName));
                System.out.println(tmp.getData(daySum));
                emitter.onNext(tmp.getData(daySum));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }

    public static void writeNameListJSON(){
        System.out.println("in function");

        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(Constants.EPIDEMICDATAPATH);
                if(!file.exists()){
                    System.out.println("菜狗写错了: " + Constants.EPIDEMICDATAPATH);
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
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                EpidemicDataMap mp = new EpidemicDataMap(jsonObject);
                List<String> nameList = new ArrayList<String>();
                Iterator<String> it = jsonObject.keys();
                while(it.hasNext()){
                    nameList.add(it.next());
                }

                file = new File(Constants.NAMELISTDATAPATH);
                if(!file.getParentFile().exists()){
                    boolean flag  = file.getParentFile().mkdirs();
                    System.out.println("making file path:" + file.toString() + " " + flag);

                }
                Map<String, Map<String, List<String>>> map = new HashMap<String,Map<String, List<String>>>();
                for(String name : nameList){
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
                    jsonObject = new JSONObject(map);
//            JSONObject jsonObject = new JSONObject();
                    PrintStream ps = new PrintStream(new FileOutputStream(file));
//            System.out.println(jsonObject.getJSONObject("China").getJSONArray("Beijing").getString(0));
                    ps.print(jsonObject.toString());
                    ps.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("done!");
            }

        }).start();
    }
//    public static void refreshEpidemicData(){
//        EpidemicDataMap epidemicDataMap = NetWorkServer.getEpidemicData();
//        JSONObject jsonObject = new JSONObject();
//        Map<String, EpidemicData> map = epidemicDataMap.getMap();
//        for(String name: map.keySet()){
//            EpidemicData tmp = map.get(name);
//            JSONObject tmpObj = new JSONObject();
//            try {
//                jsonObject.put(name, map.get(name).toJSONObject());
////                System.out.println(jsonObject.getJSONObject(name).toString());
//            } catch (JSONException e) {
//                System.out.println("别偷懒");
//                e.printStackTrace();
//            }
//        }
//        File file = new File(Constants.EPIDEMICDATAPATH);
//        if(!file.getParentFile().exists()){
//            boolean flag  = file.getParentFile().mkdirs();
//            System.out.println("making file path:" + file.toString() + " " + flag);
//        }
//        PrintStream ps = null;
//        try {
//            ps = new PrintStream(new FileOutputStream(file));
//            ps.print(jsonObject.toString());
//            ps.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
}
