package com.huanmedia.videochat.mvp.model.video;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoListRequest;

public interface IShortVideoListModel {
    /**
     * 获取短视频列表
     *
     * @param params
     * @param callBack
     */
    void getShortVideoList(Context context, ShortVideoListRequest params, DataCallBack callBack);
}
