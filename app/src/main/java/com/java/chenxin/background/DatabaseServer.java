package com.java.chenxin.background;

import com.java.chenxin.data_struct.NewsPiece;

public class DatabaseServer {
    public static void clearNewsHistory(){//清空新闻数据
        NewsPiece.deleteAll(NewsPiece.class);
    }
}
