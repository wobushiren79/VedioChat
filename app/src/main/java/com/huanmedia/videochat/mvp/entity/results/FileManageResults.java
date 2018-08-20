package com.huanmedia.videochat.mvp.entity.results;

public class FileManageResults {
    private int hasread;//1已付费读过 0没有付费读
    private int vcoin;//支付金额
    private int coin;


    public int getVcoin() {
        return vcoin;
    }

    public void setVcoin(int vcoin) {
        this.vcoin = vcoin;
    }

    public int getHasread() {
        return hasread;
    }

    public void setHasread(int hasread) {
        this.hasread = hasread;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}
