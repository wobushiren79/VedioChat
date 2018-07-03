package com.huanmedia.videochat.common;

import android.app.Activity;
import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class WebJsObjFunction extends Object {

    private WebView mWebView;
    private Context mContext;
    private String[] whiteList;

    public WebJsObjFunction(Context context, WebView webView) {
        this.mWebView = webView;
        this.mContext = context;
        whiteList = new String[]{
                "/index/luckyh5/voidpraise"
        };
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
     * 关闭当前webactivity
     */
    @JavascriptInterface
    public void exitWebActivity() {
        ((Activity) mContext).finish();
    }

    /**
     * 获取通用数据
     *
     * @param url  地址
     * @param data 请求参数
     */
    @JavascriptInterface
    public void submitCommonInfo(String url, String data) {
        if (url == null || url.length() == 0) {
            submitCommonInfoFail("没有请求地址");
            return;
        }
        //白名单验证
        if (!BuildConfig.DEBUG) {
            boolean isWhiteUrl = false;
            for (String whiteUrl : whiteList) {
                if (url.contains(whiteUrl))
                    isWhiteUrl = true;
            }
            if (!isWhiteUrl) {
                submitCommonInfoFail("此版本未开放该接口功能，请升级最新的APP哟");
                return;
            }
        }
        Object params;
        try {
            Gson gson = new Gson();
            params = gson.fromJson(data, Object.class);
        } catch (Exception e) {
            submitCommonInfoFail("参数格式错误");
            return;
        }

        MHttpManagerFactory.getMainManager().commonUrl(mContext, url, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(Object result) {
                submitCommonInfoSuccess(result);
            }

            @Override
            public void onError(String message) {
                submitCommonInfoFail(message);
            }
        });

    }


    /**
     * 提交通用数据成功
     *
     * @param data
     */
    public void submitCommonInfoSuccess(Object data) {
        Gson gs = new Gson();
        String dataStr = gs.toJson(data);
        setBaseData("submitCommonInfoSuccess", dataStr);
    }

    /**
     * 提交通用数据失败
     *
     * @param data
     */
    public void submitCommonInfoFail(String data) {
        setBaseData("submitCommonInfoFail", data);
    }
}
