package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AdsShufflingRequest;

public interface IFileHotTagModel {
    /**
     * 获取文件标签列表
     *
     * @param context
     * @param callBack
     */
    void getHotTagList(Context context, DataCallBack callBack);

}
