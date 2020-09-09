package com.java.chenxin.background;

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
    public static void getEntityData(Observer<List<Entity>> ob, final String name){
        Observable.create(new ObservableOnSubscribe<List<Entity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Entity>> emitter) throws Exception {
                List<Entity> list = NetWorkServer.searchEntity(name);
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
    public static void getViewedNews(Observer<List<NewsPiece>> ob){

    }
    public static void refreshEpidemicData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetWorkServer.downloadEpidemicDataMap();
            }
        });
    }
    public static void writeNameListJSON(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                EpidemicDataMap.writeNameListJSON();
            }
        });
    }
    public static void getDataPerDay(Observer<List<DataPerDay>> ob, final String name, final int time){
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

                EpidemicData tmp = new EpidemicData(jsonObject.getJSONObject(name));
                System.out.println(tmp.getData(time));
                emitter.onNext(tmp.getData(time));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
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
