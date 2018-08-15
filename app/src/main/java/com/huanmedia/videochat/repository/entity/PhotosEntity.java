package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;

import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;

public class PhotosEntity implements HM_PhotoEntity {
    /**
     * photo : http://mmimg.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33.jpg
     * photo_thumb : http://mmimg.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33_thumb.jpg
     * sort : 1
     */
    public int id;
    private String photo;
    private String photo_thumb;
    private int sort;
    private int status;//状态 1为审核通过 0为审核未通过 -1为未审核 -9为非法 ',
    private int plevel;//隐私类型 1公开 2隐私
    private String tag;//标签文字
    private int vcoin;//查看钻石数


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPlevel() {
        return plevel;
    }

    public void setPlevel(int plevel) {
        this.plevel = plevel;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getVcoin() {
        return vcoin;
    }

    public void setVcoin(int vcoin) {
        this.vcoin = vcoin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto_thumb() {
        return photo_thumb;
    }

    public void setPhoto_thumb(String photo_thumb) {
        this.photo_thumb = photo_thumb;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PhotosEntity{");
        sb.append("id=").append(id);
        sb.append(", photo='").append(photo).append('\'');
        sb.append(", photo_thumb='").append(photo_thumb).append('\'');
        sb.append(", sort=").append(sort).append('\'');
        sb.append(", status=").append(sort).append('\'');
        sb.append(", plevel=").append(sort).append('\'');
        sb.append(", tag=").append(sort).append('\'');
        sb.append(", vcoin=").append(sort);
        sb.append('}');
        return sb.toString();
    }


    public PhotosEntity() {
    }

    @Override
    public String getImage() {
        return photo;
    }

    @Override
    public String thumbnail() {
        return photo_thumb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.photo);
        dest.writeString(this.photo_thumb);
        dest.writeInt(this.sort);
        dest.writeInt(this.status);
        dest.writeInt(this.plevel);
        dest.writeString(this.tag);
        dest.writeInt(this.vcoin);
    }

    protected PhotosEntity(Parcel in) {
        this.id = in.readInt();
        this.photo = in.readString();
        this.photo_thumb = in.readString();
        this.sort = in.readInt();
        this.status = in.readInt();
        this.plevel = in.readInt();
        this.tag = in.readString();
        this.vcoin = in.readInt();
    }

    public static final Creator<PhotosEntity> CREATOR = new Creator<PhotosEntity>() {
        @Override
        public PhotosEntity createFromParcel(Parcel source) {
            return new PhotosEntity(source);
        }

        @Override
        public PhotosEntity[] newArray(int size) {
            return new PhotosEntity[size];
        }
    };
}
