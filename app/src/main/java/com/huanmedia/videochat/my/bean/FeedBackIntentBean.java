package com.huanmedia.videochat.my.bean;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FeedBackIntentBean implements Serializable {
    @Retention(RetentionPolicy.SOURCE)
    public @interface FeedBackType {
        int FeedBack = 1;//意见反馈
        int AppointmentComplain = 2;//预约投诉
    }

    private int feedBackType;
    private int orderId;
    private int toUid;

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFeedBackType() {
        return feedBackType;
    }

    public void setFeedBackType(int feedBackType) {
        this.feedBackType = feedBackType;
    }
}
