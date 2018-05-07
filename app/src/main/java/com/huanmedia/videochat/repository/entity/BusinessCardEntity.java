package com.huanmedia.videochat.repository.entity;

import com.huanmedia.videochat.discover.adapter.BusinessMultiItem;

import java.io.Serializable;
import java.util.List;

/**
 * Create by Administrator
 * time: 2017/12/19.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BusinessCardEntity implements Serializable {
    private BusinessCardUserTags tags;//好评数量
    private UserEvaluateEntity evaluates;//好评数量
    private BaseInfo base;//好评数量

    public BaseInfo getBase() {
        return base;
    }

    public void setBase(BaseInfo base) {
        this.base = base;
    }

    public BusinessCardUserTags getTags() {
        return tags;
    }

    public void setTags(BusinessCardUserTags tags) {
        this.tags = tags;
    }

    public UserEvaluateEntity getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(UserEvaluateEntity evaluates) {
        this.evaluates = evaluates;
    }


    public static class BaseInfo   extends BusinessMultiItem  implements Serializable{
        /**
         * uid : 2
         * type : 2300
         * totalcoin : 500
         * exchangecoin : 380
         * isstarauth : 1
         * starbutton : 1
         * startime : 10
         * starcoin : 100
         * nickname : 昵称2
         * sex : 1
         * userphoto : http://mmimg.lzwifi.com/157/08/43/35/67/userphoto/5a0bbdd21c53e.jpg
         * userphoto_thumb : http://mmimg.lzwifi.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg
         * age : 20
         * birthday : 1999-01-05
         * province_id : 340000
         * city_id : 340800
         * city_name : 安庆市
         * level :1
         * phpots : [{"photo":"http://mmimg.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33.jpg","photo_thumb":"http://mmimg.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33_thumb.jpg","sort":1},{"photo":"http://mmimg.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33.jpg","photo_thumb":"http://mmimg.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33_thumb.jpg","sort":1}]
         */

        private int uid;
        private int coin;
        private int totalcoin;
        private int exchangecoin;
        private int isstarauth;
        private int starbutton;
        private int startime;
        private int starcoin;
        private String nickname;
        private int sex;
        private String userphoto;
        private String userphoto_thumb;
        private int age;
        private String birthday;
        private int province_id;
        private int city_id;
        private String city_name;
        private String occupation;
        private List<PhpotsEntity> phpots;
        private int level;//用户等级
        private int isfavorite;//是否关注 1关注 0 未关注
        private int maxtrustvalue;//评价数量
        private int trustvalue;//好评数量
        private int onlinestatus;//0不在线 1在线 2忙碌

        public String getOccupation() {
            return occupation;
        }

        public int getIsfavorite() {
            return isfavorite;
        }

        public void setIsfavorite(int isfavorite) {
            this.isfavorite = isfavorite;
        }

        public int getMaxtrustvalue() {
            return maxtrustvalue;
        }

        public void setMaxtrustvalue(int maxtrustvalue) {
            this.maxtrustvalue = maxtrustvalue;
        }

        public int getTrustvalue() {
            return trustvalue;
        }

        public void setTrustvalue(int trustvalue) {
            this.trustvalue = trustvalue;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
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

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getTotalcoin() {
            return totalcoin;
        }

        public void setTotalcoin(int totalcoin) {
            this.totalcoin = totalcoin;
        }

        public int getExchangecoin() {
            return exchangecoin;
        }

        public void setExchangecoin(int exchangecoin) {
            this.exchangecoin = exchangecoin;
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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getProvince_id() {
            return province_id;
        }

        public void setProvince_id(int province_id) {
            this.province_id = province_id;
        }

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public List<PhpotsEntity> getPhpots() {
            return phpots;
        }

        public void setPhpots(List<PhpotsEntity> phpots) {
            this.phpots = phpots;
        }

        public int getOnlinestatus() {
            return onlinestatus;
        }

        public void setOnlinestatus(int onlinestatus) {
            this.onlinestatus = onlinestatus;
        }

        @Override
        public int getItemType() {
            return BusinessMultiItem.BusinessType.HEADER;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("BaseInfo{");
            sb.append("uid=").append(uid);
            sb.append(", coin=").append(coin);
            sb.append(", totalcoin=").append(totalcoin);
            sb.append(", exchangecoin=").append(exchangecoin);
            sb.append(", isstarauth=").append(isstarauth);
            sb.append(", starbutton=").append(starbutton);
            sb.append(", startime=").append(startime);
            sb.append(", starcoin=").append(starcoin);
            sb.append(", nickname='").append(nickname).append('\'');
            sb.append(", sex=").append(sex);
            sb.append(", userphoto='").append(userphoto).append('\'');
            sb.append(", userphoto_thumb='").append(userphoto_thumb).append('\'');
            sb.append(", age=").append(age);
            sb.append(", birthday='").append(birthday).append('\'');
            sb.append(", province_id=").append(province_id);
            sb.append(", city_id=").append(city_id);
            sb.append(", city_name='").append(city_name).append('\'');
            sb.append(", occupation='").append(occupation).append('\'');
            sb.append(", phpots=").append(phpots);
            sb.append(", level=").append(level);
            sb.append(", isfavorite=").append(isfavorite);
            sb.append(", maxtrustvalue=").append(maxtrustvalue);
            sb.append(", trustvalue=").append(trustvalue);
            sb.append(", onlinestatus=").append(onlinestatus);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BusinessCardEntity{");
        sb.append("tags=").append(tags);
        sb.append(", evaluates=").append(evaluates);
        sb.append(", base=").append(base);
        sb.append('}');
        return sb.toString();
    }
}
