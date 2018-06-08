package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.request.PageRequest;

public interface IAppointmentListModel {

    /**
     * 获取预约列表信息（For 红人）
     *
     * @param context
     * @param params
     * @param callBack
     */
    void getAppointmentListForReadMan(Context context, PageRequest params, DataCallBack callBack);


    /**
     * 获取预约列表信息（For 普通人）
     * @param context
     * @param params
     * @param callBack
     */
    void getAppointmentListForNormal(Context context,PageRequest params,DataCallBack callBack);
}
