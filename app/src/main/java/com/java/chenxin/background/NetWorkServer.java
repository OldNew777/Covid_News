package com.java.chenxin.background;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkServer {
    private static int _count = 0;
    private static int _searchPage  = 1;
    private static int _readPage = 1;
    private static int _total = 1;
    private static int _pageNum = 100;
    private final static int _SIZE = 20;
    private static String _lastId = "";
    private static NetWorkServer _netWorkServer = new NetWorkServer();

    public static int getCount(){
        return _count;
    }
    public static int getPageNum(){
        return _pageNum;
    }
    public static NetWorkServer getInstance(){
        return _netWorkServer;
    }
    private NetWorkServer(){}
    public static NewsList excute(String type){ //这个是用来实验的 最后会删掉
        OkHttpClient okHttpClient = new OkHttpClient();
        //这个是用来实验的 最后会删掉
        final Request request = new Request.Builder().url("https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=1&size=1").get().build();
        final Call call = okHttpClient.newCall(request);
        String msg = "";
        try {
            Response response = call.execute();
            msg += response.body().string();
            System.out.println(msg);
            JSONObject jsonObject = new JSONObject(msg);
            return new NewsList(jsonObject,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NewsList viewOldExcuteNew(String type){
        int newTotal = getTotal(type);
        int newPageNum = (newTotal - _total) / _SIZE;
        System.out.println("newPagenum" + newPageNum);
        _readPage += newPageNum + 1;

        if((newTotal - _total) % _SIZE == 0){//新的新闻条数是size的整数，可以直接阅读下一页
            OkHttpClient okHttpClient = new OkHttpClient();
            String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + _readPage + "&size=" + _SIZE;

            Request request = new Request.Builder().url(mUrl).get().build();
            Call call = okHttpClient.newCall(request);
            String msg = "";
            try {
                Response response = call.execute();
                if(getTotal(type) != newTotal){
                    return viewOldExcuteNew(type);
                }
                msg += response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            JSONObject jsonObject = null;
            try{
                jsonObject =new JSONObject(msg);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            if(jsonObject == null){
                return null;
            }
            NewsList list = new NewsList(jsonObject, type);
            _total = newTotal;
            return list;
        }else {//新的新闻条数不是size的整数，需要在下一页查找到最新消息
            OkHttpClient okHttpClient = new OkHttpClient();
            NewsList list = new NewsList();
            String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + _readPage + "&size=" + (_SIZE * 2);
            final Request request = new Request.Builder().url(mUrl).get().build();
            final Call call = okHttpClient.newCall(request);
            String msg = "";
            try {
                Response response = call.execute();
                if(getTotal(type) != newTotal){
                    return viewOldExcuteNew(type);
                }
                msg += response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NewsList tmplist = new NewsList(jsonObject, type);
            List<NewsPiece> pieces = tmplist.getNewsList();
            int k = 0;
            while (!pieces.get(k).get_id().equals(_lastId)) {// 掠过  直接找到上一次的末尾
                k++;
            }
            k++;
            while (list.getNewsList().size() < 20) {
                list.add(pieces.get(k));
                k++;
            }
            _lastId = list.getNewsList().get(list.getNewsList().size() - 1).get_id();

            return list;
        }
    }

    public static NewsList viewNewExcuteNew(String type){
        OkHttpClient okHttpClient = new OkHttpClient();
        String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=1&size=" + _SIZE;
        final Request request = new Request.Builder().url(mUrl).get().build();
        final Call call = okHttpClient.newCall(request);
        String msg = "";
        try {
            Response response = call.execute();
            msg += response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        JSONObject jsonObject = null;
        try{
            jsonObject =new JSONObject(msg);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        if(jsonObject == null){
            return null;
        }
        NewsList list = new NewsList(jsonObject, type);
        _lastId = list.getNewsList().get(list.getNewsList().size() - 1).get_id();
        _total = list.getTotal();
        _readPage = 1;
        return list;
    }

    public static NewsList viewOldExcute(String type){
        OkHttpClient okHttpClient = new OkHttpClient();
        String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + (_pageNum - ++_count) + "&size=" + _SIZE;
        final Request request = new Request.Builder().url(mUrl).get().build();
        final Call call = okHttpClient.newCall(request);
        String msg = "";
        try {
            Response response = call.execute();
            msg += response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        JSONObject jsonObject = null;
        try{
            jsonObject =new JSONObject(msg);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        if(jsonObject == null){
            return null;
        }
        return new NewsList(jsonObject, type);
    }
    public static NewsList viewNewExcute(String type){
        OkHttpClient okHttpClient = new OkHttpClient();
        try {
            int total = getTotal(type);
            _pageNum = 1 + total / _SIZE;//定位到倒数第二页(整除时最后一页）由于从1开始计数 所以+1
            _count = 0;
            String msg = "";
            Response response = null;
            JSONObject jsonObject = null;
            if(total % _SIZE == 0){//直接打印最后一页
                String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + _pageNum + "&size=" + _SIZE;
                final Request rrequest = new Request.Builder().url(mUrl).get().build();
                final Call ccall = okHttpClient.newCall(rrequest);
                msg = "";
                response = ccall.execute();
                msg += response.body().string();
                jsonObject = new JSONObject(msg);
                return new NewsList(jsonObject, type);
            }
            else{//打印倒数两页
                _count ++;
                String url1 = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + (_pageNum - 1) + "&size=" + _SIZE;
                final Request request1 = new Request.Builder().url(url1).get().build();
                final Call call1 = okHttpClient.newCall(request1);
                msg = "";
                response = call1.execute();
                msg += response.body().string();
                jsonObject = new JSONObject(msg);
                NewsList list1 = new NewsList(jsonObject, type);

                String url2 = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + (_pageNum) + "&size=" + _SIZE;
                final Request request2 = new Request.Builder().url(url2).get().build();
                final Call call2 = okHttpClient.newCall(request2);
                msg = "";
                response = call2.execute();
                msg += response.body().string();
                jsonObject = new JSONObject(msg);
                NewsList list2 = new NewsList(jsonObject, type);
                list1.merge(list2);
                return list1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static NewsList search(String[] key, String type){
        _searchPage = 1;
        return searchExcuteNew(key, type);
    }
    public static NewsList searchExcuteNew(String[] key, String type){
        int total = getTotal(type);
        NewsList list = new NewsList();
        OkHttpClient okHttpClient = new OkHttpClient();
        while(list.getNewsList().size() < _SIZE && _searchPage < total){
            System.out.println("loop");
            try{
                String url = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + _searchPage++ + "&size=" + _SIZE * 3;
                final Request request1 = new Request.Builder().url(url).get().build();
                final Call call = okHttpClient.newCall(request1);
                String msg = "";
                Response response = call.execute();
                msg += response.body().string();
//                System.out.println(msg);
                JSONObject jsonObject = new JSONObject(msg);
                NewsList tmplist = new NewsList(jsonObject, type);
                List<NewsPiece> pieces = tmplist.getNewsList();
                for(int j = 0; j < pieces.size(); j ++){
                    for(int i = 0; i < key.length; i ++) {
                        if (key[i].equals("")) continue;
//                        System.out.println(pieces.get(j).getContent());
                        if (!pieces.get(j).search(key[i])) continue;
                        list.add(pieces.get(j));
                        break;
                    }
                    if(list.getSize() == _SIZE){
                        break;
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }
    public static NewsList searchExcute(String[] key, String type){
        int total = getTotal(type);
        _searchPage = (total + _SIZE - 1) / _SIZE;
        NewsList list = new NewsList();
        OkHttpClient okHttpClient = new OkHttpClient();
        while(list.getSize() < _SIZE || _searchPage < 1){
            System.out.println("loop");
            try{
                String url = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + _searchPage-- + "&size=" + _SIZE * 3;
                final Request request1 = new Request.Builder().url(url).get().build();
                final Call call = okHttpClient.newCall(request1);
                String msg = "";
                Response response = call.execute();
                msg += response.body().string();
                JSONObject jsonObject = new JSONObject(msg);
                NewsList tmplist = new NewsList(jsonObject, type);
                List<NewsPiece> pieces = tmplist.getNewsList();
                for(int j = 0; j < pieces.size(); j ++){
                    for(int i = 0; i < key.length; i ++) {
                        if (key[i].equals("")) continue;
                        if (!pieces.get(j).search(key[i])) continue;
                        list.add(pieces.get(j));
                        break;
                    }
                    if(list.getSize() == _SIZE){
                        break;
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }
    public static int getTotal(String type){
        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            String tmpUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=1&size=1"; //随便找一页 找到总数
            final Request request = new Request.Builder().url(tmpUrl).get().build();
            final Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            String msg = "";
            msg = response.body().string();
            if(msg.equals("")){
                return -1;
            }
            JSONObject jsonObject =new JSONObject(msg);
            if(jsonObject == null){
                return -1;
            }
            JSONObject pagination = (JSONObject) jsonObject.get("pagination");
            return pagination.getInt("total");}
        catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
}
