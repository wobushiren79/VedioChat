package com.huanmedia.videochat.mvp.entity.request;

public class FileUpLoadRequest {
    private int type;//1视频，2音频

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
