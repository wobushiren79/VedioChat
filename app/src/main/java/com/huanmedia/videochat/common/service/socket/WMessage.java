package com.huanmedia.videochat.common.service.socket;

import java.util.Map;

/**
 * 用于消息发送和接受的数据实体
 * Create by Administrator
 * time: 2017/11/23.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class WMessage {
    private String from;
    private String to;
    private String sid;
    private long time;
    private String type;
    private String stype;
    private Map<String,Object>  body;
    private int flag;
    private String sign;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public Map<String,Object> getBody() {
        return body;
    }

    public void setBody(Map<String,Object>  body) {
        this.body = body;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("WMessage{");
        sb.append("from='").append(from).append('\'');
        sb.append(", to='").append(to).append('\'');
        sb.append(", sid='").append(sid).append('\'');
        sb.append(", time=").append(time);
        sb.append(", type='").append(type).append('\'');
        sb.append(", stype='").append(stype).append('\'');
        sb.append(", body=").append(body);
        sb.append(", flag=").append(flag);
        sb.append(", sign='").append(sign).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
