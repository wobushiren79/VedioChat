package com.huanmedia.videochat.mvp.entity.request;

public class DynamicListRequset {
    private int uid;
    private int page;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
