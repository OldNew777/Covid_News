package com.java.chenxin.ui.scholar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScholarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ScholarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is scholar fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}