package com.huanmedia.videochat.repository.entity;

import java.io.Serializable;

/**
 * Create by Administrator
 * time: 2018/1/5.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class SMsgEntity  implements Serializable {
    private String type;
    private String title;
    private String desc;
    private String createtime;
    private String strtime;
    private String url;
    private int isRed;
    private int id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStrtime() {
        return strtime;
    }

    public void setStrtime(String strtime) {
        this.strtime = strtime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsRed() {
        return isRed;
    }

    public void setIsRed(int isRed) {
        this.isRed = isRed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
