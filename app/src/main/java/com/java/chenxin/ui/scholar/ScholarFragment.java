package com.java.chenxin.ui.scholar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.java.chenxin.R;
import com.java.chenxin.background.DataServer;
import com.java.chenxin.data_struct.DataPerDay;
import com.java.chenxin.data_struct.Entity;
//import com.java.chenxin.ui.news.RefreshMode;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ScholarFragment extends Fragment {
    public Observer<List<DataPerDay>> observerDataMap;
    public Observer<List<Entity>> entityDataMap;
    List<DataPerDay> list;
    List<Entity> entityList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(com.java.chenxin.R.layout.fragment_scholar,
                container, false);

//        Button button1 = root.findViewById(R.id.button1);
//        Button button2 = root.findViewById(R.id.button2);
//        final TextView textView = root.findViewById(R.id.textview);
//
//        button1.setOnClickListener(this);
//        button2.setOnClickListener(this);
//        observerDataMap = new Observer<List<DataPerDay>>() {
//            @Override
//            // 绑定激活函数
//            public void onSubscribe(Disposable d) {}
//
//            @Override
//            public void onNext(List<DataPerDay> l) {
//                list = l;
//                String msg = "";
//                for(int i = 0; i < list.size(); i ++){
//                    msg += list.get(i).date + " " + list.get(i).dead + "\n";
//                }
//                textView.setText(msg);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
////                System.out.println("success!" + map.getData("China", 1).toString());
//            }
//        };
//        entityDataMap = new Observer<List<Entity>>() {
//            @Override
//            // 绑定激活函数
//            public void onSubscribe(Disposable d) {}
//
//            @Override
//            public void onNext(List<Entity> l) {
//                entityList = l;
//                for(int i = 0; i < l.size(); i ++){
//                    entityList.get(i).show();
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
////                System.out.println("success!" + map.getData("China", 1).toString());
//            }
//        };

        return root;
    }

//    @Override
//    public void onClick(View view) {
//        if(view.getId() == R.id.button1){
//            System.out.println("button1");
//            DataServer.getEntityData(entityDataMap,"疫苗");
////            DataServer.getDataPerDay(observerDataMap, "India", 5);
//        }
//        if(view.getId() == R.id.button2){
//            System.out.println("button2");
//            DataServer.getEntityData(entityDataMap,"黄热病病毒");
//        }
//    }
}
