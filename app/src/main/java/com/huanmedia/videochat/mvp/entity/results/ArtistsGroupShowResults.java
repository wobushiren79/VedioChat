package com.huanmedia.videochat.mvp.entity.results;

import java.util.List;

public class ArtistsGroupShowResults {
    private Base base;
    private List<Items> items;

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public static class Base {
        private int id;
        private String name;
        private long ctime;
        private String imgurl;
        private int imgwidth;
        private int imgheight;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getImgwidth() {
            return imgwidth;
        }

        public void setImgwidth(int imgwidth) {
            this.imgwidth = imgwidth;
        }

        public int getImgheight() {
            return imgheight;
        }

        public void setImgheight(int imgheight) {
            this.imgheight = imgheight;
        }
    }

    public static class Items {

        private int id;
        private int studid;
        private int binduid;
        private String imgurl;
        private int imgwidth;
        private int imgheight;
        private int imgx;
        private int imgy;
        private int imgz;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStudid() {
            return studid;
        }

        public void setStudid(int studid) {
            this.studid = studid;
        }

        public int getBinduid() {
            return binduid;
        }

        public void setBinduid(int binduid) {
            this.binduid = binduid;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getImgwidth() {
            return imgwidth;
        }

        public void setImgwidth(int imgwidth) {
            this.imgwidth = imgwidth;
        }

        public int getImgheight() {
            return imgheight;
        }

        public void setImgheight(int imgheight) {
            this.imgheight = imgheight;
        }

        public int getImgx() {
            return imgx;
        }

        public void setImgx(int imgx) {
            this.imgx = imgx;
        }

        public int getImgy() {
            return imgy;
        }

        public void setImgy(int imgy) {
            this.imgy = imgy;
        }

        public int getImgz() {
            return imgz;
        }

        public void setImgz(int imgz) {
            this.imgz = imgz;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

}
