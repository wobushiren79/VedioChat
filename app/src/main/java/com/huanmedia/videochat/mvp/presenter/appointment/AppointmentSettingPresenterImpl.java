package com.huanmedia.videochat.mvp.presenter.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
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
                mMvpView.getAppointmentSettingSuccess(data);
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
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getAppointmentSettingFail(msg);
            }
        });
    }

    @Override
    public void submitSettingInfo() {
        if (mMvpView.getContext() == null)
            return;
        AppointmentRequest params = new AppointmentRequest();
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
