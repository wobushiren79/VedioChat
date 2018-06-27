package com.huanmedia.videochat.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.repository.net.OkhttpManager;

import java.util.HashMap;

import butterknife.BindView;

public class LocalHtmlWebActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.localhtml_web_progressBar)
    ProgressBar localhtmlWebProgressBar;
    private String mTitle;


    public static Intent getCallingIntent(Context context, String url, String title) {
        Intent intent = new Intent();
        intent.setClass(context, LocalHtmlWebActivity.class);
        intent.putExtra("url", url);
        if (title != null)
            intent.putExtra("title", title);
        return intent;
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_localhtml_web;
    }

    public void initView() {
        String url = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        if (!getIntent().getBooleanExtra("Authentication", false)) {
            if (url != null)
//                webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
                webView.loadUrl(url);
        } else {
            if (url != null) {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authentication", OkhttpManager.getAuthentication());
                webView.loadUrl(url, map);
            }
        }
        setToolBar();

        if (getSupportActionBar() != null && mTitle != null && mTitle.length() > 0) {
            getSupportActionBar().setTitle(mTitle);
        }


        WebSettings settings = webView.getSettings();
        settings.setAppCacheEnabled(false);
        settings.setDatabaseEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);

        WebJsObjFunction jsObjFunction = new WebJsObjFunction( webView);
        webView.addJavascriptInterface(jsObjFunction, "VideoChat");
        webView.requestFocusFromTouch();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (localhtmlWebProgressBar == null) return;
                if (newProgress == 100) {
                    localhtmlWebProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == localhtmlWebProgressBar.getVisibility()) {
                        localhtmlWebProgressBar.setVisibility(View.VISIBLE);
                    }
                    localhtmlWebProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (mTitle == null && getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                }
                super.onReceivedTitle(view, title);
            }
        });

        //webView.loadUrl("file:///android_asset/index.html");
//        webView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

}
