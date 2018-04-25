package com.huanmedia.videochat.repository.entity;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Create by Administrator
 * time: 2017/12/21.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BillDetialEntity {
    private int total;
    private int page;
    private int pagesize;
    private int totalpage;
    private List<ItemsEntity> items;

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

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
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

    public static class ItemsEntity implements Comparable<ItemsEntity>{
        /**
         * id : 30
         * name : 加时
         * type : -25
         * date : 2017-11-29
         * time : 16:10:06
         */

        private int id;
        private String name;//消费类型
        private int coin;//消费金币
        private String date;//时间
        private String time;//精确时间
        private String desc;//精确时间

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

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

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }


        @Override
        public int compareTo(@NonNull ItemsEntity o) {
            return date.compareTo(o.date);
        }
    }
}
