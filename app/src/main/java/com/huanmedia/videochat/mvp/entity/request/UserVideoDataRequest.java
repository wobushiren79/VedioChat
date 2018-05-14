package com.huanmedia.videochat.mvp.entity.request;

import java.util.List;

public class UserVideoDataRequest {
    private String bindfilename;//从接口得到的文件名
    private String fullname;//名含扩展名的文件名
    private List<String> img;//图片

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

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }
}
