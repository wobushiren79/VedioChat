package com.huanmedia.videochat.mvp.presenter.appointment;

public interface IAppointmentSettingPresenter {
    /**
     * 获取设置信息
     */
    void getSettingInfo();


    /**
     * 提交设置信息
     * @param submitType 0提交所有 1预约模式  2常在时间段状态 3常再时间段
     */
    void submitSettingInfo(int submitType);
}
