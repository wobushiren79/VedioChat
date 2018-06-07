package com.huanmedia.videochat.repository.entity;

public class CoinEntity {
    private int coin;
    private String txt;
    private String title;
    private String imgurl;
    private String jmpurl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getJmpurl() {
        return jmpurl;
    }

    public void setJmpurl(String jmpurl) {
        this.jmpurl = jmpurl;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}
