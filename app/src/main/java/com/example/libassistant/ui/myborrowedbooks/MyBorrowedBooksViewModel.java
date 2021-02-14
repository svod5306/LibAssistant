package com.example.libassistant.ui.myborrowedbooks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.libassistant.BookInfo;
import com.example.libassistant.ReaderInfo;

import java.util.List;

public class MyBorrowedBooksViewModel extends ViewModel {

    private MutableLiveData<List<BookInfo>>  mBorrowedBooks;

    public MyBorrowedBooksViewModel() {
        mBorrowedBooks = new MutableLiveData<>();
    }

    public LiveData<List<BookInfo>> getData() {
        return mBorrowedBooks;
    }
    public void setData(List<BookInfo> bookInfo){
        mBorrowedBooks.setValue(bookInfo);
    }
}