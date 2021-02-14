package com.example.libassistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyPwdInputDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements WebService {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    public static String tagID;
    public static String userpassword;
    private SharedPreferences preferences;
    private String return_data;
    private FingerprintManager manager;
    private KeyguardManager keyguardManager;
    private final static int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0;
    private CancellationSignal cancellationSignal;

    @Override
    protected void onStart() {
        super.onStart();
        nfcAdapter=NfcAdapter.getDefaultAdapter(this);
        pendingIntent=PendingIntent.getActivity(this,0,new Intent(this,getClass()),0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(nfcAdapter!=null)
            nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
        preferences=getSharedPreferences("settings",0);
        if(preferences.getBoolean("useFinger",false)==true){
            doVerifyFinger();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfcAdapter!=null)
            nfcAdapter.disableForegroundDispatch(this);
        if(cancellationSignal!=null)
            cancellationSignal.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences=getSharedPreferences("settings",0);
        if(preferences.getBoolean("useFinger",false)==true){
            doVerifyFinger();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        tagID="";
        byte[] tagbytes=tag.getId();
        for(int i=0;i<tagbytes.length;i++){
            tagID+=byteToHexString(tagbytes[i]);
        }
        String aeskey=getString(R.string.aeskey);
        preferences=getSharedPreferences("settings",0);

        if(AESUtils.decrypt(getString(R.string.aeskey), preferences.getString("uid","")).equals(tagID)){
            //此处插入读取密码的代码
            try {
                userpassword = AESUtils.decrypt(getString(R.string.aeskey), preferences.getString("password",""));
                StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String server=getString(R.string.libserver);
                HashMap<String,String> mp=new HashMap<String, String>();
                mp.put("service","verifyuser");
                mp.put("uid",tagID);
                mp.put("password",userpassword);
                GoodHttp goodHttp=new GoodHttp(mp,server);
                String call=CallService(goodHttp);
                switch (call){
                    case "0":
                        break;
                    case "-1":
                        break;
                    default:
                        if(cancellationSignal!=null){
                            cancellationSignal.cancel();
                        }
                        Intent intent2=new Intent(this,ContentActivity.class);
                        intent2.putExtra("data",return_data);
                        startActivity(intent2);
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            //此处显示输入对话框
            final MyPwdInputDialog myPwdInputDialog=new MyPwdInputDialog(this).builder();
            myPwdInputDialog.setTitle("第一次使用请输入您的预留密码");
            myPwdInputDialog.setPasswordListener(new MyPwdInputDialog.OnPasswordResultListener() {
                @Override
                public void onPasswordResult(String s) {
                    try{
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("uid",AESUtils.encrypt(getString(R.string.aeskey),tagID));
                        editor.putString("password",AESUtils.encrypt(getString(R.string.aeskey),s));
                        editor.commit();
                        myPwdInputDialog.dismiss();
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            myPwdInputDialog.show();
        }
    }
    private final static char[] HEXDIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /**
     * 将byte类型数字转成成16进制字符串
     * @explain
     * @param b 表述范围
     * @return
     */
    public static String byteToHexString(byte b) {
        if (127 < b || b < -128) return "";
        // 确保n是正整数
        int n = b < 0 ? 256 + b : b;
        return "" + HEXDIGITS[n / 16] + HEXDIGITS[n % 16];
    }
    @Override
    public String CallService(GoodHttp request) {

        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("loading");
        loadingDialog.show();
        try {
            return_data=request.CallService();
            Gson gson=new Gson();
            ServiceReturnData<ReaderInfo> result=gson.fromJson(return_data, new TypeToken<ServiceReturnData<ReaderInfo>>(){}.getType());
            String isExit=result.getStatus();

            loadingDialog.dismiss();
            if(isExit.equals("true")){
                return return_data;
            }
            else{
                MyAlertDialog alertDialog=new MyAlertDialog(this).builder();
                alertDialog.setTitle("一卡通或密码错误");
                alertDialog.setMsg("请确认一卡通已经在图书馆注册\n请确认你的密码正确，并重新刷卡输入密码");
                alertDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                alertDialog.show();
                return "0";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            loadingDialog.dismiss();
            MyAlertDialog alertDialog2=new MyAlertDialog(this).builder();
            alertDialog2.setTitle("网络连接错误");
            alertDialog2.setMsg("请确认网络正常，或咨询图书馆");
            alertDialog2.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            alertDialog2.show();
            return "-1";
        }
    }
    private void doVerifyFinger(){
        manager=(FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        keyguardManager=(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);

        cancellationSignal=new CancellationSignal();
        FingerprintManager.AuthenticationCallback mmSelfCancelled=new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                //showAuthenticationScreen();
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                //Toast.makeText(MainActivity.this, helpString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                cancellationSignal.cancel();
                verifyUser();
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(MainActivity.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
            }
        };
        manager.authenticate(null,cancellationSignal,0,mmSelfCancelled,null);
    }
    private void showAuthenticationScreen(){
        Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("指纹登录", "请输入指纹");

        if (intent != null) {
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
            Toast.makeText(this, "提示", Toast.LENGTH_SHORT).show();
        }
    }
    private void verifyUser(){
        String aeskey=getString(R.string.aeskey);
        preferences=getSharedPreferences("settings",0);

        if(!AESUtils.decrypt(getString(R.string.aeskey), preferences.getString("uid","")).equals("")){
            //此处插入读取密码的代码
            try {
                tagID=AESUtils.decrypt(getString(R.string.aeskey), preferences.getString("uid",""));
                userpassword = AESUtils.decrypt(getString(R.string.aeskey), preferences.getString("password",""));
                StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String server=getString(R.string.libserver);
                HashMap<String,String> mp=new HashMap<String, String>();
                mp.put("service","verifyuser");
                mp.put("uid",tagID);
                mp.put("password",userpassword);
                GoodHttp goodHttp=new GoodHttp(mp,server);
                String call=CallService(goodHttp);
                switch (call){
                    case "0":
                        break;
                    case "-1":
                        break;
                    default:
                        Intent intent2=new Intent(this,ContentActivity.class);
                        intent2.putExtra("data",return_data);
                        startActivity(intent2);
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            //此处显示输入对话框
            final MyPwdInputDialog myPwdInputDialog=new MyPwdInputDialog(this).builder();
            myPwdInputDialog.setTitle("第一次使用请输入您的预留密码");
            myPwdInputDialog.setPasswordListener(new MyPwdInputDialog.OnPasswordResultListener() {
                @Override
                public void onPasswordResult(String s) {
                    try{
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("uid",AESUtils.encrypt(getString(R.string.aeskey),tagID));
                        editor.putString("password",AESUtils.encrypt(getString(R.string.aeskey),s));
                        editor.commit();
                        myPwdInputDialog.dismiss();
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            myPwdInputDialog.show();
        }
    }
}
