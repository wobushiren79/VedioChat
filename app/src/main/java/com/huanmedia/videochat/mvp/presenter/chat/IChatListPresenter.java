package com.huanmedia.videochat.mvp.presenter.chat;

public interface IChatListPresenter {

    /**
     * 获取历史记录聊天信息
     */
    void getHistoryChatList(int msgId);

    /**
     * 获取最新的聊天记录
     *
     * @param msgId
     */
    void getNewChatList(int msgId);


    /**
     * 获取出事聊天记录
     */
    void getDefChatList();

}
