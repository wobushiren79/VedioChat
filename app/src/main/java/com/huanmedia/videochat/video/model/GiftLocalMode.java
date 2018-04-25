package com.huanmedia.videochat.video.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.huanmedia.videochat.video.model.GiftLocalMode.GiftType.ANIM;
import static com.huanmedia.videochat.video.model.GiftLocalMode.GiftType.SVGA;


/**
 * Create by Administrator
 * time: 2017/12/14.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class GiftLocalMode implements Parcelable {
    private String first_path;
    private String path;
    private String jsonName;
    private String imagePath;
    private @GiftType int type;
    /**
     * int LOADING = 0, TOAST = 1,WARN = 2, SUCCEED = 3;
     */
    @IntDef({SVGA, ANIM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GiftType {
        int SVGA = 0, ANIM = 1;
    }
    /**
     * @param type
     * @param path
     * @param imagePath
     * @param first_path
     * @param jsonName
     */
    public GiftLocalMode(@GiftType int type,String path,String imagePath,String first_path,String jsonName) {
        this.type = type;
        this.first_path = first_path;
        this.path = path;
        this.jsonName = jsonName;
        this.imagePath = imagePath;
    }

    public @GiftType int getType() {
        return type;
    }

    /**
     * /path/images/xx.png
     * @return
     */
    public String getFistImageAbsolute(){
        return getPath()+getImagePath()+getFirst_path();
    }

    /**
     * /path/data.json
     * @return
     */
    public String getJsonAbsolute(){
        return getPath()+getJsonName();
    }

    /**
     *
     * @return /path/images
     */
    public String getImagesAbsolute(){
        return getPath()+getImagePath();
    }

    public String getFirst_path() {
        return first_path;
    }


    public String getPath() {
        return path;
    }


    public String getJsonName() {
        return jsonName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GiftLocalMode{");
        sb.append("first_path='").append(first_path).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", jsonName='").append(jsonName).append('\'');
        sb.append(", imagePath='").append(imagePath).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.first_path);
        dest.writeString(this.path);
        dest.writeString(this.jsonName);
        dest.writeString(this.imagePath);
    }

    protected GiftLocalMode(Parcel in) {
        this.first_path = in.readString();
        this.path = in.readString();
        this.jsonName = in.readString();
        this.imagePath = in.readString();
    }

    public static final Creator<GiftLocalMode> CREATOR = new Creator<GiftLocalMode>() {
        @Override
        public GiftLocalMode createFromParcel(Parcel source) {
            return new GiftLocalMode(source);
        }

        @Override
        public GiftLocalMode[] newArray(int size) {
            return new GiftLocalMode[size];
        }
    };
}
