package com.huanmedia.videochat.mvp.entity.request;

import java.util.List;

public class ChatSendRequest {
    private List<String> img;//图片
    private String message;
    private int msgtype;
    private int touid;//给谁留言 [默认0]
    private int virid;//关健字段值 如订单ID 默认为0

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMstype() {
        return msgtype;
    }

    public void setMstype(int mstype) {
        this.msgtype = mstype;
    }

    public int getTouid() {
        return touid;
    }

    public void setTouid(int touid) {
        this.touid = touid;
    }

    public int getVirid() {
        return virid;
    }

    public void setVirid(int virid) {
        this.virid = virid;
    }
}
