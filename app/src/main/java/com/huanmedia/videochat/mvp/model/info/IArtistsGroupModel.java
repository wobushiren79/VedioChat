package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ArtistsGroupShowRequest;

public interface IArtistsGroupModel {
    /**
     * 获取艺人列表
     *
     * @param context
     * @param callBack
     */
    void getArtistsGroupList(Context context, DataCallBack callBack);


    /**
     * 获取艺人展示信息
     * @param context
     * @param params
     * @param callBack
     */
    void getArtistsGroupShow(Context context, ArtistsGroupShowRequest params,DataCallBack callBack);
}
