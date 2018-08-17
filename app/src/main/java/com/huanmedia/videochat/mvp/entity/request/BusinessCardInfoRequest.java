package com.huanmedia.videochat.mvp.entity.request;

public class BusinessCardInfoRequest {
    private int uid;
    private int plevel;//查看隐私 1 只看公开[默认] 2只看隐私 0公开和隐私

    public int getPlevel() {
        return plevel;
    }

    public void setPlevel(int plevel) {
        this.plevel = plevel;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
