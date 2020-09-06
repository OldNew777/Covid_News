package com.java.chenxin.databaseServer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.java.chenxin.data_struct.Constants;

import java.util.Vector;

//

public class DataBase {

    public static void insertSearchHistory(Context context, String searchHistory){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        String insertString = "INSERT INTO SearchHistory(SearchContent) values(" + searchHistory + ");";
        db.execSQL(insertString);
    }
    public static Vector<String> selectSerchHistory(Context context, int num){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.SEARCHHISTORYTABLENAME, null, null, null, null, null,"id DESC");
        if(cursor.getCount() == 0) return null;
        Vector<String> vec = new Vector<String>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast() && vec.size() < Constants.SEARCHHISTORYSIZE){
            vec.add(cursor.getString(cursor.getColumnIndex("searchContent")));
            cursor.moveToNext();
        }
        return vec;
    }
}
