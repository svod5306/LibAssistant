package com.example.libassistant;

import android.os.StrictMode;
import android.webkit.WebSettings;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.Response;

public class GoodHttp {
    private transient Request request;
    private transient RequestBody form;
    private transient OkHttpClient client;
    private transient FormBody.Builder fbuilder;
    private transient Request.Builder rbuilder;
    private HashMap<String,String> args;
    private String server;

    public GoodHttp(HashMap args, String server) {
        this.args = args;
        this.server = server;
        fbuilder=new FormBody.Builder();

        if(! args.isEmpty()){
            Iterator iterator=args.keySet().iterator();
            while (iterator.hasNext()){
                String key=iterator.next().toString();
                String value=args.get(key).toString();
                fbuilder.add(key,value);
            }
            form=fbuilder.build();
            rbuilder=new Request.Builder();
            rbuilder.url(server);
            rbuilder.post(form);
            request = rbuilder.build();
        }
    }

    private void Build(){
        fbuilder=new FormBody.Builder();

        if(! args.isEmpty()){
            Iterator iterator=args.keySet().iterator();
            while (iterator.hasNext()){
                String key=iterator.next().toString();
                String value=args.get(key).toString();
                fbuilder.add(key,value);
            }
            form=fbuilder.build();
            rbuilder=new Request.Builder();
            rbuilder.url(server);
            rbuilder.post(form);
            request = rbuilder.build();
        }
    }

    public Request getRequest() {
        return request;
    }

    public HashMap<String, String> getArgs() {
        return args;
    }

    public void setArgs(HashMap<String, String> args) {
        this.args = args;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void add(String key,String value){
        if(request==null) Build();
        fbuilder.add(key,value);
        form=fbuilder.build();
        rbuilder.post(form);
        request=rbuilder.build();
    }
    public String CallService(){
        if(request==null) Build();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .build();
        try (Response response=client.newCall(request).execute()){
            String return_data=response.body().string();
            return return_data;
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
