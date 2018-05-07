package com.huanmedia.videochat.mvp.entity.request;

public class UploadUserDataRequest {
    private String lat;//纬度
    private String lng;//经度

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
