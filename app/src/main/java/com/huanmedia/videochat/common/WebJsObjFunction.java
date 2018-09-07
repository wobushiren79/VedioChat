package com.huanmedia.videochat.common;

import android.app.Activity;
import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.utils.UMengUtils;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.main2.MainActivity;
import com.huanmedia.videochat.media.MediaUpLoadActivity;
import com.huanmedia.videochat.my.PhotosActivity;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.litepal.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

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
     * 跳转微信APP
     */
    @JavascriptInterface
    public void jumpWX(String appid) {
       IWXAPI wxApi= WXAPIFactory.createWXAPI(mContext,null);
       wxApi.registerApp(appid);
       wxApi.openWXApp();
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
        jumpActivity(activityName, null);
    }

    /**
     * 跳转相应界面
     */
    @JavascriptInterface
    public void jumpActivity(String activityName, String value) {
        try {
            Navigator navigator = new Navigator();
            switch (activityName) {
                case "RechargePage":
                    //充值页
                    UMengUtils.JumpActivity(mContext, 0, 0);
                    navigator.navtoCoinPay((Activity) mContext, null);
                    break;
                case "FeedBackPage":
                    //意见反馈
                    UMengUtils.JumpActivity(mContext, 1, 0);
                    navigator.navtoFeedBack((Activity) mContext);
                    break;
                case "ReadManCertificate":
                    //红人认证
                    UMengUtils.JumpActivity(mContext, 2, 0);
                    navigator.navtoReadMainCertificate((Activity) mContext);
                    break;
                case "VideoUpdate":
                    //视频上传
                    UMengUtils.JumpActivity(mContext, 3, 0);
                    navigator.navtoMediaUpLoad((Activity) mContext, MediaUpLoadActivity.UpLoadType.NORMAL, null, false);
                    break;
                case "PhotoUpdate":
                    //照片上传
                    UMengUtils.JumpActivity(mContext, 4, 0);
                    navigator.navtoPhotos((Activity) mContext, PhotosActivity.UpLoadType.NORMAL, new ArrayList<>());
                    break;
                case "AppointmentList":
                    //预约列表
                    UMengUtils.JumpActivity(mContext, 5, 0);
                    navigator.navtoAppointmentHistoryList((Activity) mContext);
                    break;
                case "MyWallet":
                    //我的钱包
                    UMengUtils.JumpActivity(mContext, 6, 0);
                    navigator.navtoMyWallet((Activity) mContext);
                    break;
                case "Appointment":
                    //预约
                    UMengUtils.JumpActivity(mContext, 7, 0);
                    int appointmentUid = Integer.valueOf(value);
                    navigator.navtoAppointment((Activity) mContext, appointmentUid);
                    break;
                case "ReadManInfo":
                    //红人介绍界面
                    UMengUtils.JumpActivity(mContext, 8, 0);
                    int readmanUid = Integer.valueOf(value);
                    navigator.navDiscoverInfo((Activity) mContext, readmanUid, null, BusinessCardFragment.ShowType.ReadMan);
                    break;

                case "VideoPlay":
                    String[] videoUrl = value.split(",");
                    ArrayList<VideoEntity> listVide = new ArrayList<>();
                    int position = 0;
                    for (int i = 0; i < videoUrl.length; i++) {
                        if (i > 0) {
                            VideoEntity videoEntity = new VideoEntity();
                            videoEntity.setVoideurl(videoUrl[i]);
                            listVide.add(videoEntity);
                        } else {
                            position = Integer.valueOf(videoUrl[i]);
                        }
                    }
                    navigator.navtoMediaPlay((Activity) mContext, listVide, position);
                    break;
            }
        } catch (Exception e) {
            ToastUtils.showToastShortInCenter(mContext,e.getMessage());
        }

    }

    @JavascriptInterface
    public void exitWebActivity(String jumpType) {
        if (jumpType == null) {

        } else if (jumpType.equals("main_video")) {
            //跳转主页视频
            MainActivity.jumpFragmentPosition = 1;
        } else if (jumpType.equals("match")) {
            //跳转匹配
            MainActivity.jumpFragmentPosition = 0;
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
