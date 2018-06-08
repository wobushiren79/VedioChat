package com.huanmedia.videochat.mvp.entity.results;

public class AppointmentDataResults {
    private String nickname;
    private int uid;
    private String userphoto_thumb;
    private String date;
    private String time;

    private int status;
    private String datetime;
    private int aid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserphoto_thumb() {
        return userphoto_thumb;
    }

    public void setUserphoto_thumb(String userphoto_thumb) {
        this.userphoto_thumb = userphoto_thumb;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }
}
