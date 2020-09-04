package com.java.chenxin.background;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

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
    }
    public String excute(){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(_url).get().build();
        final Call call = okHttpClient.newCall(request);
        String msg = "";
        try {
            Response response = call.execute();
            msg += response.body().string();
        } catch (IOException e) {
            msg+=e.toString();
        }
        return msg;
    }
}

class JSONSwitcher{
    public static File dir;
    public JSONSwitcher(String type){
        dir = new File(Environment.getExternalStorageDirectory() + "/." + type + "/json/");
    }
    public static void saveToSDCard(Activity mActivity, String filename, String content){
        String en = Environment.getExternalStorageState();
        //获取SDCard状态,如果SDCard插入了手机且为非写保护状态
        if (en.equals(Environment.MEDIA_MOUNTED)) {
            try {
                dir.mkdirs(); //create folders where write files
                File file = new File(dir, filename);

                OutputStream out = new FileOutputStream(file);

                out.write(content.getBytes());
                out.close();

            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            //提示用户SDCard不存在或者为写保护状态
//            AppUtils.showToast(mActivity, "SDCard不存在或者为写保护状态");
        }
    }
}