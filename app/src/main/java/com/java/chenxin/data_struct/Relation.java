package com.java.chenxin.data_struct;

public class Relation {
    private boolean _isFather = false;
    private String _url;
    private String _label;
    private boolean _forward;
    public boolean getIsFather(){
        return _isFather;
    }
    public String getUrl(){
        return _url;
    }
    public String getLabel(){
        return _label;
    }
    public boolean getForward(){
        return _forward;
    }
    public Relation(boolean isFather, String url, String label, boolean forward){
        this._isFather = _isFather;
        this._forward = forward;
        this._url = url;
        this._label = label;
    }

    public void show(){
        System.out.println(_isFather + " " + _url + " " + _label + " " + _forward);
    }

}
