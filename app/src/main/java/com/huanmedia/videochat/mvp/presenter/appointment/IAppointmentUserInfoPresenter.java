package com.huanmedia.videochat.mvp.presenter.appointment;

public interface IAppointmentUserInfoPresenter {
    /**
     * 获取预约信息
     * @param uid
     */
    void getAppointmentInfo(int uid);

    /**
     * 获取预约信息 新版
     * @param uid
     */
    void getAppointmentInfoOp(int uid);
}
