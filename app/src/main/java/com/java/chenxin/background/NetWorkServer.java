package com.java.chenxin.background;

import com.java.chenxin.data_struct.Constants;
import com.java.chenxin.data_struct.NewsList;
import com.java.chenxin.data_struct.NewsPiece;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkServer {
    private static int _count = 0;
    private static int _searchPage  = 1;
    private static int _searchId = 1;
    private static int _newsIds = 0;
    private static int _paperIds = 0;
    private static int _allIds = 0;
    private static int _pageNum = 100;
    private static int _newsId = 0;
    private static int _paperId = 0;
    private static int _allId = 0;
    private static int _currentId = 0;//下一条要读入的编号
    private static String _lastId = "";

    private static void setCurrentId(String type){
        if(type.equals("news")){
            _currentId = _newsId;
        }
        else if(type.equals("paper")){
            _currentId = _paperId;

        }
        else{
            _currentId = _allId;
        }
    }

    private static void setTypeId(String type){
        if(type.equals("news")){
            _newsId = _currentId;
        }
        else if(type.equals("paper")){
            _paperId = _currentId;
        }
        else{
            _allId = _currentId;
        }
    }
    private static void setSearchId(String type){
        if(type.equals("news")){
            _searchId = _newsIds;
        }
        else if(type.equals("paper")){
            _searchId = _paperIds;

        }
        else{
            _searchId = _allIds;
        }
    }

    private static void setSearchTypeId(String type){
        if(type.equals("news")){
            _newsIds = _searchId;
        }
        else if(type.equals("paper")){
            _paperIds = _searchId;
        }
        else{
            _allIds = _searchId;
        }
    }

    private NetWorkServer(){}
    public static void loadNewsPiece(Observer<NewsPiece> ob, final String id){
        Observable.create(new ObservableOnSubscribe<NewsPiece>() {
            @Override
            public void subscribe(ObservableEmitter<NewsPiece> emitter) throws Exception {
                NewsPiece piece = NetWorkServer.searchById(id);
//                    System.out.println("下拉: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
                emitter.onNext(piece);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
    public static void loadMore(Observer<NewsList> ob, final String type){
        Observable.create(new ObservableOnSubscribe<NewsList>() {
            @Override
            public void subscribe(ObservableEmitter<NewsList> emitter) throws Exception {
                NewsList list = NetWorkServer.viewOldExcuteNew(type);
                    //System.out.println("下拉: " + NetWorkServer.getPageNum() + " " + NetWorkServer.getCount());
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
    public static void reFresh(Observer<NewsList> ob,final String type){
        Observable.create(new ObservableOnSubscribe<NewsList>() {
            @Override
            public void subscribe(ObservableEmitter<NewsList> emitter) throws Exception {
                NewsList list = NetWorkServer.viewNewExcuteNew(type);
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }

    private static NewsList viewOldExcuteNew(String type){
        setCurrentId(type);
        int latestId = getTotal(type);
        int page = (latestId - _currentId) / Constants.PAGESIZE + 1;
        int pt = latestId - Constants.PAGESIZE * page;
        OkHttpClient okHttpClient = new OkHttpClient();
        String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + page + "&size=" + Constants.PAGESIZE;
        Request request = new Request.Builder().url(mUrl).get().build();
        Call call = okHttpClient.newCall(request);
        NewsList list = new NewsList();
        String msg = "";
        try {
            Response response = call.execute();
            msg += response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            setTypeId(type);
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
            setTypeId(type);
            return null;
        }
        NewsList tmplist = new NewsList(jsonObject, type);
        int k = 0;
        while(pt > _currentId){
            k ++;
            pt --;
        }
        while(k < tmplist.getNewsList().size()){
            list.add(tmplist.getNewsList().get(k));
            k ++;
        }
        if(list.getNewsList().size() == Constants.PAGESIZE){
            _currentId -= Constants.PAGESIZE;
            setTypeId(type);
            NewsList.checkNewsList(list);
            return list;
        }
        mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + page + "&size=" + Constants.PAGESIZE;
        request = new Request.Builder().url(mUrl).get().build();
        call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            msg += response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            setTypeId(type);
            return null;
        }
        jsonObject = null;
        try{
            jsonObject =new JSONObject(msg);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        if(jsonObject == null){
            setTypeId(type);
            return null;
        }
        tmplist = new NewsList(jsonObject, type);
        k = 0;
        while(tmplist.getNewsList().size() < Constants.PAGESIZE){
            list.add(tmplist.getNewsList().get(k++));
        }
        _currentId -= Constants.PAGESIZE;
        setTypeId(type);
        NewsList.checkNewsList(list);
        return list;
    }


    private static NewsList viewNewExcuteNew(String type){
        setCurrentId(type);
        OkHttpClient okHttpClient = new OkHttpClient();
        String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=1&size=" + Constants.PAGESIZE;
        final Request request = new Request.Builder().url(mUrl).get().build();
        final Call call = okHttpClient.newCall(request);
        String msg = "";
        try {
            Response response = call.execute();
            msg += response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            setTypeId(type);
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
            setTypeId(type);
            return null;
        }
        NewsList list = new NewsList(jsonObject, type);
        _currentId = list.getTotal() - Constants.PAGESIZE;
        setTypeId(type);
        NewsList.checkNewsList(list);
        return list;
    }

//
    protected static NewsList search(String[] key, String type){
        setSearchId(type);
        _searchId = getTotal(type);
        setSearchTypeId(type);
        return searchExcuteNew(key, type);
    }
    protected static NewsList searchExcuteNew(String[] key, String type){
        setSearchId(type);
        final int SEARCHSIZE = Constants.PAGESIZE * 5;

        int latestId = getTotal(type);
        int page = (latestId - _searchId) / SEARCHSIZE + 1;
        int pt = latestId - SEARCHSIZE * (page - 1);
        NewsList list = new NewsList();

        OkHttpClient okHttpClient = new OkHttpClient();
        String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + page + "&size=" + SEARCHSIZE;
        Request request = new Request.Builder().url(mUrl).get().build();
        Call call = okHttpClient.newCall(request);

        String msg = "";
        try {
            Response response = call.execute();
            if(getTotal(type) != latestId){
                return searchExcuteNew(key, type);
            }
            msg += response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            setSearchTypeId(type);
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
            setSearchTypeId(type);
            return null;
        }
        NewsList tmplist = new NewsList(jsonObject, type);
        int k = pt - _searchId;
        //pt == searchid
        while(k < tmplist.getNewsList().size()){
            for(int i = 0; i < key.length; i ++){
                if(key[i].equals("")) continue;
                if(!tmplist.getNewsList().get(k).search(key[i])) continue;
                list.add(tmplist.getNewsList().get(k));
                break;
            }
            k ++;
            _searchId --;
            if(list.getNewsList().size() >= Constants.PAGESIZE) break;
        }
        if(list.getNewsList().size() == Constants.PAGESIZE){
            setSearchTypeId(type);
            NewsList.checkNewsList(list);
            return list;
        }
        while(list.getNewsList().size() < Constants.PAGESIZE && _searchId > 1){
            mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + ++page + "&size=" + SEARCHSIZE;
            request = new Request.Builder().url(mUrl).get().build();
            call = okHttpClient.newCall(request);
            msg = "";
            try {
                Response response = call.execute();
                msg += response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                setSearchTypeId(type);
                return null;
            }
            jsonObject = null;
            try{
                jsonObject =new JSONObject(msg);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            if(jsonObject == null){
                setSearchTypeId(type);
                return null;
            }
            tmplist = new NewsList(jsonObject, type);
            k = 0;
            while(k < tmplist.getNewsList().size()){
                for(int i = 0; i < key.length; i ++){
                    if(key[i].equals("")) continue;
                    if(!tmplist.getNewsList().get(k).search(key[i])) continue;
                    list.add(tmplist.getNewsList().get(k));
                    break;
                }
                k ++;
                _searchId --;
                if(list.getNewsList().size() >= Constants.PAGESIZE) break;
            }
        }
        setSearchTypeId(type);
        NewsList.checkNewsList(list);
        return list;
    }


    private static NewsPiece searchById(String id){
        List<NewsPiece> tmpNewsList = NewsPiece.find(NewsPiece.class, "_uid = ?", id);
        if(tmpNewsList == null || tmpNewsList.size() == 0){
            try{
                OkHttpClient okHttpClient = new OkHttpClient();
                String url = "https://covid-dashboard.aminer.cn/api/event/" + id + "?id=" + id;
                final Request request1 = new Request.Builder().url(url).get().build();
                final Call call = okHttpClient.newCall(request1);
                String msg = "";
                Response response = call.execute();
                msg += response.body().string();
                JSONObject jsonObject = new JSONObject(msg);
                NewsPiece piece = new NewsPiece(jsonObject.getJSONObject("data"), true);
                piece.save();
                return piece;
            }
            catch (Exception e){

            }
            return null;
        }
        else{
            System.out.println("already read");
            return tmpNewsList.get(0);
        }
    }
    private static int getTotal(String type){
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
//    private static NewsList searchExcute(String[] key, String type){
//        int total = getTotal(type);
//        _searchPage = (total + Constants.PAGESIZE - 1) / Constants.PAGESIZE;
//        NewsList list = new NewsList();
//        OkHttpClient okHttpClient = new OkHttpClient();
//        while(list.getSize() < Constants.PAGESIZE || _searchPage < 1){
//            System.out.println("loop");
//            try{
//                String url = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + _searchPage-- + "&size=" + Constants.PAGESIZE * 3;
//                final Request request1 = new Request.Builder().url(url).get().build();
//                final Call call = okHttpClient.newCall(request1);
//                String msg = "";
//                Response response = call.execute();
//                msg += response.body().string();
//                JSONObject jsonObject = new JSONObject(msg);
//                NewsList tmplist = new NewsList(jsonObject, type);
//                List<NewsPiece> pieces = tmplist.getNewsList();
//                for(int j = 0; j < pieces.size(); j ++){
//                    for(int i = 0; i < key.length; i ++) {
//                        if (key[i].equals("")) continue;
//                        if (!pieces.get(j).search(key[i])) continue;
//                        list.add(pieces.get(j));
//                        break;
//                    }
//                    if(list.getSize() == Constants.PAGESIZE){
//                        break;
//                    }
//                }
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return list;
//    }
//    private static NewsList viewOldExcute(String type){
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + (_pageNum - ++_count) + "&size=" + Constants.PAGESIZE;
//        final Request request = new Request.Builder().url(mUrl).get().build();
//        final Call call = okHttpClient.newCall(request);
//        String msg = "";
//        try {
//            Response response = call.execute();
//            msg += response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        JSONObject jsonObject = null;
//        try{
//            jsonObject =new JSONObject(msg);
//        }
//        catch(JSONException e){
//            e.printStackTrace();
//        }
//        if(jsonObject == null){
//            return null;
//        }
//        return new NewsList(jsonObject, type);
//    }
//    private static NewsList viewNewExcute(String type){
//        OkHttpClient okHttpClient = new OkHttpClient();
//        try {
//            int total = getTotal(type);
//            _pageNum = 1 + total / Constants.PAGESIZE;//定位到倒数第二页(整除时最后一页）由于从1开始计数 所以+1
//            _count = 0;
//            String msg = "";
//            Response response = null;
//            JSONObject jsonObject = null;
//            if(total % Constants.PAGESIZE == 0){//直接打印最后一页
//                String mUrl = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + _pageNum + "&size=" + Constants.PAGESIZE;
//                final Request rrequest = new Request.Builder().url(mUrl).get().build();
//                final Call ccall = okHttpClient.newCall(rrequest);
//                msg = "";
//                response = ccall.execute();
//                msg += response.body().string();
//                jsonObject = new JSONObject(msg);
//                return new NewsList(jsonObject, type);
//            }
//            else{//打印倒数两页
//                _count ++;
//                String url1 = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + (_pageNum - 1) + "&size=" + Constants.PAGESIZE;
//                final Request request1 = new Request.Builder().url(url1).get().build();
//                final Call call1 = okHttpClient.newCall(request1);
//                msg = "";
//                response = call1.execute();
//                msg += response.body().string();
//                jsonObject = new JSONObject(msg);
//                NewsList list1 = new NewsList(jsonObject, type);
//
//                String url2 = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + (_pageNum) + "&size=" + Constants.PAGESIZE;
//                final Request request2 = new Request.Builder().url(url2).get().build();
//                final Call call2 = okHttpClient.newCall(request2);
//                msg = "";
//                response = call2.execute();
//                msg += response.body().string();
//                jsonObject = new JSONObject(msg);
//                NewsList list2 = new NewsList(jsonObject, type);
//                list1.merge(list2);
//                return list1;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
