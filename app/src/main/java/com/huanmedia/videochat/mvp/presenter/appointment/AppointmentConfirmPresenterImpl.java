package com.huanmedia.videochat.mvp.presenter.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.model.appointment.AppointmentConfirmModelImpl;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentConfirmView;

public class AppointmentConfirmPresenterImpl extends BaseMVPPresenter<IAppointmentConfirmView, AppointmentConfirmModelImpl> implements IAppointmentConfirmPresenter {

    public AppointmentConfirmPresenterImpl(IAppointmentConfirmView mMvpView) {
        super(mMvpView, AppointmentConfirmModelImpl.class);
    }

    @Override
    public void confirmAppointment(int aid) {
        if (mMvpView.getContext() == null)
            return;
        if (aid == 0) {
            mMvpView.showToast("没有预约ID");
            return;
        }
        AppointmentRequest params = new AppointmentRequest();
        params.setState(1);
        params.setAid(aid);
        mMvpModel.confirmAppointment(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.confirmAppointmentSuccess(aid);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.confirmAppointmentFail(msg);
            }
        });
    }

    @Override
    public void cancelAppointment(int aid) {
        if (mMvpView.getContext() == null)
            return;
        if (aid == 0) {
            mMvpView.showToast("没有预约ID");
            return;
        }
        AppointmentRequest params = new AppointmentRequest();
        params.setState(-1);
        params.setAid(aid);
        mMvpModel.confirmAppointment(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.cancelAppointmentSuccess(aid);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.cancelAppointmentFail(msg);
            }
        });
    }
}
