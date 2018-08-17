package com.huanmedia.videochat.mvp.entity.request;

import java.util.List;

public class UserVideoDataRequest {
    private String bindfilename;//从接口得到的文件名
    private String fullname;//名含扩展名的文件名
    private int plevel;//隐私级别 1公开 [默认]2 付费查看
    private String tag;//默认为空
    private int vcoin;//隐私图片查看所需金币数量默认为0

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

    private String ids;//需要删除的视频ID

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getBindfilename() {
        return bindfilename;
    }

    public void setBindfilename(String bindfilename) {
        this.bindfilename = bindfilename;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

}
