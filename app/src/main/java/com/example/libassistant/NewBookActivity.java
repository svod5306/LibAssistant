package com.example.libassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

public class NewBookActivity extends AppCompatActivity {
    private GoodHttp goodHttp;
    private RecyclerView recyclerView;
    private List<BookInfo> mbooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        HashMap<String,String> mp=new HashMap<String, String>();
        mp.put("service","newbook");
        goodHttp=new GoodHttp(mp,getString(R.string.libserver));
        CallService();
    }

    private void CallService(){
        String return_data=goodHttp.CallService();
        Gson gson=new Gson();
        ServiceReturnData<List<BookInfo>> returnData=gson.fromJson(return_data,new TypeToken<ServiceReturnData<List<BookInfo>>>(){}.getType());
        if(returnData.getStatus().equals("true")){
            mbooks=(List<BookInfo>)returnData.getData();
            NewBookAdapter newBookAdapter=new NewBookAdapter(mbooks);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            recyclerView = findViewById(R.id.lv_newbook);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(newBookAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        }
    }
}
