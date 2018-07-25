package com.huanmedia.videochat.mvp.model.chat;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ChatListRequest;
import com.huanmedia.videochat.mvp.entity.request.ChatSendRequest;

public interface IChatModel {
    /**
     * 获取联系方式解锁信息
     *
     * @param params
     * @param callBack
     */
    void getChatList(Context context, ChatListRequest params, DataCallBack callBack);

    /**
     * 发送聊天信息
     *
     * @param context
     * @param params
     * @param callBack
     */
    void chatSend(Context context, ChatSendRequest params, DataCallBack callBack);


    /**
     * 预约投诉
     * @param context
     * @param aid
     * @param callBack
     */
    void appointmentComplain(Context context,int aid,DataCallBack callBack);
}
