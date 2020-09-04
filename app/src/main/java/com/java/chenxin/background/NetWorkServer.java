package com.java.chenxin.background;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttp;
import okhttp3.OkHttp.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkServer {
    private static int _count = 0;
    private static int _pageNum = 100;
    private final static int _SIZE = 5;
    private static NetWorkServer _netWorkServer = new NetWorkServer();
    public static int getCount(){
        return _count;
    }
    public static int getPageNum(){
        return _pageNum;
    }
    public static NetWorkServer getInstance(){
        return _netWorkServer;
    }
    private NetWorkServer(){

    }
    public static NewsList excute(String type){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url("https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=1&size=5").get().build();
        final Call call = okHttpClient.newCall(request);
        String msg = "";
        try {
            Response response = call.execute();
            msg += response.body().string();
            JSONObject jsonObject = new JSONObject(msg);
            return new NewsList(jsonObject,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        JSONSwitcher.saveToSDCard(msg);

        return null;
    }
    public static NewsList viewOldExcute(String type){
        OkHttpClient okHttpClient = new OkHttpClient();
        String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + (_pageNum - ++_count) + "&size=" + _SIZE;
        final Request request = new Request.Builder().url(mUrl).get().build();
        final Call call = okHttpClient.newCall(request);
        String msg = "";
        try {
            Response response = call.execute();
            msg += response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        JSONObject jsonObject = null;
        try{
            jsonObject =new JSONObject(msg);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        if(jsonObject == null){
            return null;
        }
        return new NewsList(jsonObject, type);
    }
    public static NewsList viewNewExcute(String type){

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            String tmpUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=1&size=1"; //随便找一页 找到总数
            final Request request = new Request.Builder().url(tmpUrl).get().build();
            final Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            String msg = "";
            msg = response.body().string();
            if(msg.equals("")){
                return null;
            }
            JSONObject jsonObject =new JSONObject(msg);
            if(jsonObject == null){
                return null;
            }
            JSONObject pagination = (JSONObject) jsonObject.get("pagination");
            int total = pagination.getInt("total");

            _pageNum = 1 + total / _SIZE;//定位到倒数第二页(整除时最后一页）由于从1开始计数 所以+1
            _count = 0;

            if(total % _SIZE == 0){//直接打印最后一页
                String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + _pageNum + "&size=" + _SIZE;
                final Request rrequest = new Request.Builder().url(mUrl).get().build();
                final Call ccall = okHttpClient.newCall(rrequest);
                msg = "";
                response = ccall.execute();
                msg += response.body().string();
                jsonObject = new JSONObject(msg);
                return new NewsList(jsonObject, type);
            }
            else{//打印倒数两页
                _count ++;
                String url1 = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + (_pageNum - 1) + "&size=" + _SIZE;
                final Request request1 = new Request.Builder().url(url1).get().build();
                final Call call1 = okHttpClient.newCall(request1);
                msg = "";
                response = call1.execute();
                msg += response.body().string();
                jsonObject = new JSONObject(msg);
                NewsList list1 = new NewsList(jsonObject, type);

                String url2 = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + (_pageNum) + "&size=" + _SIZE;
                final Request request2 = new Request.Builder().url(url2).get().build();
                final Call call2 = okHttpClient.newCall(request2);
                msg = "";
                response = call2.execute();
                msg += response.body().string();
                jsonObject = new JSONObject(msg);
                NewsList list2 = new NewsList(jsonObject, type);
                list1.merge(list2);
                return list1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
