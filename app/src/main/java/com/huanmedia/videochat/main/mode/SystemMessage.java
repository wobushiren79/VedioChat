package com.huanmedia.videochat.main.mode;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Create by Administrator
 * time: 2018/1/3.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class SystemMessage extends DataSupport implements Serializable{

    /**
     * type : NOTICE
     * title : 红人认证成功
     * desc : 您的红人认证审批已通过
     * inttime : 1514971923
     * createtime : 2018-01-03 17:32:03
     * msg_from_client_id : 7f00000113ed00000013
     */
    private String type;
    private String title;
    private String desc;
    private String createtime;
    private String strtime;
    private String url;
    private int isRed;
    private int msgId ;
    public int getMsgId() {
        return msgId;
    }
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrtime() {
        return strtime;
    }

    public void setStrtime(String strtime) {
        this.strtime = strtime;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }


    public String getUrl() {
        return url;
    }

    public int getIsRed() {
        return isRed;
    }

    public void setIsRed(int isRed) {
        this.isRed = isRed;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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
}
