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
}
