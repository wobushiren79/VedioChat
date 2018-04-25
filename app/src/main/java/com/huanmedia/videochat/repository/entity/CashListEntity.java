package com.huanmedia.videochat.repository.entity;

import java.util.List;

/**
 * Create by Administrator
 * time: 2017/12/21.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class CashListEntity {

    /**
     * total : 2
     * page : 1
     * pagesize : 10
     * totalpage : 1
     * items : [{"id":4,"type":200,"price":"20.00","fee_type_id":1,"fee_account":"mypaybyWeixing","createtime":"2017-12-11 15:20:00","approvaltime":"","status":-1,"fee_status":-1,"refusedesc":null},{"id":3,"type":200,"price":"20.00","fee_type_id":1,"fee_account":"mypaybyWeixing","createtime":"2017-12-11 15:19:12","approvaltime":"","status":-1,"fee_status":-1,"refusedesc":null}]
     */

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

    public static class ItemsEntity {
        /**
         * id : 4
         * type : 200
         * price : 20.00
         * fee_type_id : 1
         * fee_account : mypaybyWeixing
         * createtime : 2017-12-11 15:20:00
         * approvaltime :
         * status : -1
         * fee_status : -1
         * refusedesc : null
         */

        private int id;
        private int coin;
        private String price;
        private int fee_type_id;
        private String fee_account;
        private String createtime;
        private String approvaltime;
        private int status;
        private int fee_status;
        private Object refusedesc;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getFee_type_id() {
            return fee_type_id;
        }

        public void setFee_type_id(int fee_type_id) {
            this.fee_type_id = fee_type_id;
        }

        public String getFee_account() {
            return fee_account;
        }

        public void setFee_account(String fee_account) {
            this.fee_account = fee_account;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getApprovaltime() {
            return approvaltime;
        }

        public void setApprovaltime(String approvaltime) {
            this.approvaltime = approvaltime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getFee_status() {
            return fee_status;
        }

        public void setFee_status(int fee_status) {
            this.fee_status = fee_status;
        }

        public Object getRefusedesc() {
            return refusedesc;
        }

        public void setRefusedesc(Object refusedesc) {
            this.refusedesc = refusedesc;
        }
    }
}
