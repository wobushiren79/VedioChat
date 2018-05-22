package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;

public interface IAppointmentSubmitModel {
    /**
     * 提交红人预约
     *
     * @param context
     * @param params
     * @param callBack
     */
    void submit(Context context, AppointmentRequest params, DataCallBack callBack);
}
