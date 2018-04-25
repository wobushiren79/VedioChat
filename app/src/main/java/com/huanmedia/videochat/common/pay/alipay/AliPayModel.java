package com.huanmedia.videochat.common.pay.alipay;

import android.os.Parcel;

import com.huanmedia.videochat.common.pay.PayMode;

/**
 * Created by Administrator on 2017/4/18.
 */

public class AliPayModel implements PayMode {
    private String out_trade_no;
    private String money;
    private String name;
    private String detail;

    public AliPayModel(String out_trade_no, String money, String name, String detail) {
        this.out_trade_no = out_trade_no;
        this.money = money;
        this.name = name;
        this.detail = detail;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.out_trade_no);
        dest.writeString(this.money);
        dest.writeString(this.name);
        dest.writeString(this.detail);
    }

    protected AliPayModel(Parcel in) {
        this.out_trade_no = in.readString();
        this.money = in.readString();
        this.name = in.readString();
        this.detail = in.readString();
    }

    public static final Creator<AliPayModel> CREATOR = new Creator<AliPayModel>() {
        @Override
        public AliPayModel createFromParcel(Parcel source) {
            return new AliPayModel(source);
        }

        @Override
        public AliPayModel[] newArray(int size) {
            return new AliPayModel[size];
        }
    };
}