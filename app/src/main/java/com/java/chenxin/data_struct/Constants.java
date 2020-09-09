package com.java.chenxin.data_struct;

import android.os.Environment;

import java.io.File;

public final class Constants {
    public final static int PAGESIZE = 10;
    public final static int SEARCHHISTORYSIZE = 30;
    public final static int SEARCHMAX = 700;
    public final static String NAMELISTDATAPATH = Environment.getDataDirectory().toString() +  File.separator+ "data" +
            File.separator + "com.java.chenxin" + File.separator + "cache" + File.separator + "json" + File.separator + "namelist.txt";
    public final static String EPIDEMICDATAPATH = Environment.getDataDirectory().toString() +  File.separator+ "data" +
            File.separator + "com.java.chenxin" + File.separator + "cache" + File.separator + "json" + File.separator + "epidemic.txt";
}
