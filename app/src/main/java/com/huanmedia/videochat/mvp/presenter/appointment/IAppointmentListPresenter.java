package com.huanmedia.videochat.mvp.presenter.appointment;

public interface IAppointmentListPresenter {

    /**
     * 获取红人预约列表数据
     */
    void getAppointmentListForReadMan();

    /**
     * 获取普通预约列表数据
     */
    void getAppointmentListForNormal();

    /**
     * 获取预约列表数据 新版
     */
    void getAppointmentListOp();
}
