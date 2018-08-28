package com.huanmedia.videochat.mvp.entity.request;

public class AudioFileRequest {
    private String url;
    private int audiotimes;

    public int getAudiotimes() {
        return audiotimes;
    }

    public void setAudiotimes(int audiotimes) {
        this.audiotimes = audiotimes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
