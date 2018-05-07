package com.huanmedia.videochat.mvp.entity.request;

public class TalkRoomListRequest {
    private int page;
    private int pagesize;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
}
