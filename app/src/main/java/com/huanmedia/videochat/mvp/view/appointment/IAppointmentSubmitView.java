package com.huanmedia.videochat.mvp.view.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;

public interface IAppointmentSubmitView extends BaseMVPView {
    /***
     * 提交预约成功
     */
    void submitAppointmentSuccess(AppointmentUserInfoResults data);

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
