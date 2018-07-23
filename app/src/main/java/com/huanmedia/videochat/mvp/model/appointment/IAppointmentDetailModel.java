package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;

public interface IAppointmentDetailModel {
    /**
     * 获取详情
     * @param context
     * @param params
     * @param callBack
     */
    void getDetails(Context context, AppointmentRequest params, DataCallBack callBack);
}
