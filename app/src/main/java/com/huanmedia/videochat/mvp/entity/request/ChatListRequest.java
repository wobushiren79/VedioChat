package com.huanmedia.videochat.mvp.entity.request;

public class ChatListRequest {
    private int msgtype;//消息类型  1 消息反馈 2 预约留言
    private int ouid;// 对方的UID
    private int page;//分类 默认1
    private int newid;//查看比此记录新的记录;
    private int oldid;//查看比此记录旧的记录;

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public int getOuid() {
        return ouid;
    }

    public void setOuid(int ouid) {
        this.ouid = ouid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNewid() {
        return newid;
    }

    public void setNewid(int newid) {
        this.newid = newid;
    }

    public int getOldid() {
        return oldid;
    }

    public void setOldid(int oldid) {
        this.oldid = oldid;
    }
}
