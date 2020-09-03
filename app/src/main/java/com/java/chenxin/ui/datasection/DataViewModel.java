package com.java.chenxin.ui.datasection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DataViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is datasection fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}