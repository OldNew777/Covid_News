package com.java.chenxin.data_struct;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Scholar implements Serializable {
    private String _avatar;
    private String _id;
    private String _name;
    private String _nameZh;
    private double _activity;
    private double _citations;
    private double _diversity;
    private double _gindex;
    private double _hindex;
    private double _newStar;
    private double _pubs;
    private double _risingStar;
    private double _sociability;
    private String _address;
    private String _affiliation;
    private String _affiliationZh;
    private String _bio;
    private String _edu;
    private String _homepage;
    private String _note;
    private String _phone;
    private String _position;
    private String _work;
    private boolean _isPassedaway;

    public String getImgUrl(){return _avatar;}
    public String get_id(){return _id;}
    public String getName(){return _name;}
    public String getNameZh(){return _nameZh;}
    public String getAdress(){return _address;}
    public String getAffiliation(){return _affiliation;}
    public String getAffiliationZh(){return _affiliationZh;}
    public String getBio(){return _bio;}
    public String getEdu(){return _edu;}
    public String getHomepage(){return _homepage;}
    public String getNote(){return _note;}
    public String getPhone(){return _phone;}
    public String getPosition(){return _position;}
    public String getWork(){return _work;}
    public boolean getIsPassaway(){return _isPassedaway;}

    public Scholar(){
    }

    public void show(){
        System.out.println("name: " + _name);
        System.out.println("name_zh: " + _nameZh);
        System.out.println("imgurl " + _avatar);
        System.out.println("address: " + _address);
        System.out.println("affiliation: " + _affiliation);
        System.out.println("affiliation_zh: " + _affiliationZh);
        System.out.println("bio: " + _bio);
        System.out.println("Edu: " + _edu);
        System.out.println("note: " + _note);
        System.out.println("phone: " + _phone);
        System.out.println("position: " + _position);
        System.out.println("work: " + _work);
        System.out.println("is Passaway: " + _isPassedaway);
    }

    public Scholar(JSONObject data){
        try {
            _isPassedaway = data.getBoolean("is_passedaway");
        } catch (JSONException e) {}
        try{
            _avatar = data.getString("avatar");
        }catch (JSONException e) {}
        try{
            _id = data.getString("id");
        }catch (JSONException e){ }
        try{
            JSONObject indices = data.getJSONObject("indices");
            try{
                _activity = indices.getDouble("activity");
            }catch (JSONException e){}
            try{
                _citations = indices.getDouble("citations");
            }catch (JSONException e){}
            try{
                _diversity = indices.getDouble("diversity");
            }catch (JSONException e){}
            try{
                _gindex = indices.getDouble("gindex");
            }catch (JSONException e){}
            try{
                _hindex = indices.getDouble("hindex");
            }catch (JSONException e){}
            try{
                _newStar = indices.getDouble("newStar");
            }catch (JSONException e){}
            try{
                _pubs = indices.getDouble("pubs");
            }catch (JSONException e){}
            try{
                _risingStar = indices.getDouble("risingStar");
            }catch (JSONException e){}
            try{
                _sociability = indices.getDouble("sociability");
            }catch (JSONException e){}

        }catch (JSONException e){ }
        try{
            _name = data.getString("name");
        }catch(JSONException e){}
        try{
            _nameZh = data.getString("name_zh");
        }catch(JSONException e){}
        try{
            JSONObject profile = data.getJSONObject("profile");
            try{
                _address = profile.getString("address");
            }catch(JSONException e){}
            try{
                _affiliation = profile.getString("affiliation");
            }catch(JSONException e){}
            try{
                _affiliationZh = profile.getString("affiliation_zh");
            }catch(JSONException e){}
            try{
                _bio = profile.getString("bio");
            }catch(JSONException e){}
            try{
                _edu = profile.getString("edu");
            }catch(JSONException e){}
            try{
                _homepage = profile.getString("homepage");
            }catch(JSONException e){}
            try{
                _note = profile.getString("note");
            }catch(JSONException e){}
            try{
                _phone = profile.getString("phone");
            }catch(JSONException e){}
            try{
                _position = profile.getString("position");
            }catch(JSONException e){}
            try{
                _work = profile.getString("work");
            }catch(JSONException e){}
        }catch(JSONException e){}
    }
}

