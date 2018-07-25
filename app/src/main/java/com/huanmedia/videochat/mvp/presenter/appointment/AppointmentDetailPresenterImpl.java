package com.huanmedia.videochat.mvp.presenter.appointment;

import com.applecoffee.devtools.utils.TimeUtils;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDetailResults;
import com.huanmedia.videochat.mvp.model.appointment.AppointmentDetailModeImpl;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentDetailView;

public class AppointmentDetailPresenterImpl extends BaseMVPPresenter<IAppointmentDetailView, AppointmentDetailModeImpl> implements IAppointmentDetailPresenter {

    private int initiateUserId;
    private int acceptUserId;

    public AppointmentDetailPresenterImpl(IAppointmentDetailView mMvpView) {
        super(mMvpView, AppointmentDetailModeImpl.class);
    }


    @Override
    public void getAppointmentDetails() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getOrderId() == 0) {
            mMvpView.showToast("没有订单ID");
            return;
        }
        AppointmentRequest params = new AppointmentRequest();
        params.setAid(mMvpView.getOrderId());
        mMvpModel.getDetails(mMvpView.getContext(), params, new DataCallBack<AppointmentDetailResults>() {

            @Override
            public void getDataSuccess(AppointmentDetailResults data) {
                mMvpView.getAppointmentDetailSuccess(data);
                if (data == null || data.getDetail() == null)
                    return;
                //预约人
                initiateUserId = data.getDetail().getAccount_id();
                //被预约人
                acceptUserId = data.getDetail().getAccount_vipid();
                //是否是红人
                boolean isReadman;

                if (data.getDetail() != null) {
                    mMvpView.setPayPrice(data.getDetail().getPaycoins());
                    String startTime = TimeUtils.longToStr(data.getDetail().getBegintime() * 1000, "yyyy.MM.dd");
                    String endTime = TimeUtils.longToStr(data.getDetail().getEndtime() * 1000, "yyyy.MM.dd");
                    mMvpView.setOrderTime(startTime, endTime);
                    mMvpView.setOrderStatus(data.getDetail().getAstatus(), data.getDetail().getComplaintflag());
                }

                if (data.getMyifno() != null) {
                    if (data.getMyifno().getIsstarauth() == 1 && data.getMyifno().getStarbutton() == 1)
                        isReadman = true;
                    else
                        isReadman = false;

                    if (data.getMyifno().getUid() == initiateUserId) {
                        mMvpView.setInitiateUserIcon(data.getMyifno().getUserphoto_thumb());
                        mMvpView.setInitiateUserName(data.getMyifno().getNickname());
                        mMvpView.setInitiateUserSex(data.getMyifno().getSex(), isReadman);
                    } else if (data.getMyifno().getUid() == acceptUserId) {
                        mMvpView.setAcceptUserIcon(data.getMyifno().getUserphoto_thumb());
                        mMvpView.setAcceptUserName(data.getMyifno().getNickname());
                        mMvpView.setAcceptUserSex(data.getMyifno().getSex(), isReadman);
                    }
                }
                if (data.getOuinfo() != null) {
                    if (data.getOuinfo().getIsstarauth() == 1 && data.getOuinfo().getStarbutton() == 1)
                        isReadman = true;
                    else
                        isReadman = false;

                    if (data.getOuinfo().getUid() == initiateUserId) {
                        mMvpView.setInitiateUserIcon(data.getOuinfo().getUserphoto_thumb());
                        mMvpView.setInitiateUserName(data.getOuinfo().getNickname());
                        mMvpView.setInitiateUserSex(data.getOuinfo().getSex(), isReadman);
                    } else if (data.getOuinfo().getUid() == acceptUserId) {
                        mMvpView.setAcceptUserIcon(data.getOuinfo().getUserphoto_thumb());
                        mMvpView.setAcceptUserName(data.getOuinfo().getNickname());
                        mMvpView.setAcceptUserSex(data.getOuinfo().getSex(), isReadman);
                    }
                }
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getAppointmentDetailFail(msg);
            }
        });
    }

    @Override
    public int getInitiateUserId() {
        return initiateUserId;
    }

    @Override
    public int getAcceptUserId() {
        return acceptUserId;
    }
}
