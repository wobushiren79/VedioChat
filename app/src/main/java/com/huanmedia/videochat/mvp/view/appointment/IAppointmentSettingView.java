package com.huanmedia.videochat.mvp.view.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.AppointmentSettingResults;

public interface IAppointmentSettingView extends BaseMVPView {

    /**
     * 获取预约设置信息成功
     *
     * @param results
     */
    void getAppointmentSettingSuccess(AppointmentSettingResults results);

    /**
     * 获取预约设置信息失败
     *
     * @param msg
     */
    void getAppointmentSettingFail(String msg);


    /**
     * 设置预约模式是否开启
     *
     * @param appointmentType 0关闭 1开启
     */
    void setAppointmentType(int appointmentType);

    /**
     * 设置预约时间段
     *
     * @param btime 开始时间
     * @param etime 结束时间
     */
    void setAppointmentTimeQuantum(String btime, String etime);

    /**
     * 设置预约时间段状态
     *
     * @param status    1 每天，2工作日 3週末
     * @param statusStr
     */
    void setAppointmentTimeStatus(int status, String statusStr);


    /**
     * 提交预约设置信息成功
     */
    void submitAppointmentSettingSuccess();


    /**
     * 提交预约设置信息失败
     *
     * @param msg
     */
    void submitAPpointmentSettingFail(String msg);

}
