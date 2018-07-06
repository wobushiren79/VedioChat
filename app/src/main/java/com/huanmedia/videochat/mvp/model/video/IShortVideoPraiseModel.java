package com.huanmedia.videochat.mvp.model.video;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoPraiseRequest;

public interface IShortVideoPraiseModel {
    /**
     * 短视频点赞
     *
     * @param params
     * @param callBack
     */
    void shortVideoPraise(Context context, ShortVideoPraiseRequest params, DataCallBack callBack);
}
