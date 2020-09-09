package com.java.chenxin.data_struct;

import com.orm.SugarRecord;

public class Counter extends SugarRecord {
    public int count;
    public String name;
    public Counter(){}
    public Counter(int c, String s){
        count = c;
        name = s;
    }
}
