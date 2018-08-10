package com.huanmedia.videochat.mvp.entity.request;

public class UploadUserDataRequest {
    private String lat;//纬度
    private String lng;//经度
    private String addressinfo;//地址

    public String getAddressinfo() {
        return addressinfo;
    }

    public void setAddressinfo(String addressinfo) {
        this.addressinfo = addressinfo;
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
