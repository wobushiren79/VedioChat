package com.huanmedia.videochat.mvp.entity.results;

import com.huanmedia.videochat.repository.entity.BusinessCardEntity;

import java.util.List;

public class DynamicListResults {

    private BusinessCardEntity.BaseInfo uinfo;
    private List<DynamicItem> itmes;
    private ArtistsGroupShowResults.Base stuinfo;

    public BusinessCardEntity.BaseInfo getUinfo() {
        return uinfo;
    }

    public void setUinfo(BusinessCardEntity.BaseInfo uinfo) {
        this.uinfo = uinfo;
    }

    public List<DynamicItem> getItmes() {
        return itmes;
    }

    public void setItmes(List<DynamicItem> itmes) {
        this.itmes = itmes;
    }

    public ArtistsGroupShowResults.Base getStuinfo() {
        return stuinfo;
    }

    public void setStuinfo(ArtistsGroupShowResults.Base stuinfo) {
        this.stuinfo = stuinfo;
    }

    public List<DynamicItem> getList() {
        return itmes;
    }

    public void setList(List<DynamicItem> list) {
        this.itmes = list;
    }

    public static class DynamicItem {
        private int id;
        private int account_id;
        private long ctime;
        private int status;
        private String title;
        private String content;
        private String img;
        private String jmpurl;
        private int praise;
        private int commcount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getJmpurl() {
            return jmpurl;
        }

        public void setJmpurl(String jmpurl) {
            this.jmpurl = jmpurl;
        }

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public int getCommcount() {
            return commcount;
        }

        public void setCommcount(int commcount) {
            this.commcount = commcount;
        }
    }
}
