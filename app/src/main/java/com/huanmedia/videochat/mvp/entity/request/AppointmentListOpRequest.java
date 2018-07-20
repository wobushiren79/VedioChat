package com.huanmedia.videochat.mvp.entity.request;

import com.huanmedia.videochat.mvp.entity.results.ListDataResults;

public class AppointmentListOpRequest extends PageRequest {
    //    查看订单所属状态列表
    //            订单状态
    //0未确认
    //-1红人取消订单
    //-2自己取消订单
    //-3超时未确认
    //1红人确认
    //2已完成
    private String astatus;


    public String getAstatus() {
        return astatus;
    }

    public void setAstatus(String astatus) {
        this.astatus = astatus;
    }
}
