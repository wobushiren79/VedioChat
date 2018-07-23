package com.huanmedia.videochat.chat.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseLinearLayout;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.StatusChangeLayout;
import com.huanmedia.videochat.common.widget.dialog.CheckContactDialog;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataOpResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDetailResults;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentConfirmPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentDetailPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentConfirmPresenter;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentDetailPresenter;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentConfirmView;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentDetailView;

import java.util.Map;

import mvp.data.store.glide.GlideUtils;

public class AppointmentChatLayout extends BaseLinearLayout implements IAppointmentDetailView, IAppointmentConfirmView, View.OnClickListener {
    private ImageView mIVSex;
    private ImageView mIVHeard;
    private ImageView mIVFlag;
    private TextView mTVName;
    private TextView mTVPay;
    private TextView mTVOrderTime;
    private TextView mTVCommunication;
    private TextView mTVCommunicationTitle;

    private TextView mTVBackMoney;
    private TextView mTVCancelOrder;
    private TextView mTVConfirmOrder;
    private TextView mTVCompleteOrder;
    private StatusChangeLayout mStatusLayout;

    private int mOrderId;
    private IAppointmentDetailPresenter mDetailPresenter;
    private IAppointmentConfirmPresenter mConfirmPresenter;
    private CallBack mCallBack;

    public AppointmentChatLayout(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_chat_appointment;
    }

    @Override
    protected void initView(View baseView) {
        mIVSex = getView(R.id.iv_sex);
        mTVName = getView(R.id.tv_name);
        mIVHeard = getView(R.id.iv_heard);
        mIVFlag = getView(R.id.iv_flag);
        mTVPay = getView(R.id.tv_pay);
        mTVOrderTime = getView(R.id.tv_order_time);
        mStatusLayout = getView(R.id.status_change_layout);
        mTVCommunicationTitle = getView(R.id.tv_communication_title);
        mTVCommunication = getView(R.id.tv_communication_click);

        mTVBackMoney = getView(R.id.tv_back_money);
        mTVCancelOrder = getView(R.id.tv_cancel_order);
        mTVConfirmOrder = getView(R.id.tv_confirm_order);
        mTVCompleteOrder = getView(R.id.tv_complete_order);

        mTVCommunication.setOnClickListener(this);
        mTVBackMoney.setOnClickListener(this);
        mTVCancelOrder.setOnClickListener(this);
        mTVConfirmOrder.setOnClickListener(this);
        mTVCompleteOrder.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mDetailPresenter = new AppointmentDetailPresenterImpl(this);
        mConfirmPresenter = new AppointmentConfirmPresenterImpl(this);
    }

    public void setCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void setOrderId(int orderId) {
        mOrderId = orderId;
        mDetailPresenter.getAppointmentDetails();
    }


    //------------------订单详情相关-------------------------
    @Override
    public void getAppointmentDetailSuccess(AppointmentDetailResults results) {
        if (mCallBack != null)
            mCallBack.getDetailsSuccess(results);
    }

    @Override
    public void getAppointmentDetailFail(String msg) {
        if (mCallBack != null)
            mCallBack.getDetailsFail(msg);
        showToast(msg);
    }

    @Override
    public int getOrderId() {
        return mOrderId;
    }

    @Override
    public void setInitiateUserName(String name) {
        if (mDetailPresenter.getInitiateUserId() != UserManager.getInstance().getId())
            setName(name);
    }

    @Override
    public void setInitiateUserIcon(String url) {
        if (mDetailPresenter.getInitiateUserId() != UserManager.getInstance().getId())
            setIcon(url);
    }


    @Override
    public void setInitiateUserSex(int sex, boolean isReadMan) {
        if (mDetailPresenter.getInitiateUserId() != UserManager.getInstance().getId())
            setSex(sex, isReadMan);
    }

    @Override
    public void setAcceptUserName(String name) {
        if (mDetailPresenter.getAcceptUserId() != UserManager.getInstance().getId())
            setName(name);
    }

