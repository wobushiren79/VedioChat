package com.huanmedia.videochat.mvp.model.chat;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ChatReadRequest;

public interface IChatReadModel {

    /**
     * 阅读消息
     *
     * @param context
     * @param params
     * @param callBack
     */
    void chatRead(Context context, ChatReadRequest params, DataCallBack callBack);
}
