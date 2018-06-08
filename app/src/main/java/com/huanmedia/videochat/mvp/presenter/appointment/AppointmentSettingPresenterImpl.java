package com.huanmedia.videochat.mvp.presenter.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.request.AppointmentSettingRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentSettingResults;
import com.huanmedia.videochat.mvp.model.appointment.AppointmentSettingModelImpl;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentSettingView;

public class AppointmentSettingPresenterImpl extends BaseMVPPresenter<IAppointmentSettingView, AppointmentSettingModelImpl> implements IAppointmentSettingPresenter {

    public AppointmentSettingPresenterImpl(IAppointmentSettingView mMvpView) {
        super(mMvpView, AppointmentSettingModelImpl.class);
    }

    @Override
    public void getSettingInfo() {
        if (mMvpView.getContext() == null)
            return;
        AppointmentRequest params = new AppointmentRequest();
        mMvpModel.getSettingInfo(mMvpView.getContext(), params, new DataCallBack<AppointmentSettingResults>() {

            @Override
            public void getDataSuccess(AppointmentSettingResults data) {
                mMvpView.setAppointmentType(data.getStatus());
                mMvpView.setAppointmentTimeQuantum(data.getBtime(), data.getEtime());
                String timeStatusStr;
                switch (data.getDset()) {
                    case 1:
                        timeStatusStr = "每天";
                        break;
                    case 2:
                        timeStatusStr = "工作日";
                        break;
                    case 3:
                        timeStatusStr = "周末";
                        break;
                    default:
                        timeStatusStr = "";
                        break;
                }
                mMvpView.setAppointmentTimeStatus(data.getDset(), timeStatusStr);
                mMvpView.getAppointmentSettingSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getAppointmentSettingFail(msg);
            }
        });
    }

    @Override
    public void submitSettingInfo(int submitType) {
        if (mMvpView.getContext() == null)
            return;

        String timeStatusStr = mMvpView.getAppointmentTimeStatus();
        String timeQuantum = mMvpView.getAppointmentTimeQuantum();
        int type = mMvpView.getAppointmentType();
        int timeStatus = 1;
        if (timeStatusStr.equals("每天")) {
            timeStatus = 1;
        } else if (timeStatusStr.equals("工作日")) {
            timeStatus = 2;
        } else if (timeStatusStr.equals("周末")) {
            timeStatus = 3;
        }
        String[] timeData = timeQuantum.split("-");
        String beginTime = null;
        String endTime = null;
        if (timeData != null && timeData.length == 2) {
            beginTime = timeData[0] + ":00";
            endTime = timeData[1] + ":00";
        }

        AppointmentSettingRequest params = new AppointmentSettingRequest();
        switch (submitType) {
            case 1:
                break;
            case 2:
                if (timeStatus == 0) {
                    mMvpView.showToast("还未设置常在时段");
                    return;
                }
                break;
            case 3:
                if (beginTime == null || endTime == null) {
                    mMvpView.showToast("还未选择在线时间段");
                    return;
                }
                break;
            default:
                break;
        }
        params.setDset(timeStatus);
        params.setStatus(type);
        params.setTbegin(beginTime);
        params.setTend(endTime);
        mMvpModel.submitSettingInfo(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.submitAppointmentSettingSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.submitAPpointmentSettingFail(msg);
            }
        });
    }
}
