package com.huanmedia.videochat.mvp.view.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IAppointmentSubmitView extends BaseMVPView {
    /***
     * 提交预约成功
     */
    void submitAppointmentSuccess();

    /**
     * 提交预约失败
     *
     * @param msg
     */
    void submitAppointmentFail(String msg);

    /***
     * 获取被预约者ID
     * @return
     */
    int getAppointmentUid();

    /**
     * 获取被预约日期
     * @return
     */
    String getAppointmentDate();

    /**
     * 获取被预约时间
     * @return
     */
    String getAppointmentTime();
}
