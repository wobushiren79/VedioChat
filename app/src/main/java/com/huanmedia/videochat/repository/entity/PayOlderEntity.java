package com.huanmedia.videochat.repository.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Create by Administrator
 * time: 2017/12/17.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class PayOlderEntity implements Serializable{


    /**
     * ordernum : SN201712171537438868041
     * result : {"Notify_URL":"http://mmapi.lzwifi.com/index/callback/callbackByWXPay","AppID":"wx22fed74481eec2c9","MchID":"1493060222","Key":"IZ4MM02S7RVJV4Y38FKVWCCRKRB1E04R","AppSecret":"2085fae910fc7cad436bebea1214e9e0"}
     */
    private String ordernum;
    private String trade_no;//支付宝支付号
    private WXEntity wxpay;

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getAlipay() {
        return trade_no;
    }

    public void setAlipay(String alipay) {
        this.trade_no = alipay;
    }

    public WXEntity getWxpay() {
        return wxpay;
    }

    public void setWxpay(WXEntity wxpay) {
        this.wxpay = wxpay;
    }

    public static class WXEntity implements Serializable {


        /**
         * appid : wx22fed74481eec2c9
         * partnerid : 1493060222
         * prepayid : wx201712172022367fc741f4b60512841347
         * package : Sign=WXPay
         * noncestr : lw0osk60k82o8dh4uhn6w4a3ujaxlecr
         * timestamp : 1513513356
         * sign : 79516C738D58481C48B4A66A864BDAE4
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String noncestr;
        private int timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
