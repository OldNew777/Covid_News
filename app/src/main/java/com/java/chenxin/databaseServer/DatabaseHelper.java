package com.java.chenxin.databaseServer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASENAME = "database.db";
    public static final int databaseVersion = 1;
    public static final String SEARCHHISTORYTABLENAME = "SearchHistory";
    public static final String NEWSHISTORYTABLENAME = "NewsHistory";
    private static final String _CREATESEARCHHISTORYTABLE = "CREATE TABLE" + " " + SEARCHHISTORYTABLENAME +
            "(" +
            "id" + " " + "INTEGER" + " " + "PRIMARY KEY" + " " +"AUTOINCREMENT," +
            "searchContent" + " " + "TEXT" + " " + "NOT NULL" +
            ");";
    private static final String _CREATEVIEWHISTORY = "CREATE TABLE" + " " + NEWSHISTORYTABLENAME +
            "(" +
            "id         TEXT    PRIMARY KEY NOT NULL," +
            "title      TEXT    NOT NULL," +
            "content    TEXT    NOT NULL," +
            "date       TEXT    NOT NULL," +
            "type       TEXT    NOT NULL," +
            "source     TEXT," +
            "influence  REAL," +
            "author     TEXT," +
            "region     TEXT" +
            ");";

    private static int _lastSearchHistoryId = 0;
    private static int _lastNewsId = 0;

    protected static void increaseLastSearchHistoryId(){_lastSearchHistoryId ++;}
    protected static void increaseLastNewsId(){_lastNewsId ++;}
    protected static void decreaseLastSearchHistoryId(){_lastSearchHistoryId --;}
    protected static void decreaseLastNewsId(){_lastNewsId --;}
    protected static void resetLastSearchHistoryId(){_lastSearchHistoryId = 0;}
    protected static void resetLastNewsId(){_lastNewsId = 0;}


    public DatabaseHelper(Context context){
        super(context, DATABASENAME, null, databaseVersion);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(_CREATEVIEWHISTORY);
        db.execSQL(_CREATESEARCHHISTORYTABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
