package com.example.libassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AssistActivity extends AppCompatActivity implements WebService {

    private List<Msg> msgList=new ArrayList<>();
    private RecyclerView recyclerView;
    private String return_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assist);
        recyclerView=findViewById(R.id.assist_recyclerview);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        Msg msg=new Msg("你好，我是平图小智，很高兴为您服务",Msg.TYPE_RECEIVE);
        msgList.add(msg);
        Intent intent=getIntent();
        String userpic=intent.getStringExtra("userpic");
        MsgAdapter msgAdapter=new MsgAdapter(msgList);
        msgAdapter.userpic=userpic;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(msgAdapter);
        recyclerView.scrollToPosition(msgList.size()-1);
    }
    public void AssistXiaoZhi(View view){
        EditText editText=findViewById(R.id.question_text);
        String question=editText.getText().toString();
        msgList.add(new Msg(question,Msg.TYPE_SEND));

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String server=getString(R.string.libserver);
        editText.setText("");

        HashMap<String,String> mp=new HashMap<String, String>();
        mp.put("service","faq");
        mp.put("question",question);
        GoodHttp goodHttp=new GoodHttp(mp,server);

        CallService(goodHttp);
        recyclerView.scrollToPosition(msgList.size()-1);
    }

    @Override
    public String CallService(GoodHttp request) {
        try{
            return_data=request.CallService();
            Gson gson=new Gson();
            ServiceReturnData<Msg> result=gson.fromJson(return_data,new TypeToken<ServiceReturnData<Msg>>(){}.getType());
            return_data=result.getData().getContent();
            msgList.add(new Msg(return_data,Msg.TYPE_RECEIVE));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
