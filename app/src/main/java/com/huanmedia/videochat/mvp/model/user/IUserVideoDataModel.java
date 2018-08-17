package com.huanmedia.videochat.mvp.model.user;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.UserVideoDataRequest;

import java.util.List;

public interface IUserVideoDataModel {
    /**
     * 上传用户视频数据
     *
     * @param context
     * @param params
     * @param callBack
     */
    void uploadUserVideo(Context context, UserVideoDataRequest params, List<String> videoImages, DataCallBack callBack);


    /**
     * 删除用户视频数据
     *
     * @param context
     * @param params
     * @param callBack
     */
    void deleteUserVideo(Context context, UserVideoDataRequest params, DataCallBack callBack);
}
