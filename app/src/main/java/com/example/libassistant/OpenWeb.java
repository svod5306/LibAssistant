package com.example.libassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OpenWeb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_web);

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        WebView webView=(WebView)findViewById(R.id.nav_to);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.1; MZ-m1 metal Build/LMY47I) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
