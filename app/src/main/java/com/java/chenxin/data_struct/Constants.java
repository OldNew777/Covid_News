package com.java.chenxin.data_struct;

import android.os.Environment;

import java.io.File;

public final class Constants {
    public final static int PAGESIZE = 15;
    public final static int SEARCHHISTORYSIZE = 20;
    public final static int SEARCHMAX = 700;
    public final static String NAMELISTDATAPATH = Environment.getDataDirectory().toString() +  File.separator+ "data" +
            File.separator + "com.java.chenxin" + File.separator + "cache" + File.separator + "json" + File.separator + "namelist.txt";
    public final static String EPIDEMICDATAPATH = Environment.getDataDirectory().toString() +  File.separator+ "data" +
            File.separator + "com.java.chenxin" + File.separator + "cache" + File.separator + "json" + File.separator + "epidemic.txt";
    public final static String ENTITYIMAGEPATH = Environment.getDataDirectory().toString() +  File.separator+ "data" +
            File.separator + "com.java.chenxin" + File.separator + "cache" + File.separator + "image";


    // APP_ID 替换为你的应用从官方网站申请到的合法appID
    public static final String APP_ID = "wx88888888";
}
