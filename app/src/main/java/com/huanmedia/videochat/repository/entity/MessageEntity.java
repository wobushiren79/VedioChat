package com.huanmedia.videochat.repository.entity;

import java.io.Serializable;

public class MessageEntity implements Serializable {
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
