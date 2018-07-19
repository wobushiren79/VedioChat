package com.huanmedia.videochat.mvp.entity.request;

public class AppointmentRequest {
    private int uid;//红人UID
    private String date;//预约日期 格式如： 2018-05-06
    private String time;//预约时间格式如： 18:15 最后分钟数只能以0,5结束

    private int aid;//要更新的预约的ID
    private int state;//1确信  -1取消

    private int coins;//预约费用
    private String msg;//预约留言
    private int times;//预约时间 分钟数，最小10

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
