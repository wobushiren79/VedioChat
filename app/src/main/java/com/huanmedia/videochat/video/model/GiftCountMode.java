package com.huanmedia.videochat.video.model;

/**
 * Create by Administrator
 * time: 2018/1/22.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class GiftCountMode {

    private String name;
    private int count;

    public GiftCountMode(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