    @Override
    public void setAcceptUserIcon(String url) {
        if (mDetailPresenter.getAcceptUserId() != UserManager.getInstance().getId())
            setIcon(url);
    }

    @Override
    public void setAcceptUserSex(int sex, boolean isReadMan) {
        if (mDetailPresenter.getAcceptUserId() != UserManager.getInstance().getId())
            setSex(sex, isReadMan);
    }

    @Override
    public void setPayPrice(int coin) {
        mTVPay.setText(coin + "钻");
    }

    @Override
    public void setOrderTime(String startTime, String endTime) {
        mTVOrderTime.setText(startTime + "-" + endTime);
    }

    @Override
    public void setOrderStatus(int status) {
        Map<Integer, String> statusMap = new LinkedTreeMap<>();
        statusMap.put(0, "待确认");
        statusMap.put(1, "进行中");
        statusMap.put(2, "已完成");
        mStatusLayout.setStatusMap(statusMap);
        mStatusLayout.setStatus(status);

        switch (status) {
            case AppointmentDataOpResults.OrderStatus.NoConfirm:
                mTVBackMoney.setVisibility(GONE);
                mTVCompleteOrder.setVisibility(GONE);
                if (mDetailPresenter.getAcceptUserId() == UserManager.getInstance().getId()) {
                    mTVCancelOrder.setVisibility(VISIBLE);
                    mTVConfirmOrder.setVisibility(VISIBLE);
                } else {
                    mTVCancelOrder.setVisibility(GONE);
                    mTVConfirmOrder.setVisibility(GONE);
                }
                break;
            case AppointmentDataOpResults.OrderStatus.ReadManConfirm:
                mTVCancelOrder.setVisibility(GONE);
                mTVConfirmOrder.setVisibility(GONE);
                if (mDetailPresenter.getAcceptUserId() == UserManager.getInstance().getId()) {
                    mTVBackMoney.setVisibility(GONE);
                    mTVCompleteOrder.setVisibility(GONE);
                } else {
                    mTVBackMoney.setVisibility(VISIBLE);
                    mTVCompleteOrder.setVisibility(VISIBLE);
                }
                break;
            case AppointmentDataOpResults.OrderStatus.Complete:
                mTVCancelOrder.setVisibility(GONE);
                mTVConfirmOrder.setVisibility(GONE);
                mTVCompleteOrder.setVisibility(GONE);
                if (mDetailPresenter.getAcceptUserId() == UserManager.getInstance().getId()) {
                    mTVBackMoney.setVisibility(GONE);
                } else {
                    mTVBackMoney.setVisibility(VISIBLE);
                }
                break;
            default:
                mTVCancelOrder.setVisibility(GONE);
                mTVConfirmOrder.setVisibility(GONE);
                mTVCompleteOrder.setVisibility(GONE);
                mTVBackMoney.setVisibility(GONE);
                mTVBackMoney.setVisibility(GONE);
                mStatusLayout.setVisibility(GONE);
                break;
        }
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }

    //-------------私有方法------------------


    /**
     * 设置性别相关
     *
     * @param sex
     */
    private void setSex(int sex, boolean isReadMan) {
        if (mIVSex != null && mIVFlag != null)
            if (sex == 1) {
                mIVSex.setImageResource(R.drawable.icon_focus_boy);
                if (isReadMan) {
                    mIVFlag.setVisibility(VISIBLE);
                    mIVFlag.setImageResource(R.drawable.icon_pendant_boy);
                } else {
                    mIVFlag.setVisibility(GONE);
                    mTVCommunicationTitle.setVisibility(GONE);
                    mTVCommunication.setVisibility(GONE);
                    mIVFlag.setImageResource(R.drawable.icon_pendant_boy);
                }
            } else {
                mIVSex.setImageResource(R.drawable.icon_focus_girl);
                if (isReadMan) {
                    mIVFlag.setVisibility(VISIBLE);
                    mIVFlag.setImageResource(R.drawable.icon_pendant_girl);
                } else {
                    mIVFlag.setVisibility(GONE);
                    mTVCommunicationTitle.setVisibility(GONE);
                    mTVCommunication.setVisibility(GONE);
                    mIVFlag.setImageResource(R.drawable.icon_pendant_girl);
                }
            }
    }

