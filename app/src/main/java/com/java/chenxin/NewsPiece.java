package com.java.chenxin;

import java.util.Date;
import java.util.Vector;

public class NewsPiece {
    private String _id, title, date, content, source, rigion;
    double influence;
    NewsType type;
    Vector<String> author;

    public NewsPiece(final String _id, final String title, final String date,
                     Vector<String> author, final String content){
        this._id = _id;
        this.title = title;
        this.date = date;
        this.content = content;
        this.author = author;
    }

    public String getTitle(){
        return title;
    }
}

enum NewsType{
    NEWS, PAPER,
}