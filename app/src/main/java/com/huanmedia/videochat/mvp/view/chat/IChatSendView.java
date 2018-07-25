package com.huanmedia.videochat.mvp.view.chat;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.ChatSendResults;

import java.util.List;

public interface IChatSendView extends BaseMVPView {
    /**
     * 发送聊天成功
     */
    void chatSendSuccess(ChatSendResults results);

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

    /**
     * 获取发送聊天图片
     * @return
     */
    List<String> getChatSengImages();
}
