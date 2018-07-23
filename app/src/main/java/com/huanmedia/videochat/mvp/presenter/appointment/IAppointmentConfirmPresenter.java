package com.huanmedia.videochat.mvp.presenter.appointment;

public interface IAppointmentConfirmPresenter {
    /**
     * 确认预约
     */
    void confirmAppointment(int aid);

    /**
     * 取消预约
     */
    void cancelAppointment(int aid);

    /**
     * 确认预约 新版
     */
    void confirmAppointmentOp(int aid);

    /**
     * 取消预约 新版
     */
    void cancelAppointmentOp(int aid);

    /**
     * 完成预约
     * @param aid
     */
    void completeAppointment(int aid);
}
