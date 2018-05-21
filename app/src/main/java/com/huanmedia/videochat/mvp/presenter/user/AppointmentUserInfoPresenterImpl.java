package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;
import com.huanmedia.videochat.mvp.model.user.AppointmentUserInfoModelImpl;
import com.huanmedia.videochat.mvp.view.user.IAppointmentUserInfoView;

public class AppointmentUserInfoPresenterImpl extends BaseMVPPresenter<IAppointmentUserInfoView, AppointmentUserInfoModelImpl> implements IAppointmentUserInfoPresenter {

    public AppointmentUserInfoPresenterImpl(IAppointmentUserInfoView mMvpView) {
        super(mMvpView, AppointmentUserInfoModelImpl.class);
    }

    @Override
    public void getAppointmentInfo(int uid) {
        if (mMvpView.getContext() == null)
            return;
        if (uid == 0) {
            mMvpView.showToast("没有用户ID");
            return;
        }
        AppointmentRequest params = new AppointmentRequest();
        params.setUid(uid);
        mMvpModel.getAppointmentUserInfo(mMvpView.getContext(), params, new DataCallBack<AppointmentUserInfoResults>() {

            @Override
            public void getDataSuccess(AppointmentUserInfoResults data) {
                mMvpView.getAppointmentUserInfoSuccess(data);
                if (data.getBase() == null)
                    return;
                mMvpView.setAppointmentUserIcon(data.getBase().getUserphoto_thumb());
                mMvpView.setAppointmentUserName(data.getBase().getNickname());
                mMvpView.setAppointmentUserCharge(data.getBase().getStarcoin() + "");
                mMvpView.setAppointmentDeposit(data.getCoin() + "");
                mMvpView.setAppointmentListData(data.getList());
                String onlineStatus;
                switch (data.getBase().getAppoindset()) {
                    case 1:
                        onlineStatus = "每天";
                        break;
                    case 2:
                        onlineStatus = "工作日";
                        break;
                    case 3:
                        onlineStatus = "周末";
                        break;
                    default:
                        onlineStatus = "未知";
                        break;
                }
                mMvpView.setAppointmentUserOnlineTime(
                        onlineStatus,
                        data.getBase().getAppointbegin() + "-" + data.getBase().getAppointend());
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getAppointmentUserInfoFail(msg);
            }
        });
    }
}
