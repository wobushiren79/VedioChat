package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;

public interface IAppointmentConfirmModel {

    /**
     * 预约确认或取消
     * @param context
     * @param params
     * @param callBack
     */
    void confirmAppointment(Context context, AppointmentRequest params, DataCallBack callBack);

}
