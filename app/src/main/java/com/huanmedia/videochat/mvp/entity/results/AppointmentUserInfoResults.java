package com.huanmedia.videochat.mvp.entity.results;

import java.util.List;

public class AppointmentUserInfoResults {
    private Base base;
    private List<AppointmentDataResults> list;
    private int coin;

    public List<AppointmentDataResults> getList() {
        return list;
    }

    public void setList(List<AppointmentDataResults> list) {
        this.list = list;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public static class Base {
        private String nickname;
        private int starcoin;
        private String appointbegin;
        private String appointend;
        private int appoindset;//1每天  2工作日  3周末
        private String userphoto_thumb;
        private int timeinterval;//时间跨度

        public int getTimeinterval() {
            return timeinterval;
        }

        public void setTimeinterval(int timeinterval) {
            this.timeinterval = timeinterval;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getStarcoin() {
            return starcoin;
        }

        public void setStarcoin(int starcoin) {
            this.starcoin = starcoin;
        }

        public String getAppointbegin() {
            return appointbegin;
        }

        public void setAppointbegin(String appointbegin) {
            this.appointbegin = appointbegin;
        }

        public String getAppointend() {
            return appointend;
        }

        public void setAppointend(String appointend) {
            this.appointend = appointend;
        }

        public int getAppoindset() {
            return appoindset;
        }

        public void setAppoindset(int appoindset) {
            this.appoindset = appoindset;
        }

        public String getUserphoto_thumb() {
            return userphoto_thumb;
        }

        public void setUserphoto_thumb(String userphoto_thumb) {
            this.userphoto_thumb = userphoto_thumb;
        }
    }

}
