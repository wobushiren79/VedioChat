package com.huanmedia.videochat.mvp.entity.request;

public class FileManageRequest {
    private int typeid;//隐私图片[视频]ID
    private int type;//1隐私图片 2隐私视频

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
