package com.huanmedia.videochat.mvp.presenter.appointment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.huanmedia.videochat.appointment.AppointmentActivity;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;
import com.huanmedia.videochat.mvp.model.appointment.AppointmentSubmitModelImpl;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentSubmitView;

import static com.huanmedia.videochat.common.utils.VideoChatUtils.NoMoreMoneyDialog;

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
        params.setTime(mMvpView.getAppointmentTime() + ":00");
        mMvpModel.submit(mMvpView.getContext(), params, new DataCallBack<AppointmentUserInfoResults>() {
            @Override
            public void getDataSuccess(AppointmentUserInfoResults data) {
                mMvpView.submitAppointmentSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.submitAppointmentFail(msg);
            }
        });
    }

    @Override
    public void submitAppointmentOp() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getAppointmentUid() == 0) {
            mMvpView.showToast("没有被预约者ID");
            return;
        }
        if (mMvpView.getAppointmentMsg() == null || mMvpView.getAppointmentMsg().length() == 0) {
            mMvpView.showToast("没有留言");
            return;
        }
        if (mMvpView.getAppointmentCallTime() == 0) {
            mMvpView.showToast("没有预约时长");
            return;
        }
        if (mMvpView.getAppointmentPrice() == 0) {
            mMvpView.showToast("没有预约价格");
            return;
        }
        int userCoin = UserManager.getInstance().getCurrentUser().getUserinfo().getCoin();
        if (mMvpView.getAppointmentPrice() > userCoin) {
            errorNoMoney();
            return;
        }
        if (mMvpView.getAppointmentCallTime() < mMvpView.getMinCallTime()) {
            mMvpView.showToast("预约时长必须大于" + mMvpView.getMinCallTime() + "分钟");
            return;
        }
        AppointmentRequest params = new AppointmentRequest();
        params.setCoins(mMvpView.getAppointmentPrice());
        params.setUid(mMvpView.getAppointmentUid());
        params.setMsg(mMvpView.getAppointmentMsg());
        params.setTimes(mMvpView.getAppointmentCallTime());

        mMvpModel.submitOp(mMvpView.getContext(), params, new DataCallBack<AppointmentUserInfoResults>() {
            @Override
            public void getDataSuccess(AppointmentUserInfoResults data) {
                UserManager.getInstance().getCurrentUser().getUserinfo().setCoin(data.getCoin());
                mMvpView.submitAppointmentSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                if (msg == null)
                    return;
                if (msg.contains("砖石不足"))
                    errorNoMoney();
                else
                    mMvpView.submitAppointmentFail(msg);
            }
        });
    }

    /**
     * 用户金币不足处理
     */
    private void errorNoMoney() {
        NoMoreMoneyDialog(mMvpView.getContext(), "抱歉，无法完成预约，没有更多钻石了。");
    }
}
