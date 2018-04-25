package com.huanmedia.videochat.repository.entity;

import java.io.Serializable;

/**
 * code	string	返回码（0-成功 非0代表失败）
 * data	object
 * 返回给各客户端的数据
 * data.BindAccount
 * string	帮定账号
 * data.BindType
 * int	帮定账号类型  1微信 2支付宝
 * data.CanExchange
 * int	是否可以提现
 * data.exchangecoin
 * int	可提现总金额
 * data.isBindPay
 * int	是否邦定提现账号
 * data.Status
 * int	账号审核状态   1为通过 -1为审核中 -2为审核未通过 -3解除邦定状态
 * msg	string	接口状态描述
 * Create by Administrator
 * time: 2017/12/21.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class UserAccountBoundEntity  implements Serializable {

    private int exchangecoin;
    private int isBindPay;
    private int BindType;
    private String BindAccount;
    private int CanExchange;
    private int Status;

    public int getExchangecoin() {
        return exchangecoin;
    }

    public void setExchangecoin(int exchangecoin) {
        this.exchangecoin = exchangecoin;
    }

    public int getIsBindPay() {
        return isBindPay;
    }

    public void setIsBindPay(int isBindPay) {
        this.isBindPay = isBindPay;
    }

    public int getBindType() {
        return BindType;
    }

    public void setBindType(int BindType) {
        this.BindType = BindType;
    }

    public String getBindAccount() {
        return BindAccount;
    }

    public void setBindAccount(String BindAccount) {
        this.BindAccount = BindAccount;
    }

    public int getCanExchange() {
        return CanExchange;
    }

    public void setCanExchange(int CanExchange) {
        this.CanExchange = CanExchange;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }
}
