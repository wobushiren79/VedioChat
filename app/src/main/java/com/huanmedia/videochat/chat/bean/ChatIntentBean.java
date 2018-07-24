package com.huanmedia.videochat.chat.bean;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ChatIntentBean implements Serializable {
    @Retention(RetentionPolicy.SOURCE)
    public @interface ChatType {
        int Appointment = 1;
    }

    private int chatType;//聊天类型
    private int orderId;//订单ID
    private int chatUserId;//聊天对象ID

    public int getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(int chatUserId) {
        this.chatUserId = chatUserId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
}
