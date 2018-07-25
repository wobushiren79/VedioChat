package com.huanmedia.videochat.mvp.presenter.chat;

import java.util.List;

public interface IChatReadPresenter {
    /**
     * 阅读所有信息
     */
    void readAllMsg(int chatUserId);

    /**
     * 阅读消息
     * @param msgIds
     */
    void readMsg(int chatUserId,List<Integer> msgIds);
}
