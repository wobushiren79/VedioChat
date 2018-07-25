package com.huanmedia.videochat.mvp.view.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDetailResults;

public interface IAppointmentDetailView extends BaseMVPView {
    /**
     * 获取预约详情成功
     *
     * @param results
     */
    void getAppointmentDetailSuccess(AppointmentDetailResults results);

    /**
     * 获取预约详情失败
     *
     * @param msg
     */
    void getAppointmentDetailFail(String msg);

    /**
     * 获取订单ID
     *
     * @return
     */
    int getOrderId();

    /**
     * 设置预约方姓名
     *
     * @param name
     */
    void setInitiateUserName(String name);

    /**
     * 设置预约方头像
     *
     * @param url
     */
    void setInitiateUserIcon(String url);

    /**
     * 设置预约方性别
     *
     * @param sex
     */
    void setInitiateUserSex(int sex, boolean isReadMan);

    /**
     * 设置被预约方姓名
     *
     * @param name
     */
    void setAcceptUserName(String name);

    /**
     * 设置被预约方头像
     *
     * @param url
     */
    void setAcceptUserIcon(String url);

    /**
     * 设置被预约方性别
     *
     * @param sex
     */
    void setAcceptUserSex(int sex, boolean isReadMan);

    /**
     * 设置支付过得钻石
     *
     * @param coin
     */
    void setPayPrice(int coin);

    /**
     * 设置订单持续时间
     *
     * @param startTime
     * @param endTime
     */
    void setOrderTime(String startTime, String endTime);

    /**
     * 设置订单状态
     *
     * @param orderStatus
     * @param complainStatus
     */
    void setOrderStatus(int orderStatus, int complainStatus);
}
