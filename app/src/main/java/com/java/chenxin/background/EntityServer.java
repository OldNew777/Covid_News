package com.java.chenxin.background;

import com.java.chenxin.data_struct.Entity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EntityServer {
    public static void getEntityData(Observer<List<Entity>> ob, final String name){
        Observable.create(new ObservableOnSubscribe<List<Entity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Entity>> emitter) throws Exception {
                List<Entity> list = NetWorkServer.searchEntity(name);
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //在io执行上述操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程执行下面操作
                .subscribe(ob);
    }
}
