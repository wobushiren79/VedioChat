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
        start(aid, 1);
    }

    @Override
    public void cancelAppointment(int aid) {
        start(aid, -1);
    }

    @Override
    public void confirmAppointmentOp(int aid) {
        startOp(aid, 1);
    }

    @Override
    public void cancelAppointmentOp(int aid) {
        startOp(aid, -1);
    }

    @Override
    public void completeAppointment(int aid) {
        if (mMvpView.getContext() == null)
            return;
        if (aid == 0) {
            mMvpView.showToast("没有预约ID");
            return;
        }
        AppointmentRequest params = new AppointmentRequest();
        params.setAid(aid);
        mMvpModel.completeAppointment(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.completeAppointmentSuccess(aid);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.completeAppointmentFail(msg);
            }
        });
    }

    private void start(int aid, int submitType) {
        if (mMvpView.getContext() == null)
            return;
        if (aid == 0) {
            mMvpView.showToast("没有预约ID");
            return;
        }
        AppointmentRequest params = new AppointmentRequest();
        params.setState(submitType);
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

    private void startOp(int aid, int submitType) {
        if (mMvpView.getContext() == null)
            return;
        if (aid == 0) {
            mMvpView.showToast("没有预约ID");
            return;
        }
        AppointmentRequest params = new AppointmentRequest();
        params.setState(submitType);
        params.setAid(aid);
        mMvpModel.confirmAppointmentOp(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                if (submitType == 1) {
                    mMvpView.confirmAppointmentSuccess(aid);
                } else if (submitType == -1) {
                    mMvpView.cancelAppointmentSuccess(aid);
                }

            }

            @Override
            public void getDataFail(String msg) {
                if (submitType == 1) {
                    mMvpView.confirmAppointmentFail(msg);
                } else if (submitType == -1) {
                    mMvpView.cancelAppointmentFail(msg);
                }

            }
        });
    }
}
