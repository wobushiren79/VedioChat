package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.huanmedia.videochat.main2.weight.ConditionEntity;


/**
 * Create by Administrator
 * time: 2017/12/8.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class VideoChatEntity implements Parcelable {


    public VideoChatEntity(int _location_videoType, int _endFlag) {
        this._location_videoType = _location_videoType;
        this._endFlag = _endFlag;
    }

    public VideoChatEntity() {
    }

    /**
     * type : begin1v1chatnotice
     * fromuid : 1
     * touid : 2
     * begintime : 1512725633
     * endtime : 1512725693
     * frommask : 2
     * tomask : 1
     * issearch : 1
     * callid : 25
     * channelName : f42529f8c36b1f8a3a804004976bf1eb
     * touidinfo : {"uid":2,"sex":1,"age":20,"userphoto_thumb":"http://mmimg.kdlz.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg","area":"安庆市","lng":"104.0921069","lat":"30.6797665","distince":"0"}
     */

    private  int _location_videoType;//本地判断视频模式
    private int _endFlag=0;


    private String type;
    private int fromuid;
    private int touid;
    private int begintime;
    private int endtime;
    private int frommask;
    private int tomask;
    private int issearch;
    private String callid;
    private int chatcoin;
    private String channelName;
    private int favorited;
    private String extDataString;
    private TouidinfoEntity touidinfo;


    public String getExtDataString() {
        return extDataString;
    }

    public void setExtDataString(String extDataString) {
        this.extDataString = extDataString;
    }

    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
    }

    /**
     *
     * @return 视频模式 {@link ConditionEntity.VideoType}
     */
    public @ConditionEntity.VideoType
    int get_location_videoType() {
        return _location_videoType;
    }
    /**
     * @param _location_videoType 视频模式 {@link ConditionEntity.VideoType}
     * @return
     */
    public void set_location_VideoType(@ConditionEntity.VideoType int _location_videoType) {
        this._location_videoType = _location_videoType;
    }

    public int getChatcoin() {
        return chatcoin;
    }

    public void setChatcoin(int chatcoin) {
        this.chatcoin = chatcoin;
    }
    public int get_endFlag() {
        return _endFlag;
    }

    public void set_endFlag(int _endFlag) {
        this._endFlag = _endFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFromuid() {
        return fromuid;
    }

    public void setFromuid(int fromuid) {
        this.fromuid = fromuid;
    }

    public int getTouid() {
        return touid;
    }

    public void setTouid(int touid) {
        this.touid = touid;
    }

    public int getBegintime() {
        return begintime;
    }

    public void setBegintime(int begintime) {
        this.begintime = begintime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public int getFrommask() {
        return frommask;
    }

    public void setFrommask(int frommask) {
        this.frommask = frommask;
    }

    public int getTomask() {
        return tomask;
    }

    public void setTomask(int tomask) {
        this.tomask = tomask;
    }

    public int getIssearch() {
        return issearch;
    }

    public void setIssearch(int issearch) {
        this.issearch = issearch;
    }

    public String getCallid() {
        return callid;
    }

    public void setCallid(String callid) {
        this.callid = callid;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public TouidinfoEntity getTouidinfo() {
        return touidinfo;
    }

    public void setTouidinfo(TouidinfoEntity touidinfo) {
        this.touidinfo = touidinfo;
    }


    public static class TouidinfoEntity implements Parcelable {
        /**
         * uid : 2
         * sex : 1
         * age : 20
         * userphoto_thumb : http://mmimg.kdlz.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg
         * area : 安庆市
         * lng : 104.0921069
         * lat : 30.6797665
         * distince : 0
         */

        private int uid;
        private int sex;
        private int age;
        private String userphoto_thumb;
        private String area;
        private String lng;
        private String lat;
        private String distince;
        /**
         * nickname : 小A
         */

        private String nickname;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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

        public String getUserphoto_thumb() {
            return userphoto_thumb;
        }

        public void setUserphoto_thumb(String userphoto_thumb) {
            this.userphoto_thumb = userphoto_thumb;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
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

        public String getDistince() {
            return distince;
        }

        public void setDistince(String distince) {
            this.distince = distince;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public TouidinfoEntity() {
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("TouidinfoEntity{");
            sb.append("uid=").append(uid);
            sb.append(", sex=").append(sex);
            sb.append(", age=").append(age);
            sb.append(", userphoto_thumb='").append(userphoto_thumb).append('\'');
            sb.append(", area='").append(area).append('\'');
            sb.append(", lng='").append(lng).append('\'');
            sb.append(", lat='").append(lat).append('\'');
            sb.append(", distince='").append(distince).append('\'');
            sb.append(", nickname='").append(nickname).append('\'');
            sb.append('}');
            return sb.toString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.uid);
            dest.writeInt(this.sex);
            dest.writeInt(this.age);
            dest.writeString(this.userphoto_thumb);
            dest.writeString(this.area);
            dest.writeString(this.lng);
            dest.writeString(this.lat);
            dest.writeString(this.distince);
            dest.writeString(this.nickname);
        }

        protected TouidinfoEntity(Parcel in) {
            this.uid = in.readInt();
            this.sex = in.readInt();
            this.age = in.readInt();
            this.userphoto_thumb = in.readString();
            this.area = in.readString();
            this.lng = in.readString();
            this.lat = in.readString();
            this.distince = in.readString();
            this.nickname = in.readString();
        }

        public static final Creator<TouidinfoEntity> CREATOR = new Creator<TouidinfoEntity>() {
            @Override
            public TouidinfoEntity createFromParcel(Parcel source) {
                return new TouidinfoEntity(source);
            }

            @Override
            public TouidinfoEntity[] newArray(int size) {
                return new TouidinfoEntity[size];
            }
        };
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("VideoChatEntity{");
        sb.append("_location_videoType=").append(_location_videoType);
        sb.append(", _endFlag=").append(_endFlag);
        sb.append(", type='").append(type).append('\'');
        sb.append(", fromuid=").append(fromuid);
        sb.append(", touid=").append(touid);
        sb.append(", begintime=").append(begintime);
        sb.append(", endtime=").append(endtime);
        sb.append(", frommask=").append(frommask);
        sb.append(", tomask=").append(tomask);
        sb.append(", issearch=").append(issearch);
        sb.append(", callid='").append(callid).append('\'');
        sb.append(", chatcoin=").append(chatcoin);
        sb.append(", channelName='").append(channelName).append('\'');
        sb.append(", touidinfo=").append(touidinfo);
        sb.append(", extDataString=").append(extDataString);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._location_videoType);
        dest.writeInt(this._endFlag);
        dest.writeString(this.type);
        dest.writeInt(this.fromuid);
        dest.writeInt(this.touid);
        dest.writeInt(this.begintime);
        dest.writeInt(this.endtime);
        dest.writeInt(this.frommask);
        dest.writeInt(this.tomask);
        dest.writeInt(this.issearch);
        dest.writeString(this.callid);
        dest.writeInt(this.chatcoin);
        dest.writeString(this.channelName);
        dest.writeString(this.extDataString);
        dest.writeInt(this.favorited);
        dest.writeParcelable(this.touidinfo, flags);
    }

    protected VideoChatEntity(Parcel in) {
        this._location_videoType = in.readInt();
        this._endFlag = in.readInt();
        this.type = in.readString();
        this.fromuid = in.readInt();
        this.touid = in.readInt();
        this.begintime = in.readInt();
        this.endtime = in.readInt();
        this.frommask = in.readInt();
        this.tomask = in.readInt();
        this.issearch = in.readInt();
        this.callid = in.readString();
        this.chatcoin = in.readInt();
        this.channelName = in.readString();
        this.extDataString = in.readString();
        this.favorited = in.readInt();
        this.touidinfo = in.readParcelable(TouidinfoEntity.class.getClassLoader());
    }

    public static final Creator<VideoChatEntity> CREATOR = new Creator<VideoChatEntity>() {
        @Override
        public VideoChatEntity createFromParcel(Parcel source) {
            return new VideoChatEntity(source);
        }

        @Override
        public VideoChatEntity[] newArray(int size) {
            return new VideoChatEntity[size];
        }
    };
}
