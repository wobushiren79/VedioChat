package com.huanmedia.videochat.mvp.view.user;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;

import java.util.List;

public interface IAppointmentUserInfoView extends BaseMVPView {
    /**
     * 获取预约红人用户数据成功
     *
     * @param results
     */
    void getAppointmentUserInfoSuccess(AppointmentUserInfoResults results);

    /**
     * 获取预约红人数据失败
     *
     * @param msg
     */
    void getAppointmentUserInfoFail(String msg);

    /**
     * 设置预约红人头像
     * @param photoUrl
     */
    void setAppointmentUserIcon(String photoUrl);

    /**
     * 设置预约红人姓名
     * @param name
     */
    void setAppointmentUserName(String name);

    /**
     * 设置预约红人费用
     * @param charge
     */
    void setAppointmentUserCharge(String charge);

    /**
     * 设置预约红人上线时间
     * @param time
     */
    void setAppointmentUserOnlineTime(String timeStatus,String time);


    /**
     * 设置预约红人押金
     * @param deposit
     */
    void setAppointmentDeposit(String deposit);

    /**
     * 设置预约列表数据
     * @param listData
     */
    void setAppointmentListData(List<AppointmentDataResults> listData);
}
