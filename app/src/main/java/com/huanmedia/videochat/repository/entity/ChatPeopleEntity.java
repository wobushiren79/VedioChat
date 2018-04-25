package com.huanmedia.videochat.repository.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Administrator
 * time: 2017/12/18.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 * 聊过的人和偶遇的人
 */

public class ChatPeopleEntity {

    /**
     * total : 5
     * page : 1
     * totalpage : 1
     * items : [{"uid":6,"nickname":"杨杨","sex":1,"userphoto_thumb":"http://mmimg.kdlz.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg","city":"呼伦贝尔市","isstarauth":-1,"onlinestatus":1,"starbutton":0,"startime":5,"starcoin":50,"distance":""},{"uid":5,"nickname":"大大","sex":1,"userphoto_thumb":"http://mmimg.kdlz.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg","city":"","isstarauth":1,"starbutton":0,"startime":5,"starcoin":50,"onlinestatus":0,"distance":""},{"uid":4,"nickname":"小小","sex":2,"userphoto_thumb":"http://mmimg.kdlz.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg","city":"","isstarauth":1,"starbutton":0,"startime":5,"starcoin":50,"onlinestatus":1,"distance":""},{"uid":3,"nickname":"昵称3","sex":1,"userphoto_thumb":"http://mmimg.kdlz.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg","city":"","isstarauth":1,"starbutton":0,"startime":5,"starcoin":50,"onlinestatus":1,"distance":20249},{"uid":1,"nickname":"昵称1","sex":2,"userphoto_thumb":"http://mmimg.kdlz.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg","city":"","isstarauth":-1,"starbutton":0,"startime":5,"starcoin":50,"onlinestatus":0,"distance":""}]
     */
    private int pagesize;
    private int total;
    private int page;
    private int totalpage;

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    private List<ItemsEntity> items=new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public List<ItemsEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public static class ItemsEntity {
        /**
         * uid : 6
         * nickname : 杨杨
         * sex : 1
         * userphoto_thumb : http://mmimg.kdlz.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg
         * city : 呼伦贝尔市
         * isstarauth : -1
         * onlinestatus : 1
         * starbutton : 0
         * startime : 5
         * starcoin : 50
         * distance :
         * lastuptime:1548785420
         */

        private int uid;
        private String nickname;
        private int sex;
        private String userphoto_thumb;
        private String city;
        private int isstarauth;
        private int onlinestatus;
        private int starbutton;
        private int startime;
        private int starcoin;
        private String distance;
        private int lastuptime;
        private int coin;

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getLastuptime() {
            return lastuptime;
        }

        public void setLastuptime(int lastuptime) {
            this.lastuptime = lastuptime;
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

        public String getUserphoto_thumb() {
            return userphoto_thumb;
        }

        public void setUserphoto_thumb(String userphoto_thumb) {
            this.userphoto_thumb = userphoto_thumb;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getIsstarauth() {
            return isstarauth;
        }

        public void setIsstarauth(int isstarauth) {
            this.isstarauth = isstarauth;
        }

        public int getOnlinestatus() {
            return onlinestatus;
        }

        public void setOnlinestatus(int onlinestatus) {
            this.onlinestatus = onlinestatus;
        }

        public int getStarbutton() {
            return starbutton;
        }

        public void setStarbutton(int starbutton) {
            this.starbutton = starbutton;
        }

        public int getStartime() {
            return startime;
        }

        public void setStartime(int startime) {
            this.startime = startime;
        }

        public void setStarcoin(int starcoin) {
            this.starcoin = starcoin;
        }

        public int getStarcoin() {
            return starcoin;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("ItemsEntity{");
            sb.append("uid=").append(uid);
            sb.append(", nickname='").append(nickname).append('\'');
            sb.append(", sex=").append(sex);
            sb.append(", userphoto_thumb='").append(userphoto_thumb).append('\'');
            sb.append(", city='").append(city).append('\'');
            sb.append(", isstarauth=").append(isstarauth);
            sb.append(", onlinestatus=").append(onlinestatus);
            sb.append(", starbutton=").append(starbutton);
            sb.append(", startime=").append(startime);
            sb.append(", starcoin=").append(starcoin);
            sb.append(", distance='").append(distance).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
