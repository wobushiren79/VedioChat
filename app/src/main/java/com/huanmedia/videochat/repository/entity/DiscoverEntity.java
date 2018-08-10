package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;

import java.util.List;

/**
 * Create by Administrator
 * time: 2017/12/4.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class DiscoverEntity implements Parcelable {

    /**
     * uid : 2
     * nickname : 昵称2
     * sex : 1
     * area :
     * age : 20
     * onlinestatus
     * logintime
     * photos : [{"photo":"http://mmimg.kdlz.com/157/08/43/35/67/photos/655c59eefa9c00eff8c032c9edce2787.jpg","photo_thumb":"defimgurl","sort":1},{"photo":"http://mmimg.kdlz.com/157/08/43/35/67/photos/cc480be48d618f3cdc6bd5cce183825d.jpg","photo_thumb":"defimgurl","sort":1},{"photo":"http://mmimg.kdlz.com\\159\\28\\11\\99\\19\\photos\\5a0d1d2d2f89a.jpg","photo_thumb":"http://mmimg.kdlz.com\\159\\28\\11\\99\\19\\photos\\5a0d1d2d2f89a_thumb.jpg","sort":1}]
     */

    private int uid;
    private int onlinestatus;
    private int logintime;
    private int systemtime;
    private String nickname;
    private int sex;
    private int age;
    private String distance;
    private String occupation;
    private int startime;
    private int starcoin;
    private String userphoto_thumb;
    private List<PhotosEntity> photos;
    int favorited;
    private int starbutton;
    private int yuyueflag;
    private int hasappoint;
    private AddressInfoEntity addressinfo;

    public AddressInfoEntity getAddressinfo() {
        return addressinfo;
    }

    public void setAddressinfo(AddressInfoEntity addressinfo) {
        this.addressinfo = addressinfo;
    }

    public int getHasappoint() {
        return hasappoint;
    }

    public void setHasappoint(int hasappoint) {
        this.hasappoint = hasappoint;
    }

    public int getYuyueflag() {
        return yuyueflag;
    }

    public void setYuyueflag(int yuyueflag) {
        this.yuyueflag = yuyueflag;
    }

    public int getSystemtime() {
        return systemtime;
    }

    public void setSystemtime(int systemtime) {
        this.systemtime = systemtime;
    }

    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
    }

    public String getUserphoto_thumb() {
        return userphoto_thumb;
    }

    public void setUserphoto_thumb(String userphoto_thumb) {
        this.userphoto_thumb = userphoto_thumb;
    }

    public int getOnlinestatus() {
        return onlinestatus;
    }

    public void setOnlinestatus(int onlinestatus) {
        this.onlinestatus = onlinestatus;
    }

    public int getLogintime() {
        return logintime;
    }

    public void setLogintime(int logintime) {
        this.logintime = logintime;
    }

    public int getStartime() {
        return startime;
    }

    public void setStartime(int startime) {
        this.startime = startime;
    }

    public int getStarcoin() {
        return starcoin;
    }

    public void setStarcoin(int starcoin) {
        this.starcoin = starcoin;
    }

    /**
     * area : {"id":340800,"name":"安庆市"}
     */
    private AreaEntity area;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public List<PhotosEntity> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotosEntity> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DiscoverEntity{");
        sb.append("uid=").append(uid);
        sb.append(", nickname='").append(nickname).append('\'');
        sb.append(", sex=").append(sex);
        sb.append(", age=").append(age);
        sb.append(", distance='").append(distance).append('\'');
        sb.append(", occupation='").append(occupation).append('\'');
        sb.append(", startime=").append(startime);
        sb.append(", starcoin=").append(starcoin);
        sb.append(", userphoto_thumb='").append(userphoto_thumb).append('\'');
        sb.append(", photos=").append(photos);
        sb.append(", area=").append(area);
        sb.append(", yuyueflag=").append(yuyueflag);
        sb.append('}');
        return sb.toString();
    }

    public AreaEntity getArea() {
        return area;
    }

    public void setArea(AreaEntity area) {
        this.area = area;
    }

    public int getStarbutton() {
        return starbutton;
    }

    public void setStarbutton(int starbutton) {
        this.starbutton = starbutton;
    }

    public static class PhotosEntity implements HM_PhotoEntity {
        /**
         * photo : http://mmimg.kdlz.com/157/08/43/35/67/photos/655c59eefa9c00eff8c032c9edce2787.jpg
         * photo_thumb : defimgurl
         * sort : 1
         */

        private String photo;
        private String photo_thumb;
        private int sort;

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
            sb.append("photo='").append(photo).append('\'');
            sb.append(", photo_thumb='").append(photo_thumb).append('\'');
            sb.append(", sort=").append(sort);
            sb.append('}');
            return sb.toString();
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
            dest.writeString(this.photo);
            dest.writeString(this.photo_thumb);
            dest.writeInt(this.sort);
        }

        public PhotosEntity() {
        }

        protected PhotosEntity(Parcel in) {
            this.photo = in.readString();
            this.photo_thumb = in.readString();
            this.sort = in.readInt();
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

    public static class AreaEntity implements Parcelable {
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("AreaEntity{");
            sb.append("id=").append(id);
            sb.append(", name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }

        /**
         * id : 340800
         * name : 安庆市
         */

        private int id;
        private String name;

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public AreaEntity() {
        }

        protected AreaEntity(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Creator<AreaEntity> CREATOR = new Creator<AreaEntity>() {
            @Override
            public AreaEntity createFromParcel(Parcel source) {
                return new AreaEntity(source);
            }

            @Override
            public AreaEntity[] newArray(int size) {
                return new AreaEntity[size];
            }
        };
    }

    public DiscoverEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.uid);
        dest.writeInt(this.onlinestatus);
        dest.writeInt(this.logintime);
        dest.writeInt(this.systemtime);
        dest.writeString(this.nickname);
        dest.writeInt(this.sex);
        dest.writeInt(this.age);
        dest.writeString(this.distance);
        dest.writeString(this.occupation);
        dest.writeInt(this.startime);
        dest.writeInt(this.starcoin);
        dest.writeString(this.userphoto_thumb);
        dest.writeTypedList(this.photos);
        dest.writeInt(this.favorited);
        dest.writeInt(this.starbutton);
        dest.writeInt(this.yuyueflag);
        dest.writeParcelable(this.area, flags);
    }

    protected DiscoverEntity(Parcel in) {
        this.uid = in.readInt();
        this.onlinestatus = in.readInt();
        this.logintime = in.readInt();
        this.systemtime = in.readInt();
        this.nickname = in.readString();
        this.sex = in.readInt();
        this.age = in.readInt();
        this.distance = in.readString();
        this.occupation = in.readString();
        this.startime = in.readInt();
        this.starcoin = in.readInt();
        this.userphoto_thumb = in.readString();
        this.photos = in.createTypedArrayList(PhotosEntity.CREATOR);
        this.favorited = in.readInt();
        this.starbutton = in.readInt();
        this.yuyueflag = in.readInt();
        this.area = in.readParcelable(AreaEntity.class.getClassLoader());
    }

    public static final Creator<DiscoverEntity> CREATOR = new Creator<DiscoverEntity>() {
        @Override
        public DiscoverEntity createFromParcel(Parcel source) {
            return new DiscoverEntity(source);
        }

        @Override
        public DiscoverEntity[] newArray(int size) {
            return new DiscoverEntity[size];
        }
    };
}
