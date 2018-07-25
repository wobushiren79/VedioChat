package com.huanmedia.videochat.mvp.presenter.chat;

public interface IChatSendPresenter {

    /**
     * 发送聊天信息
     *
     * @param chatUserId
     */
    void chatSend(int chatUserId);

    /**
     * 投诉意见发送
     */
    void feedBackSend();

    /**
     * 预约投诉
     */
    void appointmentComplain(int orderId,int toUid);
}
