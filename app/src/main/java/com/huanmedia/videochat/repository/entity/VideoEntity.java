package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoEntity implements Parcelable {

    private int id;
    private String voideurl;
    private String imgurl;
    private int uploadStatus;
    private int loadPB = 0;//上传进度
    private int isSelect=0;//是否选中

    public int getLoadPB() {
        return loadPB;
    }

    public void setLoadPB(int loadPB) {
        this.loadPB = loadPB;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoideurl() {
        return voideurl;
    }

    public void setVoideurl(String voideurl) {
        this.voideurl = voideurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public static final Creator<VideoEntity> CREATOR = new Creator<VideoEntity>() {
        @Override
        public VideoEntity createFromParcel(Parcel in) {
            return new VideoEntity(in);
        }

        @Override
        public VideoEntity[] newArray(int size) {
            return new VideoEntity[size];
        }
    };

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("VideoEntity{");
        sb.append("id=").append(id);
        sb.append(", voideurl='").append(voideurl).append('\'');
        sb.append(", imgurl='").append(imgurl).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected VideoEntity(Parcel in) {
        this.id = in.readInt();
        this.voideurl = in.readString();
        this.imgurl = in.readString();
    }

    public VideoEntity() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.voideurl);
        dest.writeString(this.imgurl);
    }
}
