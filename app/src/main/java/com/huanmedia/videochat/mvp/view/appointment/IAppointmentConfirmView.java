package com.huanmedia.videochat.mvp.view.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IAppointmentConfirmView extends BaseMVPView {

    /**
     * 确认预约单成功
     */
    void confirmAppointmentSuccess(int aid);

    /**
     * 确认预约单失败
     *
     * @param msg
     */
    void confirmAppointmentFail(String msg);

    /**
     * 取消预约单成功
     */
    void cancelAppointmentSuccess(int aid);

    /**
     * 取消预约单失败
     *
     * @param msg
     */
    void cancelAppointmentFail(String msg);

    /**
     * 完成订单成功
     * @param aid
     */
    void completeAppointmentSuccess(int aid);

    /**
     * 完成订单失败
     * @param msg
     */
    void completeAppointmentFail(String msg);
}
