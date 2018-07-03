package com.huanmedia.videochat.mvp.entity.request;

public class RewardRequest {
    private int uid;//被打赏人UID
    private int type;//用户消费类型：1 视频打赏
    private int coin;//打赏的金币数

    private int videoid;//type==1时 必填视频ID
    private int giftid;//type==1时 必填礼物ID
    private int ggiftcount;//type==1时 必填礼物数量

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getVideoid() {
        return videoid;
    }

    public void setVideoid(int videoid) {
        this.videoid = videoid;
    }

    public int getGiftid() {
        return giftid;
    }

    public void setGiftid(int giftid) {
        this.giftid = giftid;
    }

    public int getGgiftcount() {
        return ggiftcount;
    }

    public void setGgiftcount(int ggiftcount) {
        this.ggiftcount = ggiftcount;
    }
}
