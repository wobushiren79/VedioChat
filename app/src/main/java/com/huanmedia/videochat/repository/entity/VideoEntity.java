package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoEntity implements Parcelable {

    private int id;
    private String video_url;
    private String video_pic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_pic() {
        return video_pic;
    }

    public void setVideo_pic(String video_pic) {
        this.video_pic = video_pic;
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
        sb.append(", video_url='").append(video_url).append('\'');
        sb.append(", video_pic='").append(video_pic).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected VideoEntity(Parcel in) {
        this.id = in.readInt();
        this.video_url = in.readString();
        this.video_pic = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.video_url);
        dest.writeString(this.video_pic);
    }
}
