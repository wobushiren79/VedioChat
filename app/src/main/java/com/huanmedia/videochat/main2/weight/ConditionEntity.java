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
    private ReadMainConfig mReadMainConfig=new ReadMainConfig();
    private MatchConfig mMatchConfig=new MatchConfig();

    @IntDef({ConditionTtype.FILTER,ConditionTtype.ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ConditionTtype {
        int FILTER = 2, ALL = 1;
    }

    @IntDef({RequestType.MATCH,RequestType.SELF,RequestType.PERSON})
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
    @IntDef({VideoType.MATCH,VideoType.REDMAN,VideoType.NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VideoType {
        int MATCH = 1, REDMAN = 2, NONE = 3;
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

    public ConditionEntity() {
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.videoType);
        dest.writeParcelable(this.mReadMainConfig, flags);
        dest.writeParcelable(this.mMatchConfig, flags);
    }

    protected ConditionEntity(Parcel in) {
        this.videoType = in.readInt();
        this.mReadMainConfig = in.readParcelable(ReadMainConfig.class.getClassLoader());
        this.mMatchConfig = in.readParcelable(MatchConfig.class.getClassLoader());
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
