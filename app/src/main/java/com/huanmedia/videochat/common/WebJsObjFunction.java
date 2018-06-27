package com.huanmedia.videochat.common;

import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class WebJsObjFunction extends Object {

    private WebView mWebView;

    public WebJsObjFunction(WebView webView) {
        this.mWebView = webView;
    }

    /**
     * 发送基础数据
     *
     * @param methodName
     * @param data
     */
    public void setBaseData(String methodName, String data) {
        mWebView.post(() -> {
            if (mWebView != null)
                mWebView.loadUrl("javascript:" + methodName + "('" + data + "')");
        });
    }

    /**
     * 获取抽奖数据
     */
    @JavascriptInterface
    public void getLuckInfo() {
        setLuckInfo("测试");
    }

    /**
     * 设置抽奖数据
     *
     * @param data
     */
    public void setLuckInfo(String data) {
        setBaseData("setLuckInfo", data);
    }



}
