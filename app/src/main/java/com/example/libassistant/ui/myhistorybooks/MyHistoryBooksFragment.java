package com.example.libassistant.ui.myhistorybooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libassistant.BookInfo;
import com.example.libassistant.BookInfoAdapter;
import com.example.libassistant.ContentActivity;
import com.example.libassistant.R;
import com.example.libassistant.ReaderInfo;

import java.util.List;

public class MyHistoryBooksFragment extends Fragment {

    private MyHistoryBooksViewModel notificationsViewModel;
    private ReaderInfo readerInfo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(MyHistoryBooksViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_history_books, container, false);
        ContentActivity contentActivity=(ContentActivity)getActivity();
        readerInfo=contentActivity.getReaderInfo();
        notificationsViewModel.setData(readerInfo.getHistoryBookList());
        //final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<BookInfo>>() {
            @Override
            public void onChanged(@Nullable List<BookInfo> s) {
                UpdateUI(readerInfo,s,root);
            }
        });

        return root;
    }
    private void UpdateUI(ReaderInfo readerInfo,List<BookInfo> bookInfos,View view){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        BookInfoAdapter historyBoos=new BookInfoAdapter(bookInfos);
        RecyclerView recyclerView=view.findViewById(R.id.listview_history_books);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(historyBoos);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    public void setData(){
        ContentActivity activity=(ContentActivity)getActivity();
        notificationsViewModel.setData(activity.getHistoryBooks());
        UpdateUI(((ContentActivity) getActivity()).getReaderInfo(),((ContentActivity) getActivity()).getHistoryBooks(),getView());
    }
}
