package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ericYang on 2017/5/18.
 * Email:eric.yang@huanmedia.com
 * 用于网络解析
 */

public class UserEntity implements Serializable, Parcelable {


    /**
     * id : 5
     * mobile : 18580764904
     * token : 9e6ortf03heu2qgnsqod8m6vu5
     * autoString : ODlmODBJSGtaTldKcitYTjNSWFVLRGxieE93Ni91NlpXTWIzd1lkOC9OWTNMRCtEV0hvdFNVQ3czRk8vSDhvaGM5RitrcTFQdVVjdWhWYkxibExycHdiTXdTbHMyNmFyajBnVjAxWnljS01jSm9zdzdaQVNFQnlrT3ZVZm1xSHNXa2NPSmpYRGprNHNiWHhOUnFLOWZoUjRsZDRSM2tsVUpBQmlGRlFHbXl5SENFQU1KeUxFQnU4WG1QNE9FUzF4NCtVcHRRWGFqUHFDQTFaYW0xcjBMQTdQR0E=
     * extdataflag : 1
     * time : 1511834925
     * userinfo : {"id":5,"username":"18580764904","nickname":"大大","sex":1,"userphoto":"","userphoto_thumb":"","age":36,"occupation":"","province":"","city":"","startime":0,"starcoin":0,"lng":"0.0000000","lat":"0.0000000","isstarauth":-1,"starbutton":0,"birthday":"2001-05-06"}
     */

    private long id;
    private String mobile;
    private String token;
    private String autoString;
    private int extdataflag;
    private String time;
    private UserinfoEntity userinfo;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAutoString() {
        return autoString;
    }

    public void setAutoString(String autoString) {
        this.autoString = autoString;
    }

    public int getExtdataflag() {
        return extdataflag;
    }

    public void setExtdataflag(int extdataflag) {
        this.extdataflag = extdataflag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserinfoEntity getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoEntity userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoEntity implements Parcelable, Serializable {
        /**
         * username : 18580764904
         * nickname : 大大
         * sex : 1
         * userphoto :
         * userphoto_thumb :
         * age : 36
         * occupation : null
         * province :
         * city :
         * startime : 0
         * starcoin : 0
         * lng : 0.0000000
         * lat : 0.0000000
         * isstarauth : -1
         * starbutton : 0
         * birthday : 2001-05-06
         *level: 1
         */

        private String username;
        private String nickname;
        private int sex;
        private String userphoto;
        private String userphoto_thumb;
        private String age;
        private int startime;
        private int starcoin;
        private String lng;
        private String lat;
        private int isstarauth;
        private int starbutton;
        private int coin;
        private int exchangecoin;
        private int level;
        private int trustvalue;


        public int getTrustvalue() {
            return trustvalue;
        }

        public void setTrustvalue(int trustvalue) {
            this.trustvalue = trustvalue;
        }

        /**
         * id : 6
         * age : 50
         * province : {"id":150000,"name":"内蒙古"}
         * city : {"id":150700,"name":"呼伦贝尔市"}
         */

        private ItemMenuEntity province;
        private ItemMenuEntity.SubEntity city;
        private ItemMenuEntity occupation;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getExchangecoin() {
            return exchangecoin;
        }

        public void setExchangecoin(int exchangecoin) {
            this.exchangecoin = exchangecoin;
        }

        private String birthday;

        public ItemMenuEntity getOccupation() {
            return occupation;
        }

        public void setOccupation(ItemMenuEntity occupation) {
            this.occupation = occupation;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public String getUserphoto() {
            return userphoto;
        }

        public void setUserphoto(String userphoto) {
            this.userphoto = userphoto;
        }

        public String getUserphoto_thumb() {
            return userphoto_thumb;
        }

        public void setUserphoto_thumb(String userphoto_thumb) {
            this.userphoto_thumb = userphoto_thumb;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
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

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public int getIsstarauth() {
            return isstarauth;
        }

        public void setIsstarauth(int isstarauth) {
            this.isstarauth = isstarauth;
        }

        public int getStarbutton() {
            return starbutton;
        }

        public void setStarbutton(int starbutton) {
            this.starbutton = starbutton;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public UserinfoEntity() {
        }

        public ItemMenuEntity getProvince() {
            return province;
        }

        public void setProvince(ItemMenuEntity province) {
            this.province = province;
        }

        public ItemMenuEntity.SubEntity getCity() {
            return city;
        }

        public void setCity(ItemMenuEntity.SubEntity city) {
            this.city = city;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.username);
            dest.writeString(this.nickname);
            dest.writeInt(this.sex);
            dest.writeString(this.userphoto);
            dest.writeString(this.userphoto_thumb);
            dest.writeString(this.age);
            dest.writeInt(this.startime);
            dest.writeInt(this.starcoin);
            dest.writeString(this.lng);
            dest.writeString(this.lat);
            dest.writeInt(this.isstarauth);
            dest.writeInt(this.starbutton);
            dest.writeInt(this.coin);
            dest.writeInt(this.exchangecoin);
            dest.writeInt(this.level);
            dest.writeInt(this.trustvalue);
            dest.writeParcelable(this.province, flags);
            dest.writeParcelable(this.city, flags);
            dest.writeParcelable(this.occupation, flags);
            dest.writeString(this.birthday);
        }

        protected UserinfoEntity(Parcel in) {
            this.username = in.readString();
            this.nickname = in.readString();
            this.sex = in.readInt();
            this.userphoto = in.readString();
            this.userphoto_thumb = in.readString();
            this.age = in.readString();
            this.startime = in.readInt();
            this.starcoin = in.readInt();
            this.lng = in.readString();
            this.lat = in.readString();
            this.isstarauth = in.readInt();
            this.starbutton = in.readInt();
            this.coin = in.readInt();
            this.exchangecoin = in.readInt();
            this.level = in.readInt();
            this.trustvalue = in.readInt();
            this.province = in.readParcelable(ItemMenuEntity.class.getClassLoader());
            this.city = in.readParcelable(ItemMenuEntity.SubEntity.class.getClassLoader());
            this.occupation = in.readParcelable(ItemMenuEntity.class.getClassLoader());
            this.birthday = in.readString();
        }

        public static final Creator<UserinfoEntity> CREATOR = new Creator<UserinfoEntity>() {
            @Override
            public UserinfoEntity createFromParcel(Parcel source) {
                return new UserinfoEntity(source);
            }

            @Override
            public UserinfoEntity[] newArray(int size) {
                return new UserinfoEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.mobile);
        dest.writeString(this.token);
        dest.writeString(this.autoString);
        dest.writeInt(this.extdataflag);
        dest.writeString(this.time);
        dest.writeParcelable(this.userinfo, flags);
    }

    public UserEntity() {
    }

    protected UserEntity(Parcel in) {
        this.id = in.readLong();
        this.mobile = in.readString();
        this.token = in.readString();
        this.autoString = in.readString();
        this.extdataflag = in.readInt();
        this.time = in.readString();
        this.userinfo = in.readParcelable(UserinfoEntity.class.getClassLoader());
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}
