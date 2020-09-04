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

import okhttp3.Call;
import okhttp3.OkHttp;
import okhttp3.OkHttp.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkServer {
    private String _url;
    public NetWorkServer(String url){
        _url = url;
//        _url = "https://covid-dashboard.aminer.cn/api/events/list?type=all&page=5&size=20";
    }
    public NetWorkServer(){}
    public JSONObject excute() throws JSONException{
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url("https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=1&size=5").get().build();
        final Call call = okHttpClient.newCall(request);
        String msg = "";
        try {
            Response response = call.execute();
            msg += response.body().string();
        } catch (IOException e) {
            msg+=e.toString();
        }
//        JSONSwitcher.saveToSDCard(msg);
        JSONObject jsonObject =new JSONObject(msg);
        return jsonObject;
    }
}
