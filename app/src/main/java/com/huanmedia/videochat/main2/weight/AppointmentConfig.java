package com.huanmedia.videochat.main2.weight;

import android.os.Parcel;
import android.os.Parcelable;

import com.huanmedia.videochat.repository.entity.VideoChatEntity;

public class AppointmentConfig implements Parcelable {
    private int initiateUserId;//预约人ID
    private int acceptUserID;//被预约人ID
    private int orderId;//订单ID；
    private VideoChatEntity videoChatConfig;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getInitiateUserId() {
        return initiateUserId;
    }

    public void setInitiateUserId(int initiateUserId) {
        this.initiateUserId = initiateUserId;
    }

    public int getAcceptUserID() {
        return acceptUserID;
    }

    public void setAcceptUserID(int acceptUserID) {
        this.acceptUserID = acceptUserID;
    }

    public VideoChatEntity getVideoChatConfig() {
        return videoChatConfig;
    }

    public void setVideoChatConfig(VideoChatEntity mVideoChatConfig) {
        this.videoChatConfig = mVideoChatConfig;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.initiateUserId);
        parcel.writeInt(this.acceptUserID);
        parcel.writeInt(this.orderId);
        parcel.writeParcelable(this.videoChatConfig, flags);
    }

    protected AppointmentConfig() {
    }

    protected AppointmentConfig(Parcel in) {
        initiateUserId = in.readInt();
        acceptUserID = in.readInt();
        orderId = in.readInt();
        videoChatConfig = in.readParcelable(VideoChatEntity.class.getClassLoader());
    }

    public static final Creator<AppointmentConfig> CREATOR = new Creator<AppointmentConfig>() {
        @Override
        public AppointmentConfig createFromParcel(Parcel in) {
            return new AppointmentConfig(in);
        }

        @Override
        public AppointmentConfig[] newArray(int size) {
            return new AppointmentConfig[size];
        }
    };
}
