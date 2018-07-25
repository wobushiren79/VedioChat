package com.huanmedia.videochat.mvp.entity.results;

import android.support.annotation.NonNull;

import com.huanmedia.videochat.repository.entity.UserEntity;

import java.util.List;

public class ChatListResults {
    private UserInfoResults myinfo;// 自己的信息
    private UserInfoResults uinfo;//留言方的信息
    private List<Item> items;//留言列表

    public UserInfoResults getMyinfo() {
        return myinfo;
    }

    public void setMyinfo(UserInfoResults myinfo) {
        this.myinfo = myinfo;
    }

    public UserInfoResults getUinfo() {
        return uinfo;
    }

    public void setUinfo(UserInfoResults uinfo) {
        this.uinfo = uinfo;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Page {
        private int page;
        private int total;
        private int totalpage;
        private int pagesize;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalpage() {
            return totalpage;
        }

        public void setTotalpage(int totalpage) {
            this.totalpage = totalpage;
        }

        public int getPagesize() {
            return pagesize;
        }

        public void setPagesize(int pagesize) {
            this.pagesize = pagesize;
        }
    }

    public static class Item implements Comparable<Item> {
        private int id;
        private int account_id;
        private int toaccount_id;
        private int msgtype;
        private long createtime;
        private String msg;
        private String virid;
        private String imgs;
        private int handle_id;
        private int handle_type;//处理类型 0：未处理(未阅读) 1.已回复(已阅读) 2.忽略
        private String handle_text;
        private long handle_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

        public int getToaccount_id() {
            return toaccount_id;
        }

        public void setToaccount_id(int toaccount_id) {
            this.toaccount_id = toaccount_id;
        }

        public int getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(int msgtype) {
            this.msgtype = msgtype;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getVirid() {
            return virid;
        }

        public void setVirid(String virid) {
            this.virid = virid;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public int getHandle_id() {
            return handle_id;
        }

        public void setHandle_id(int handle_id) {
            this.handle_id = handle_id;
        }

        public int getHandle_type() {
            return handle_type;
        }

        public void setHandle_type(int handle_type) {
            this.handle_type = handle_type;
        }

        public String getHandle_text() {
            return handle_text;
        }

        public void setHandle_text(String handle_text) {
            this.handle_text = handle_text;
        }

        public long getHandle_time() {
            return handle_time;
        }

        public void setHandle_time(long handle_time) {
            this.handle_time = handle_time;
        }

        @Override
        public int compareTo(@NonNull Item item) {
            return (int)(this.getCreatetime() - item.getCreatetime());
        }
    }
}
