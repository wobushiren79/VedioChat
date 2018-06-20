package com.huanmedia.videochat.mvp.entity.results;

public class ShortVideoResults {
    private int id;
    private int urltype;
    private String url;//视频url
    private int account_id;//非0，为红人的UID
    private String tags;//	不空时，为红人的标签字符串，多个用逗号分隔
    private int praise;//点赞总数
    private int ispraise;//用户是否对此视频点赞
    private String describe;//描述
    private String title;//标题
    private String account_face;//用户头像
    private String account_nickname;//用户名字
    private int account_isstarauth;//是否是红人
    private String account_lat;//用户纬度
    private String account_lng;//用户经度
    private String videothumb;//视频缩略图

    public String getVideothumb() {
        return videothumb;
    }

    public void setVideothumb(String videothumb) {
        this.videothumb = videothumb;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccount_face() {
        return account_face;
    }

    public void setAccount_face(String account_face) {
        this.account_face = account_face;
    }

    public String getAccount_nickname() {
        return account_nickname;
    }

    public void setAccount_nickname(String account_nickname) {
        this.account_nickname = account_nickname;
    }

    public int getAccount_isstarauth() {
        return account_isstarauth;
    }

    public void setAccount_isstarauth(int account_isstarauth) {
        this.account_isstarauth = account_isstarauth;
    }

    public String getAccount_lat() {
        return account_lat;
    }

    public void setAccount_lat(String account_lat) {
        this.account_lat = account_lat;
    }

    public String getAccount_lng() {
        return account_lng;
    }

    public void setAccount_lng(String account_lng) {
        this.account_lng = account_lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUrltype() {
        return urltype;
    }

    public void setUrltype(int urltype) {
        this.urltype = urltype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getIspraise() {
        return ispraise;
    }

    public void setIspraise(int ispraise) {
        this.ispraise = ispraise;
    }
}
