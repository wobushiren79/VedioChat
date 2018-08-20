package com.huanmedia.videochat.mvp.entity.request;

public class PhotoListRequest {
    private int type;//类型  0全部[默认] 1公开  2隐私
    private Integer status;//类型  1审核通过[默认]   0所有
    private int limit;//显示条数  默认20

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
