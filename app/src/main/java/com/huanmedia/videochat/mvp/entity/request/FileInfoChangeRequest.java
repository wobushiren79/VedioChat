package com.huanmedia.videochat.mvp.entity.request;

public class FileInfoChangeRequest {
    private Integer id;
    private Integer plevel;//隐私级别1公开 2付费查看
    private Integer type;//类型 1图片 2视频
    private String tag;//默认为空
    private Integer vcoin;//隐私图片查看所需金币数量 默认为0

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlevel() {
        return plevel;
    }

    public void setPlevel(Integer plevel) {
        this.plevel = plevel;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getVcoin() {
        return vcoin;
    }

    public void setVcoin(Integer vcoin) {
        this.vcoin = vcoin;
    }
}
