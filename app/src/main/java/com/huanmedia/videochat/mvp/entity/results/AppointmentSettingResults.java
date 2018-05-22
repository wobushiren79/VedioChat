package com.huanmedia.videochat.mvp.entity.results;

public class AppointmentSettingResults {
    private int id;
    private int account_id;
    private int status;//开启状态  0关闭 1开启
    private String btime;//开始时间
    private String etime;//结束时间
    private long ctime;
    private int dset;//1每天  2工作日 3周末

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public int getDset() {
        return dset;
    }

    public void setDset(int dset) {
        this.dset = dset;
    }
}
