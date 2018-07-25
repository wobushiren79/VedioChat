package com.huanmedia.videochat.mvp.entity.request;

public class ChatReadRequest {
    private String ids;//已读消息的ID，多个用半角逗号分隔 不传 标记所有
    private int msgtype;//消息规定的类型 默认不写为0
    private int fromuid;//消息发送者的UID，默认不写为0

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public int getFromuid() {
        return fromuid;
    }

    public void setFromuid(int fromuid) {
        this.fromuid = fromuid;
    }
}
