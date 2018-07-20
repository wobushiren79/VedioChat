package com.huanmedia.videochat.mvp.view.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataOpResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListOpResults;

import java.util.List;

public interface IAppointmentListOpView extends BaseMVPView {

    /**
     * 获取列表数据成功
     *
     * @param results
     */
    void getAppointmentListSuccess(AppointmentListOpResults results);

    /**
     * 获取列表数据失败
     *
     * @param msg
     */
    void getAppointmentListFail(String msg);

    /**
     * 获取请求页数
     *
     * @return
     */
    int getAppointmentListPage();

    /**
     * 获取请求页数大小
     *
     * @return
     */
    int getAppointmentListPageSize();

    /**
     * 获取请求列表年份
     *
     * @return
     */
    int getAppointmentListYear();

    /**
     * 获取请求列表月份
     *
     * @return
     */
    int getAppointmentListMonth();

    /**
     * 获取请求列表状态
     *
     * @return
     */
    String getAppointmentListStatus();


    /**
     * 设置列表数据
     * @param listData
     */
    void setAppointmentListData(AppointmentListOpResults results,List<AppointmentDataOpResults> listData);
}
