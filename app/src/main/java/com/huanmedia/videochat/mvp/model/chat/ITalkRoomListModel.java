package com.huanmedia.videochat.mvp.model.chat;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.TalkRoomListRequest;

public interface ITalkRoomListModel {
    /**
     * 获取联系方式解锁信息
     *
     * @param params
     * @param callBack
     */
    void getTalkRoomList(Context context, TalkRoomListRequest params, DataCallBack callBack);
}
