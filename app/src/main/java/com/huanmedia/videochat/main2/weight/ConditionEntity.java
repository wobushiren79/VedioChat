package com.huanmedia.videochat.main2.weight;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 匹配条件
 * time: 2017/12/2.
 * Email:eric.yang@huanmedia.com
 * version: 1.0
 */

public class ConditionEntity implements Parcelable {
    @VideoType
    private int videoType;// 1 匹配 2 红人
    @RequestType
    private int requestType;

    private int needCloseFU = 1;//0不需要关闭，1需要关闭
    private ReadMainConfig mReadMainConfig = new ReadMainConfig();
    private MatchConfig mMatchConfig = new MatchConfig();
    private AppointmentConfig mAppointmentConfig = new AppointmentConfig();


    @Retention(RetentionPolicy.SOURCE)
    public @interface ConditionTtype {
        int FILTER = 2, ALL = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RequestType {
        /**
         * 匹配
         */
        int MATCH = 3;
        /**
         * 自己拨打
         */
        int SELF = 2;
        /**
         * 对方拨打唤醒
         */
        int PERSON = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VideoType {
        int MATCH = 1, REDMAN = 2, NONE = 0, APPOINTMENT = 3;
    }

    @RequestType
    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(@RequestType int requestType) {
        this.requestType = requestType;
    }

    public @VideoType
    int getVideoType() {
        return videoType;
    }

    public MatchConfig getMatchConfig() {
        return mMatchConfig;
    }

    public void setMatchConfig(MatchConfig matchConfig) {
        mMatchConfig = matchConfig;
    }

    public void setVideoType(@VideoType int videoType) {
        this.videoType = videoType;
    }

    public ReadMainConfig getReadMainConfig() {
        return mReadMainConfig;
    }


    public void setReadMainConfig(ReadMainConfig readMainConfig) {
        mReadMainConfig = readMainConfig;
    }

    public AppointmentConfig getAppointmentConfig() {
        return mAppointmentConfig;
    }

    public void setAppointmentConfig(AppointmentConfig mAppointmentConfig) {
        this.mAppointmentConfig = mAppointmentConfig;
    }

    public int getNeedCloseFU() {
        return needCloseFU;
    }

    public void setNeedCloseFU(int needCloseFU) {
        this.needCloseFU = needCloseFU;
    }

    public ConditionEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.videoType);
        dest.writeInt(this.requestType);
        dest.writeInt(this.needCloseFU);
        dest.writeParcelable(this.mReadMainConfig, flags);
        dest.writeParcelable(this.mMatchConfig, flags);
        dest.writeParcelable(this.mAppointmentConfig, flags);
    }

    protected ConditionEntity(Parcel in) {
        this.videoType = in.readInt();
        this.requestType = in.readInt();
        this.needCloseFU = in.readInt();
        this.mReadMainConfig = in.readParcelable(ReadMainConfig.class.getClassLoader());
        this.mMatchConfig = in.readParcelable(MatchConfig.class.getClassLoader());
        this.mAppointmentConfig = in.readParcelable(AppointmentConfig.class.getClassLoader());
    }

    public static final Creator<ConditionEntity> CREATOR = new Creator<ConditionEntity>() {
        @Override
        public ConditionEntity createFromParcel(Parcel source) {
            return new ConditionEntity(source);
        }

        @Override
        public ConditionEntity[] newArray(int size) {
            return new ConditionEntity[size];
        }
    };
}
