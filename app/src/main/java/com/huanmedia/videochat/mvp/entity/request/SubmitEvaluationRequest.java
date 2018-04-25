package com.huanmedia.videochat.mvp.entity.request;

public class SubmitEvaluationRequest {
    private int uid;
    private String txt;
    private String tagids;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getTagids() {
        return tagids;
    }

    public void setTagids(String tagids) {
        this.tagids = tagids;
    }
}
