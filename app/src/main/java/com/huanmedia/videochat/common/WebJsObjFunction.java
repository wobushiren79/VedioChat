package com.huanmedia.videochat.common;

import android.app.Activity;
import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.utils.UMengUtils;
import com.huanmedia.videochat.main2.MainActivity;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.ArrayList;

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
        exitWebActivity(null);
    }


    /**
     * 跳转相应界面
     */
    @JavascriptInterface
    public void jumpActivity(String activityName) {
        Navigator navigator = new Navigator();
        switch (activityName) {
            case "RechargePage":
                //充值页
                UMengUtils.JumpActivity(mContext,0,0);
                navigator.navtoCoinPay((Activity)mContext, null);
                break;
            case "FeedBackPage":
                //意见反馈
                UMengUtils.JumpActivity(mContext,1,0);
                navigator.navtoFeedBack((Activity)mContext);
                break;
            case "ReadManCertificate":
                //红人认证
                UMengUtils.JumpActivity(mContext,2,0);
                navigator.navtoReadMainCertificate((Activity) mContext);
                break;
            case "VideoUpdate":
                //视频上传
                navigator.navtoMediaUpLoad((Activity) mContext, null, false);
                break;
            case "PhotoUpdate":
                //照片上传
                navigator.navtoPhotos((Activity) mContext,new ArrayList<>());
                break;
        }
    }

    @JavascriptInterface
    public void exitWebActivity(String jumpType) {
        if (jumpType == null) {

        } else if (jumpType.equals("main_video")) {
            //跳转主页视频
            MainActivity.jumpFragmentPosition = 1;
        }
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
        submitCommonInfo(url, data, "submitCommonInfoSuccess", "submitCommonInfoFail");
    }

    /**
     * 获取通用数据
     *
     * @param url
     * @param data
     * @param callBackSuccessName
     * @param callBackFailName
     */
    @JavascriptInterface
    public void submitCommonInfo(String url, String data, String callBackSuccessName, String callBackFailName) {
        if (url == null || url.length() == 0) {
            submitCommonInfoFail("没有请求地址", callBackFailName);
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
                submitCommonInfoFail("此版本未开放该接口功能，请升级最新的APP哟", callBackFailName);
                return;
            }
        }
        Object params;
        try {
            Gson gson = new Gson();
            params = gson.fromJson(data, Object.class);
        } catch (Exception e) {
            submitCommonInfoFail("参数格式错误", callBackFailName);
            return;
        }

        MHttpManagerFactory.getMainManager().commonUrl(mContext, url, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(Object result) {
                submitCommonInfoSuccess(result, callBackSuccessName);
            }

            @Override
            public void onError(String message) {
                submitCommonInfoFail(message, callBackFailName);
            }
        });


    }

    /**
     * 提交通用数据成功
     *
     * @param data
     */
    public void submitCommonInfoSuccess(Object data, String name) {
        Gson gs = new Gson();
        String dataStr = gs.toJson(data);
        setBaseData(name, dataStr);
    }

    /**
     * 提交通用数据失败
     *
     * @param data
     */
    public void submitCommonInfoFail(String data, String name) {
        setBaseData(name, data);
    }
}
