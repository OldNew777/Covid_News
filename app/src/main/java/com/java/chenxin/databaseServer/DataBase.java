package com.java.chenxin.databaseServer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//

public class DataBase {

    public static void insertSearchHistory(Context context, String searchHistory){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        String insertString = "INSERT INTO SearchHistory(SearchContent) values(" + searchHistory + ");";
        db.execSQL(insertString);
    }
//    public static String selectSerchHistory(Context context, int num){
//        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
//        Cursor cursor = db.query(DatabaseHelper.SEARCHHISTORYTABLENAME, null, null, null, null, null,null);
//
//    }
}
