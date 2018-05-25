package com.huanmedia.videochat.mvp.view.appointment;

import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListResults;

import java.util.List;

public interface IAppointmentListView extends BaseMVPView {

    /**
     * 获取预约列表数据成功（红人）
     *
     * @param listData
     */
    void getAppointmentListForReadManSuccess(List<AppointmentListResults.Item> listData);

    /**
     * 获取预约列表数据失败（红人）
     *
     * @param msg
     */
    void getAppointmentListForReadManFail(String msg);

    /**
     * 获取预约列表数据成功（普通）
     *
     * @param listData
     */
    void getAppointmentListForNormalSuccess(List<AppointmentListResults.Item> listData);

    /**
     * 获取预约列表数据失败（普通）
     *
     * @param msg
     */
    void getAppointmentListForNormalFail(String msg);

    /**
     * 获取页数(红人)
     *
     * @return
     */
    int getPageForReadMan();

    /**
     * 获取每页数量(红人)
     *
     * @return
     */
    int getPageSizeForReadMan();

    /**
     * 获取页数(普通人)
     *
     * @return
     */
    int getPageForNormal();

    /**
     * 获取每页数量(普通人)
     *
     * @return
     */
    int getPageSizeForNormal();

    /**
     * 获取筛选条件年
     * @return
     */
    int getFiltrateYear();

    /**
     * 获取筛选条件月
     * @return
     */
    int getFiltrateMonth();

    /**
     * 获取红人下拉
     * @return
     */
    PtrLayout getPtrLayoutForReadMan();

    /**
     * 获取普通下拉
     * @return
     */
    PtrLayout getPtrLayoutForNormal();
}
