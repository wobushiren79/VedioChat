package com.huanmedia.videochat.mvp.view.chat;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IChatSendView extends BaseMVPView {
    /**
     * 发送聊天成功
     */
    void chatSendSuccess();

    /**
     * 发送聊天失败
     * @param msg
     */
    void chatSendFail(String msg);

    /**
     * 获取发送聊天数据
     * @return
     */
    String getChatSendMsg();
}
