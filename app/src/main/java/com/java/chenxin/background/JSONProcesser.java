package com.java.chenxin.background;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class JSONProcesser {
//    public static File dir;
    public static File dir = new File(Environment.getExternalStorageDirectory() + "/.news/json/");
    public JSONProcesser(){}
//    public JSONProcesser(String type){
//        dir = new File(Environment.getExternalStorageDirectory() + "/." + type + "/json/");
//    }
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