    /**
     * 设置姓名相关
     *
     * @param name
     */
    private void setName(String name) {
        if (mTVName != null)
            mTVName.setText(name);
    }

    /**
     * 设置头像
     */
    private void setIcon(String url) {
        if (mIVHeard != null)
            GlideUtils.getInstance().loadBitmapNoAnim(getContext(), url, mIVHeard);
    }

    /**
     * 通讯点击处理
     */
    private void communicationClick() {
        CheckContactDialog dialog = new CheckContactDialog(getContext());
        dialog.setReadManId(mDetailPresenter.getAcceptUserId());
        dialog.show();
    }

    /**
     * 取消订单点击处理
     */
    private void cancelClick() {
        GeneralDialog cancelDialog = new GeneralDialog(getContext());
        cancelDialog.setContent("是否取消订单？");
        cancelDialog.setCallBack(new GeneralDialog.CallBack() {
            @Override
            public void submitClick(Dialog dialog) {
                mConfirmPresenter.cancelAppointmentOp(mOrderId);
            }

            @Override
            public void cancelClick(Dialog dialog) {

            }
        });
        cancelDialog.show();
    }

    /**
     * 确认订单点击处理
     */
    private void confirmClick() {
        GeneralDialog cancelDialog = new GeneralDialog(getContext());
        cancelDialog.setContent("是否确认订单？");
        cancelDialog.setCallBack(new GeneralDialog.CallBack() {
            @Override
            public void submitClick(Dialog dialog) {
                mConfirmPresenter.confirmAppointmentOp(mOrderId);
            }

            @Override
            public void cancelClick(Dialog dialog) {

            }
        });
        cancelDialog.show();
    }

    /**
     * 完成订单点击处理
     */
    private void completeClick() {
        GeneralDialog cancelDialog = new GeneralDialog(getContext());
        cancelDialog.setContent("是否完成订单？");
        cancelDialog.setCallBack(new GeneralDialog.CallBack() {
            @Override
            public void submitClick(Dialog dialog) {
                mConfirmPresenter.completeAppointment(mOrderId);
            }

            @Override
            public void cancelClick(Dialog dialog) {

            }
        });
        cancelDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_communication_click:
                communicationClick();
                break;
            case R.id.tv_back_money:
                break;
            case R.id.tv_cancel_order:
                cancelClick();
                break;
            case R.id.tv_confirm_order:
                confirmClick();
                break;
            case R.id.tv_complete_order:
                completeClick();
                break;
        }
    }

    //--------------确认，取消，完成订单--------------
    @Override
    public void confirmAppointmentSuccess(int aid) {
        setOrderStatus(AppointmentDataOpResults.OrderStatus.ReadManConfirm);
    }

    @Override
    public void confirmAppointmentFail(String msg) {
        showToast(msg);
    }

    @Override
    public void cancelAppointmentSuccess(int aid) {
        setOrderStatus(AppointmentDataOpResults.OrderStatus.ReadManCancel);
    }

    @Override
    public void cancelAppointmentFail(String msg) {
        showToast(msg);
    }

    @Override
    public void completeAppointmentSuccess(int aid) {
        setOrderStatus(AppointmentDataOpResults.OrderStatus.Complete);
    }

    @Override
    public void completeAppointmentFail(String msg) {
        showToast(msg);
    }

    public interface CallBack {
        void getDetailsSuccess(AppointmentDetailResults results);

        void getDetailsFail(String msg);
    }
}
