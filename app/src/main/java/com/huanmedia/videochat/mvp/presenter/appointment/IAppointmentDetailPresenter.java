package com.huanmedia.videochat.mvp.presenter.appointment;

public interface IAppointmentDetailPresenter {

    /**
     * 获取预约详情
     */
    void getAppointmentDetails();

    /**
     * 获取预约人ID
     * @return
     */
    int getInitiateUserId();

    /**
     * 获取被预约人ID
     * @return
     */
    int getAcceptUserId();
}
