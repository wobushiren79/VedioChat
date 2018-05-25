package com.huanmedia.videochat.repository.entity;

public class AppointmentEntity {
    private long apptime;
    private long systime;
    private int uidfrom;
    private int uidto;
    private String unickname;
    private String backmsg;
    private String msg_from_client_id;


    public String getBackmsg() {
        return backmsg;
    }

    public void setBackmsg(String backmsg) {
        this.backmsg = backmsg;
    }

    public long getApptime() {
        return apptime;
    }

    public void setApptime(long apptime) {
        this.apptime = apptime;
    }

    public long getSystime() {
        return systime;
    }

    public void setSystime(long systime) {
        this.systime = systime;
    }

    public int getUidfrom() {
        return uidfrom;
    }

    public void setUidfrom(int uidfrom) {
        this.uidfrom = uidfrom;
    }

    public int getUidto() {
        return uidto;
    }

    public void setUidto(int uidto) {
        this.uidto = uidto;
    }

    public String getUnickname() {
        return unickname;
    }

    public void setUnickname(String unickname) {
        this.unickname = unickname;
    }

    public String getMsg_from_client_id() {
        return msg_from_client_id;
    }

    public void setMsg_from_client_id(String msg_from_client_id) {
        this.msg_from_client_id = msg_from_client_id;
    }
}
