package com.huanmedia.videochat.mvp.entity.results;

import java.io.Serializable;
import java.util.List;

public class SystemTagsResults implements Serializable {
    private List<Tag> goodtags;
    private List<Tag> badtags;
    private String goodtxt;
    private String badtxt;

    public List<Tag> getGoodtags() {
        return goodtags;
    }

    public void setGoodtags(List<Tag> goodtags) {
        this.goodtags = goodtags;
    }

    public List<Tag> getBadtags() {
        return badtags;
    }

    public void setBadtags(List<Tag> badtags) {
        this.badtags = badtags;
    }

    public String getGoodtxt() {
        return goodtxt;
    }

    public void setGoodtxt(String goodtxt) {
        this.goodtxt = goodtxt;
    }

    public String getBadtxt() {
        return badtxt;
    }

    public void setBadtxt(String badtxt) {
        this.badtxt = badtxt;
    }

    public static class Tag implements Serializable{
        private long id;
        private String tag;
        private String type;
        private int  status;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
