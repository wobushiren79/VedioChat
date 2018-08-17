package com.huanmedia.videochat.mvp.model.video;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.UserVideoListRequest;

public interface IUserVideoListModel {
    /**
     * 获取用户短视频
     *
     * @param params
     * @param callBack
     */
    void getUserVideoList(Context context, UserVideoListRequest params, DataCallBack callBack);
}
