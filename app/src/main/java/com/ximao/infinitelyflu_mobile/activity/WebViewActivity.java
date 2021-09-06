package com.ximao.infinitelyflu_mobile.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.ximao.infinitelyflu_mobile.R;


/**
 * @author ximao
 * @date 2021/8/29
 * WebViewActivity 网页Activity
 */
public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl("http://main.m.taobao.com?sprefer=sypc00");
        webView.setWebViewClient(new WebViewClient() {
            /**
             * 此处重写onReceivedSslError而不是shouldOverrideUrlLoading方法目的是为了不打开淘宝App
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }

}