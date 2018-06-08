package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.request.AppointmentSettingRequest;

public interface IAppoinmentSettingModel {
    /**
     * 获取预约设置信息
     *
     * @param context
     * @param params
     * @param callBack
     */
    void getSettingInfo(Context context, AppointmentRequest params, DataCallBack callBack);

    /**
     * 设置预约设置信息
     *
     * @param context
     * @param params
     * @param callBack
     */
    void submitSettingInfo(Context context, AppointmentSettingRequest params, DataCallBack callBack);
}
