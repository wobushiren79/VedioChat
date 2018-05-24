package com.huanmedia.videochat.mvp.entity.request;

public class AppointmentSettingRequest {
    private int status;//预约开关 0关闭 1开启
    private int dset;//预约周 1每天  2工作日 3周末
    private String tbegin;//开始时间段：12:30 最后一位须为0,5
    private String tend;// 结束时间：23:55 最后一位须为0,5

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDset() {
        return dset;
    }

    public void setDset(int dset) {
        this.dset = dset;
    }

    public String getTbegin() {
        return tbegin;
    }

    public void setTbegin(String tbegin) {
        this.tbegin = tbegin;
    }

    public String getTend() {
        return tend;
    }

    public void setTend(String tend) {
        this.tend = tend;
    }
}
