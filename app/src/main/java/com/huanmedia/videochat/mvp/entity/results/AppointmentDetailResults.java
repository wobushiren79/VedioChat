package com.huanmedia.videochat.mvp.entity.results;

public class AppointmentDetailResults {
    private UserInfo ouinfo;
    private UserInfo myifno;
    private AppointmentDataOpResults.OrderInfo detail;


    public UserInfo getOuinfo() {
        return ouinfo;
    }

    public void setOuinfo(UserInfo ouinfo) {
        this.ouinfo = ouinfo;
    }

    public UserInfo getMyifno() {
        return myifno;
    }

    public void setMyifno(UserInfo myifno) {
        this.myifno = myifno;
    }

    public AppointmentDataOpResults.OrderInfo getDetail() {
        return detail;
    }

    public void setDetail(AppointmentDataOpResults.OrderInfo detail) {
        this.detail = detail;
    }

    public static class UserInfo {
        private int uid;
        private String nickname;
        private String userphoto_thumb;
        private int sex;
        private int isstarauth;
        private int starbutton;
        private int starcoin;

        public String getUserphoto_thumb() {
            return userphoto_thumb;
        }

        public void setUserphoto_thumb(String userphoto_thumb) {
            this.userphoto_thumb = userphoto_thumb;
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

        public int getStarcoin() {
            return starcoin;
        }

        public void setStarcoin(int starcoin) {
            this.starcoin = starcoin;
        }
    }


}
