package com.huanmedia.videochat.mvp.entity.request;

public class ReportRequest {
    private long ouid;
    private int type;

    public long getOuid() {
        return ouid;
    }

    public void setOuid(long ouid) {
        this.ouid = ouid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
