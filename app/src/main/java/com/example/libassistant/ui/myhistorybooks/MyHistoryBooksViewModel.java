package com.example.libassistant.ui.myhistorybooks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.libassistant.BookInfo;

import java.util.List;

public class MyHistoryBooksViewModel extends ViewModel {

    private MutableLiveData<List<BookInfo>> mHistoryBooks;

    public MyHistoryBooksViewModel() {
        mHistoryBooks = new MutableLiveData<>();
    }

    public LiveData<List<BookInfo>> getData() {
        return mHistoryBooks;
    }
    public void setData(List<BookInfo> bookInfo){
        mHistoryBooks.setValue(bookInfo);
    }
}