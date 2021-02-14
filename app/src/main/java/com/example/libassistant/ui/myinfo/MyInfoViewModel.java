package com.example.libassistant.ui.myinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.libassistant.ReaderInfo;

public class MyInfoViewModel extends ViewModel {

    private MutableLiveData<ReaderInfo> readerInfos;

    public MyInfoViewModel() {
        readerInfos = new MutableLiveData<>();
    }

    public LiveData<ReaderInfo> getData() {
        return readerInfos;
    }
    public void setData(ReaderInfo r){
        readerInfos.setValue(r);
    }
}