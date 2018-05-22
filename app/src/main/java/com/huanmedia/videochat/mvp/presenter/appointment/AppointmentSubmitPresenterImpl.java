package com.huanmedia.videochat.mvp.presenter.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.model.appointment.AppointmentSubmitModelImpl;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentSubmitView;

public class AppointmentSubmitPresenterImpl extends BaseMVPPresenter<IAppointmentSubmitView, AppointmentSubmitModelImpl> implements IAppointmentSubmitPresenter {

    public AppointmentSubmitPresenterImpl(IAppointmentSubmitView mMvpView) {
        super(mMvpView, AppointmentSubmitModelImpl.class);
    }

    @Override
    public void submitAppointment() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getAppointmentUid() == 0) {
            mMvpView.showToast("没有被预约者ID");
            return;
        }
        if (mMvpView.getAppointmentDate() == null || mMvpView.getAppointmentDate().length() == 0) {
            mMvpView.showToast("还没有选择预约日期");
            return;
        }
        if (mMvpView.getAppointmentTime() == null || mMvpView.getAppointmentTime().length() == 0) {
            mMvpView.showToast("还没有选择预约时间");
            return;
        }

        AppointmentRequest params = new AppointmentRequest();
        params.setDate(mMvpView.getAppointmentDate());
        params.setUid(mMvpView.getAppointmentUid());
        params.setTime(mMvpView.getAppointmentTime()+":00");
        mMvpModel.submit(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.submitAppointmentSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.submitAppointmentFail(msg);
            }
        });
    }
}
