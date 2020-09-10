package com.java.chenxin.background;

import com.java.chenxin.data_struct.Constants;
import com.java.chenxin.data_struct.DataPerDay;
import com.java.chenxin.data_struct.EpidemicData;
import com.java.chenxin.data_struct.Scholar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

public class ScholarServer {
    public static void getPassawayScholarList(Observer<List<Scholar>> ob){//你唯一需要调用的接口 会返给你所有学者的一个list  也需要建一个observer
        Observable.create(new ObservableOnSubscribe<List<Scholar>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Scholar>> emitter) throws Exception {
                List<Scholar> list = ScholarServer.getScholarList();
                List<Scholar> passawayList  = new ArrayList<Scholar>();
                for(Scholar s : list){
                    if(s.getIsPassaway()){
                        passawayList.add(s);
                    }
                }
                emitter.onNext(passawayList);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
    public static void getScholarList(Observer<List<Scholar>> ob){//你唯一需要调用的接口 会返给你所有学者的一个list  也需要建一个observer
        Observable.create(new ObservableOnSubscribe<List<Scholar>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Scholar>> emitter) throws Exception {
                List<Scholar> list = ScholarServer.getScholarList();
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
    private static List<Scholar> getScholarList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        String mUrl = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";
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
        List<Scholar> scholarList = new ArrayList<Scholar>();
        try{
            jsonObject =new JSONObject(msg);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i = 0; i < jsonArray.length(); i ++){
                scholarList.add(new Scholar(jsonArray.getJSONObject(i)));
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return scholarList;
    }

}
