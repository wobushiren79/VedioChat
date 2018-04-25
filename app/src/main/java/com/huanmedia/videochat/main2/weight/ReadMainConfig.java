package com.huanmedia.videochat.main2.weight;

import android.os.Parcel;
import android.os.Parcelable;

import com.huanmedia.videochat.repository.entity.VideoChatEntity;

public class ReadMainConfig implements Parcelable {
    private int mRedManId;
    private int mRedMainStartCoin;
    private int mReadMainStartTime;

    private @ConditionEntity.RequestType
    int mRequestType;
    private VideoChatEntity mVideoChatConfig;

    public ReadMainConfig(int redManId, int redMainStartCoin, int readMainStartTime, int requestType) {
        mRedManId = redManId;
        mRedMainStartCoin = redMainStartCoin;
        mReadMainStartTime = readMainStartTime;
        mRequestType = requestType;
    }

    public ReadMainConfig(int redManId, int redMainStartCoin, int readMainStartTime, int requestType, VideoChatEntity videoChatConfig) {
        mRedManId = redManId;
        mRedMainStartCoin = redMainStartCoin;
        mReadMainStartTime = readMainStartTime;
        mRequestType = requestType;
        mVideoChatConfig = videoChatConfig;
    }

    public @ConditionEntity.RequestType
    int getRequestType() {
        return mRequestType;
    }

    public void setRequestType(@ConditionEntity.RequestType int requestType) {
        mRequestType = requestType;
    }

    public int getRedMainStartCoin() {
        return mRedMainStartCoin;
    }

    public void setRedMainStartCoin(int redMainStartCoin) {
        mRedMainStartCoin = redMainStartCoin;
    }

    public int getReadMainStartTime() {
        return mReadMainStartTime;
    }

    public void setReadMainStartTime(int readMainStartTime) {
        mReadMainStartTime = readMainStartTime;
    }

    public int getRedManId() {
        return mRedManId;
    }

    public void setRedManId(int redManId) {
        mRedManId = redManId;
    }

    public ReadMainConfig() {
    }

    public void setVideoChatConfig(VideoChatEntity videoChatConfig) {
        mVideoChatConfig = videoChatConfig;
    }

    public VideoChatEntity getVideoChatConfig() {
        return mVideoChatConfig;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRedManId);
        dest.writeInt(this.mRedMainStartCoin);
        dest.writeInt(this.mReadMainStartTime);
        dest.writeInt(this.mRequestType);
        dest.writeParcelable(this.mVideoChatConfig, flags);
    }

    protected ReadMainConfig(Parcel in) {
        this.mRedManId = in.readInt();
        this.mRedMainStartCoin = in.readInt();
        this.mReadMainStartTime = in.readInt();
        this.mRequestType = in.readInt();
        this.mVideoChatConfig = in.readParcelable(VideoChatEntity.class.getClassLoader());
    }

    public static final Creator<ReadMainConfig> CREATOR = new Creator<ReadMainConfig>() {
        @Override
        public ReadMainConfig createFromParcel(Parcel source) {
            return new ReadMainConfig(source);
        }

        @Override
        public ReadMainConfig[] newArray(int size) {
            return new ReadMainConfig[size];
        }
    };
}

