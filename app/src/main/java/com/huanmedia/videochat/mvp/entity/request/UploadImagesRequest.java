package com.huanmedia.videochat.mvp.entity.request;

public class UploadImagesRequest {
    private int  plevel;//隐私级别 1公开 [默认] 2 付费查看
    private String tag;//标签
    private int vcoin;//查看砖石数

    public int getPlevel() {
        return plevel;
    }

    public void setPlevel(int plevel) {
        this.plevel = plevel;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getVcoin() {
        return vcoin;
    }

    public void setVcoin(int vcoin) {
        this.vcoin = vcoin;
    }
}
