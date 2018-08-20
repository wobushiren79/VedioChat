package com.huanmedia.videochat.mvp.entity.request;

public class UserVideoListRequest {
    private int type;
    private Integer status;
    private int limit;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

