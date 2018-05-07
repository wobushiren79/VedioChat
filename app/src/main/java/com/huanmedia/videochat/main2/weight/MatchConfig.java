package com.huanmedia.videochat.main2.weight;

import android.os.Parcel;
import android.os.Parcelable;

import com.huanmedia.videochat.repository.entity.VideoChatEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MatchConfig implements Parcelable {
    @ConditionEntity.ConditionTtype
    private int type = ConditionEntity.ConditionTtype.ALL;//1为所有 2 为条件筛选 默认为所有：1
    private int sex = 2; //1:男 2:女 3:不限 默认为：3
    private int age; //0:不限 16:16-20 20:20-25 25:25-30 30:30-35 35:35-∞
    private int mask;//1蒙面 0不蒙面
    private int matchType;//匹配模式类型
    private @ConditionEntity.RequestType
    int mRequestType = ConditionEntity.RequestType.MATCH;
    private int mUid;
    private VideoChatEntity mVideoChatEntity;


    @Retention(RetentionPolicy.SOURCE)
    public @interface MatchType {
        /**
         * 拨打
         */
        int CALL = 1;

        /**
         * 默认
         */
        int DEF = 0;
    }

    public MatchConfig(int type, int sex, int age, int mask) {
        this.type = type;
        this.sex = sex;
        this.age = age;
//            this.mask = mask;
    }

    public @ConditionEntity.ConditionTtype
    int getType() {
        return type;
    }

    public void setType(@ConditionEntity.ConditionTtype int type) {
        this.type = type;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



    /**
     * v1.0 版本时候使用的
     * 后续更应该不会使用了
     *
     * @return
     */
    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public String getAgeString() {
        String mAgeString;
        switch (age) {
            case 0:
                mAgeString = "不限";
                break;
            case 16:
                mAgeString = "16-20岁";
                break;
            case 20:
                mAgeString = "20-25岁";
                break;
            case 25:
                mAgeString = "25-30岁";
                break;
            case 30:
                mAgeString = "30-35岁";
                break;
            case 35:
                mAgeString = "35岁以上";
                break;
            default:
                mAgeString = "35岁以上";
                break;
        }
        return mAgeString;
    }

    public MatchConfig() {
    }

    public int getUid() {
        return mUid;
    }

    public void setUid(int uid) {
        mUid = uid;
    }

    public int getMatchType() {
        return matchType;
    }

    public void setMatchType(int matchType) {
        this.matchType = matchType;
    }

    public VideoChatEntity getVideoChatConfig() {
        return mVideoChatEntity;
    }

    public void setVideoChatConfig(VideoChatEntity videoChatEntity) {
        mVideoChatEntity = videoChatEntity;
    }

    public @ConditionEntity.RequestType
    int getRequestType() {
        return mRequestType;
    }

    public void setRequestType(@ConditionEntity.RequestType int requestType) {
        mRequestType = requestType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.sex);
        dest.writeInt(this.age);
        dest.writeInt(this.mask);
        dest.writeInt(this.mRequestType);
        dest.writeInt(this.mUid);
        dest.writeInt(this.matchType);
        dest.writeParcelable(this.mVideoChatEntity, flags);
    }

    protected MatchConfig(Parcel in) {
        this.type = in.readInt();
        this.sex = in.readInt();
        this.age = in.readInt();
        this.mask = in.readInt();
        this.mRequestType = in.readInt();
        this.mUid = in.readInt();
        this.matchType = in.readInt();
        this.mVideoChatEntity = in.readParcelable(VideoChatEntity.class.getClassLoader());
    }

    public static final Creator<MatchConfig> CREATOR = new Creator<MatchConfig>() {
        @Override
        public MatchConfig createFromParcel(Parcel source) {
            return new MatchConfig(source);
        }

        @Override
        public MatchConfig[] newArray(int size) {
            return new MatchConfig[size];
        }
    };
}