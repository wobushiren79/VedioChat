package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ShufflingAdsRequest;

public interface IShufflingAdsModel {
    /**
     * 获取广告信息
     *
     * @param context
     * @param params
     * @param callBack
     */
    void getAdsInfo(Context context, ShufflingAdsRequest params, DataCallBack callBack);

}
