package com.golden.antelope;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.*;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class InfoActivity extends AppCompatActivity {

    public static final String PRIVACY_EXTRA_KEY = "antelope_policy";

    private static final String PRIVACY_LINK = "file:///android_asset/antelope_policy.html";
    private static final String TOAST_EXIT_TEXT = "Tap twice for go back";
    private static final String PARAM = "";
    private static final String RELOAD_FLAG = "reload://";

    private WebView webView;
    private boolean isDoubleClick;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        Intent intent = getIntent();
        boolean privacy = intent.getBooleanExtra(PRIVACY_EXTRA_KEY, false);
        webView = findViewById(R.id.webView);
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new ProWebViewClient());
        if (privacy) {
            webView.loadUrl(PRIVACY_LINK);
        } else {
            if (isNetworkConnected()) {
                webView.loadUrl(PARAM);
            } else {
                webView.loadUrl(PRIVACY_LINK);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else if (isDoubleClick) {
            finish();
            return;
        }
        this.isDoubleClick = true;
        Toast.makeText(this, TOAST_EXIT_TEXT, Toast.LENGTH_SHORT).show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isDoubleClick = false;
            }
        }, 1000);
    }

    public boolean isNetworkConnected() {
        return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    private class ProWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return handleUri(view, url);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return handleUri(view, request.getUrl().toString());
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError err) {
            super.onReceivedError(view, req, err);
            webView.loadUrl(PRIVACY_LINK);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            webView.loadUrl(PRIVACY_LINK);
        }

        private boolean handleUri(WebView view, final String url) {
            if (url.contains(RELOAD_FLAG)) {
                Intent intent = new Intent(InfoActivity.this, InfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                InfoActivity.this.startActivity(intent);
                System.exit(0);
                return true;
            } else {
                return false;
            }
        }
    }
}