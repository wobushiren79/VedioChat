package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.huanmedia.videochat.video.model.GiftLocalMode;

/**
 * Create by Administrator
 * time: 2017/12/14.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class GiftEntity implements Parcelable {
    /**
     * id : 1
     * name : 玫瑰花
     * desc : 50钻石 玫瑰花
     * type : 50
     * img_pack : http://mmimg.kdlz.com
     * img_thumb : http://mmimg.kdlz.com
     * payCount : 礼物数量
     */
    private GiftLocalMode _localMode;
    private int id;
    private String name;
    private String desc;
    private int coin;
    private String img_pack;
    private String img_thumb;
    private int addtime;
    private int defcount;
    private int payCount;
    private String mSendUserId;
    private String mSendUserName;
    private String mReceiveUserId;
    private String mReceiveUserName;

    public String getReceiveUserName() {
        return mReceiveUserName;
    }

    public String getReceiveUserId() {
        return mReceiveUserId;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public void setReceiveUserId(String receiveUserId) {
        mReceiveUserId = receiveUserId;
    }

    public void setReceiveUserName(String receiveUserName) {
        mReceiveUserName = receiveUserName;
    }

    public void setSendUserId(String sendUserId) {
        mSendUserId = sendUserId;
    }

    public void setSendUserName(String sendUserName) {
        mSendUserName = sendUserName;
    }

    public GiftLocalMode get_localMode() {
        return _localMode;
    }

    public void set_localMode(GiftLocalMode _localMode) {
        this._localMode = _localMode;
    }

    public int getDefcount() {
        return defcount;
    }

    public void setDefcount(int defcount) {
        this.defcount = defcount;
    }

    public int getPayCount() {
        return payCount;
    }

    public void setPayCount(int payCount) {
        this.payCount = payCount;
    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getImg_pack() {
        return img_pack;
    }

    public void setImg_pack(String img_pack) {
        this.img_pack = img_pack;
    }

    public String getImg_thumb() {
        return img_thumb;
    }

    public void setImg_thumb(String img_thumb) {
        this.img_thumb = img_thumb;
    }

    public GiftEntity() {
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GiftEntity{");
        sb.append("_localMode=").append(_localMode);
        sb.append(", id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append(", coin=").append(coin);
        sb.append(", img_pack='").append(img_pack).append('\'');
        sb.append(", img_thumb='").append(img_thumb).append('\'');
        sb.append(", addtime=").append(addtime);
        sb.append(", defcount=").append(defcount);
        sb.append(", payCount=").append(payCount);
        sb.append(", mSendUserId='").append(mSendUserId).append('\'');
        sb.append(", mSendUserName='").append(mSendUserName).append('\'');
        sb.append(", mReceiveUserId='").append(mReceiveUserId).append('\'');
        sb.append(", mReceiveUserName='").append(mReceiveUserName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getSendUserId() {
        return mSendUserId;
    }

    public String getSendUserName() {
        return mSendUserName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this._localMode, flags);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeInt(this.coin);
        dest.writeString(this.img_pack);
        dest.writeString(this.img_thumb);
        dest.writeInt(this.addtime);
        dest.writeInt(this.defcount);
        dest.writeInt(this.payCount);
        dest.writeString(this.mSendUserId);
        dest.writeString(this.mSendUserName);
        dest.writeString(this.mReceiveUserId);
        dest.writeString(this.mReceiveUserName);
    }

    protected GiftEntity(Parcel in) {
        this._localMode = in.readParcelable(GiftLocalMode.class.getClassLoader());
        this.id = in.readInt();
        this.name = in.readString();
        this.desc = in.readString();
        this.coin = in.readInt();
        this.img_pack = in.readString();
        this.img_thumb = in.readString();
        this.addtime = in.readInt();
        this.defcount = in.readInt();
        this.payCount = in.readInt();
        this.mSendUserId = in.readString();
        this.mSendUserName = in.readString();
        this.mReceiveUserId = in.readString();
        this.mReceiveUserName = in.readString();
    }

    public static final Creator<GiftEntity> CREATOR = new Creator<GiftEntity>() {
        @Override
        public GiftEntity createFromParcel(Parcel source) {
            return new GiftEntity(source);
        }

        @Override
        public GiftEntity[] newArray(int size) {
            return new GiftEntity[size];
        }
    };
}
