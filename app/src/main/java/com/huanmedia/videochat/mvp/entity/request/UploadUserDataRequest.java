package com.huanmedia.videochat.mvp.entity.request;

public class UploadUserDataRequest {
    private String lat;//纬度
    private String lng;//经度
    private String address;//地址

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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
