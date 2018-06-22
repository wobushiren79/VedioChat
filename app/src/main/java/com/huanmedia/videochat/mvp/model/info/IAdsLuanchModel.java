package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AdsLuanchRequest;

public interface IAdsLuanchModel {
    /**
     * 获取首页广告信息
     *
     * @param context
     * @param params
     * @param callBack
     */
    void getAdsInfo(Context context, AdsLuanchRequest params, DataCallBack callBack);
}
