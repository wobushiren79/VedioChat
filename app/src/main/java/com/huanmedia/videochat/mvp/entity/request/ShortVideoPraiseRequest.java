package com.huanmedia.videochat.mvp.entity.request;

public class ShortVideoPraiseRequest {
    private int id;//视频ID
    private int flag;//点赞状态

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
