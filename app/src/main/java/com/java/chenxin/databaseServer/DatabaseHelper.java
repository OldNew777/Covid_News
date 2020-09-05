package com.java.chenxin.databaseServer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASENAME = "database.db";
    public static int databaseVersion = 1;
    private static final String _CREATESEARCHHISTORYTABLE = "CREATE TABLE" + " " + "SearchHistory" +
            "(" +
            "id" + " " + "INTEGER" + " " + "PRIMARY KEY" + " " +"AUTOINCREMENT," +
            "searchContent" + " " + "TEXT" + " " + "NOT NULL" +
            ");";
    private static final String _CREATEVIEWHISTORY = "CREATE TABLE ViewHistory (" +
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
