package com.java.chenxin.data_struct;

import java.io.Serializable;

public class DataPerDay implements Serializable {
    String date;
    public int confirmed;
    public int cured;
    public int dead;
    public DataPerDay(){}
    public DataPerDay(String s, int confirmed, int cured, int dead){
        this.date = s;
        this.confirmed = confirmed;
        this.cured = cured;
        this.dead = dead;
    }
    public void show(){
        System.out.println("date: " + date + " confirmed: " + confirmed + " cured: " + cured + " dead: " + dead);
    }
}
