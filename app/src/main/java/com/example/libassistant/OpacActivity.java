package com.example.libassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.ProgressWheel;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OpacActivity extends AppCompatActivity implements WebService {

    private String return_date;
    private List<OpacData> mOpacData=new ArrayList<>();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opac);
        recyclerView=findViewById(R.id.opac_books);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }
    public void NlpSearch(View view){
        return_date="";
        ImageView opacImg=findViewById(R.id.opac_image);
        EditText editText=findViewById(R.id.opac_text);
        String txt=editText.getText().toString();

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String server=getString(R.string.libserver);
        HashMap<String,String> mp=new HashMap<String, String>();
        mp.put("service","opac");
        mp.put("txt",txt);
        GoodHttp goodHttp=new GoodHttp(mp,server);
        CallService(goodHttp);
        editText.setText("");
        ShowOpac();
    }

    private void ShowOpac(){
        Gson opacJson=new Gson();
        ServiceReturnData<List<OpacData>> result=opacJson.fromJson(return_date,new TypeToken<ServiceReturnData<List<OpacData>>>(){}.getType());
        ImageView imgOpac=findViewById(R.id.opac_image);
        if(result.getStatus().equals("true")){
            mOpacData=(List<OpacData>)result.getData();
            imgOpac.setVisibility(View.GONE);
            LinearLayoutManager layoutManager=new LinearLayoutManager(this);
            OpacDataAdapter opacDataAdapter=new OpacDataAdapter(mOpacData);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(opacDataAdapter);
        }else{
            imgOpac.setVisibility(View.VISIBLE);
            MyAlertDialog dlg=new MyAlertDialog(this).builder();
            dlg.setTitle("提示");
            dlg.setMsg("检索不到满足您要求的数据");
            dlg.show();
            mOpacData.clear();
            //recyclerView.cl
        }

    }

    @Override
    public String CallService(GoodHttp request) {
        try{
            return_date=request.CallService();
            int i=0;
            return return_date;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
