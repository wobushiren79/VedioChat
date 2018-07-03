package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AdsShufflingRequest;

public interface IGiftListInfoModelImpl {
    /**
     * 获取广告信息
     *
     * @param context
     * @param callBack
     */
    void getGiftList(Context context, DataCallBack callBack);
}
