package com.java.chenxin.databaseServer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBase {

    public static void insertSearchHistory(Context context, String searchHistory){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        String insertString = "";
        db.execSQL(insertString);
    }
}
