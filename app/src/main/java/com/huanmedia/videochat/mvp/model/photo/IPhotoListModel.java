package com.huanmedia.videochat.mvp.model.photo;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.PhotoListRequest;
public interface IPhotoListModel {
    /**
     * 获取短视频列表
     *
     * @param params
     * @param callBack
     */
    void getPhotoList(Context context, PhotoListRequest params, DataCallBack callBack);
}
