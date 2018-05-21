package com.huanmedia.videochat.mvp.model.user;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;

public interface IAppointmentUserInfoModel {
    /**
     * 获取预约红人信息
     *
     * @param context
     * @param params
     * @param callBack
     */
    void getAppointmentUserInfo(Context context, AppointmentRequest params, DataCallBack callBack);
}
