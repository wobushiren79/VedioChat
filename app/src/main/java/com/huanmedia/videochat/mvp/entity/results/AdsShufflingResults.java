package com.huanmedia.videochat.mvp.entity.results;

public class AdsShufflingResults {
    private int id;
    private String title;
    private String ctime;
    private String bimg;
    private int linktype;
    private String linkurl;
    private int status;
    private int location;
    private int bsort;
    private String bparas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getBimg() {
        return bimg;
    }

    public void setBimg(String bimg) {
        this.bimg = bimg;
    }

    public int getLinktype() {
        return linktype;
    }

    public void setLinktype(int linktype) {
        this.linktype = linktype;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getBsort() {
        return bsort;
    }

    public void setBsort(int bsort) {
        this.bsort = bsort;
    }

    public String getBparas() {
        return bparas;
    }

    public void setBparas(String bparas) {
        this.bparas = bparas;
    }
}
