package com.huanmedia.videochat.mvp.entity.results;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AppointmentDataOpResults {
    //订单状态
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrderStatus {
        int NoConfirm = 0;//待确认
        int ReadManConfirm = 1;//红人确认
        int Complete = 2;//完成
        int ReadManCancel = -1;//红人取消订单
        int SelfCancel = -2;//自己取消订单
        int OverTime = -3;//超时
    }

    //投诉状态
    @Retention(RetentionPolicy.SOURCE)
    public @interface ComplainStatus {
        int NoComplain = 0;//未投诉
        int InComplain = 1;//投诉
        int CompleteComplain = 2;//完成投诉
    }

    private OrderInfo appinfo;
    private UserInfo uinfo;
    private Msg msg;

    public OrderInfo getAppinfo() {
        return appinfo;
    }

    public void setAppinfo(OrderInfo appinfo) {
        this.appinfo = appinfo;
    }

    public UserInfo getUinfo() {
        return uinfo;
    }

    public void setUinfo(UserInfo uinfo) {
        this.uinfo = uinfo;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public static class Msg {
        private ChatListResults.Item last;
        private int noreadcount;

        public ChatListResults.Item getLast() {
            return last;
        }

        public void setLast(ChatListResults.Item last) {
            this.last = last;
        }

        public int getNoreadcount() {
            return noreadcount;
        }

        public void setNoreadcount(int noreadcount) {
            this.noreadcount = noreadcount;
        }
    }

    public static class UserInfo {
        private int uid;
        private String nickname;
        private String userphoto_thumb;
        private int sex;
        private int isstart;


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

        public String getUserphoto_thumb() {
            return userphoto_thumb;
        }

        public void setUserphoto_thumb(String userphoto_thumb) {
            this.userphoto_thumb = userphoto_thumb;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getIsstart() {
            return isstart;
        }

        public void setIsstart(int isstart) {
            this.isstart = isstart;
        }
    }

    public static class OrderInfo {
        private int id;
        private int aid;
        private int account_id;
        private int account_vipid;
        private int from;
        private int to;
        private int paycoins;
        private int costtime;
        private long ctime;
        private long begintime;
        private long endtime;
        private long datetime;
        private int status;
        private int astatus;
        private int complaintflag;
        private int ytime;//预约时长
        //    查看订单所属状态列表
        //            订单状态
        //0未确认
        //-1红人取消订单
        //-2自己取消订单
        //-3超时未确认
        //1红人确认
        //2已完成


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getComplaintflag() {
            return complaintflag;
        }

        public void setComplaintflag(int complaintflag) {
            this.complaintflag = complaintflag;
        }

        public int getAstatus() {
            return astatus;
        }

        public void setAstatus(int astatus) {
            this.astatus = astatus;
        }

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

        public int getAccount_vipid() {
            return account_vipid;
        }

        public void setAccount_vipid(int account_vipid) {
            this.account_vipid = account_vipid;
        }

        public int getYtime() {
            return ytime;
        }

        public void setYtime(int ytime) {
            this.ytime = ytime;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getPaycoins() {
            return paycoins;
        }

        public void setPaycoins(int paycoins) {
            this.paycoins = paycoins;
        }

        public int getCosttime() {
            return costtime;
        }

        public void setCosttime(int costtime) {
            this.costtime = costtime;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public long getBegintime() {
            return begintime;
        }

        public void setBegintime(long begintime) {
            this.begintime = begintime;
        }

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }

        public long getDatetime() {
            return datetime;
        }

        public void setDatetime(long datetime) {
            this.datetime = datetime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusStr() {
            String statusStr = "未知";
            switch (status) {
                case 0:
                    statusStr = "未确认";
                    break;
                case -1:
                    statusStr = "已取消";
                    break;
                case -2:
                    statusStr = "已取消";
                    break;
                case -3:
                    statusStr = "超时";
                    break;
                case 1:
                    statusStr = "已确认";
                    break;
                case 2:
                    statusStr = "已完成";
                    break;
            }
            return statusStr;
        }

    }
}
