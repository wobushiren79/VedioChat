package com.huanmedia.videochat.appointment.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.constants.TimeConstants;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.appointment.AppointmentListActivity;
import com.huanmedia.videochat.appointment.fragment.AppointmentListFragment;
import com.huanmedia.videochat.common.widget.dialog.BusinessCardDialog;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListResults;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentConfirmPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentConfirmPresenter;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentConfirmView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;

import kotlin.Unit;
import mvp.data.store.glide.GlideApp;

public class AppointmentListPtrAdapter extends BaseRCAdapter<AppointmentListResults.Item> implements IAppointmentConfirmView {

    private @AppointmentListFragment.UserType
    int mUserType = 0;

    private IAppointmentConfirmPresenter mAppointmentConfirmPresenter;

    public AppointmentListPtrAdapter(Context context, @AppointmentListFragment.UserType int mUserType) {
        super(context, R.layout.item_appointment_style_2);
        mAppointmentConfirmPresenter = new AppointmentConfirmPresenterImpl(this);
        this.mUserType = mUserType;
    }


    @Override
    public void convert(BaseViewHolder baseViewHolder, AppointmentListResults.Item itemData, int position) {
        RoundedImageView ivIcon = baseViewHolder.getView(R.id.iv_icon);
        ImageView ivSex = baseViewHolder.getView(R.id.iv_sex);
        TextView tvName = baseViewHolder.getView(R.id.tv_name);
        TextView tvTime = baseViewHolder.getView(R.id.tv_time);
        TextView tvStatus = baseViewHolder.getView(R.id.tv_status);
        TextView tvSubmit = baseViewHolder.getView(R.id.tv_submit);
        TextView tvCancel = baseViewHolder.getView(R.id.tv_cancel);
        LinearLayout llSubmitLayout = baseViewHolder.getView(R.id.ll_submit_layout);

        int sexPicId;
        if (itemData.getSex() == 1) {
            sexPicId = R.drawable.icon_focus_boy;
        } else {
            sexPicId = R.drawable.icon_focus_girl;
        }
        GlideApp.with(mContext).asBitmap().load(itemData.getUserphoto_thumb()).into(ivIcon);
        GlideApp.with(mContext).asBitmap().load(sexPicId).into(ivSex);

        tvName.setText(itemData.getNickname());
        tvTime.setText(TimeUtils.millis2String(itemData.getDatetime() * 1000, new SimpleDateFormat("yyyy-MM-dd HH:mm")));

        tvStatus.setVisibility(View.GONE);
        tvStatus.setTextColor(mContext.getResources().getColor(R.color.base_gray));
        llSubmitLayout.setVisibility(View.GONE);
        if (itemData.getStatus() == 0) {
            //状态为未处理
            if (mUserType == 0) {
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_f65aa0));
                tvStatus.setText("待确认");
            } else {
                llSubmitLayout.setVisibility(View.VISIBLE);
                tvSubmit.setOnClickListener(view -> {
                    GeneralDialog dialog = new GeneralDialog(getContext());
                    dialog
                            .setContent("是否确认该预约信息")
                            .setCallBack(new GeneralDialog.CallBack() {
                                @Override
                                public void submitClick(Dialog dialog) {
                                    mAppointmentConfirmPresenter.confirmAppointment(itemData.getAid());
                                }

                                @Override
                                public void cancelClick(Dialog dialog) {

                                }
                            })
                            .show();
                });
                tvCancel.setOnClickListener(view -> {
                    GeneralDialog dialog = new GeneralDialog(getContext());
                    dialog
                            .setContent("是否取消该预约信息")
                            .setCallBack(new GeneralDialog.CallBack() {
                                @Override
                                public void submitClick(Dialog dialog) {
                                    mAppointmentConfirmPresenter.cancelAppointment(itemData.getAid());
                                }

                                @Override
                                public void cancelClick(Dialog dialog) {

                                }
                            })
                            .show();
                });
            }
        } else if (itemData.getStatus() == 1) {
            //确认
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_f65aa0));
            if (mUserType == 0) {
                tvStatus.setText("预约成功");
            } else {
                tvStatus.setText("已确认");
            }
        } else if (itemData.getStatus() == -1) {
            //红人取消
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.base_gray));
            if (mUserType == 0) {
                tvStatus.setText("对方已取消");
            } else {
                tvStatus.setText("已取消");
            }
        } else if (itemData.getStatus() == -2) {
            //自己取消
        } else if (itemData.getStatus() == -3) {
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.base_gray));
            tvStatus.setText("预约单超时");
        } else {
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.base_gray));
            tvStatus.setText("未处理");
        }

        //个人资料
        baseViewHolder.itemView.setOnClickListener(view -> {
            if (itemData.getIsstart() == 1) {
                ((AppointmentListActivity) getContext()).getNavigator().navDiscoverInfo
                        ((Activity) getContext(), itemData.getUid(), null, BusinessCardFragment.ShowType.ReadMan);
            } else {
                BusinessCardDialog dialog = new BusinessCardDialog(getContext());
                dialog.setUid(itemData.getUid());
                dialog.show();
            }

        });
    }

    @Override
    public void confirmAppointmentSuccess(int aid) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getAid() == aid) {
                mDatas.get(i).setStatus(1);
                this.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void confirmAppointmentFail(String msg) {
        showToast(msg);
    }

    @Override
    public void cancelAppointmentSuccess(int aid) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getAid() == aid) {
                mDatas.get(i).setStatus(-1);
                this.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void cancelAppointmentFail(String msg) {
        showToast(msg);
    }

    @Override
    public void completeAppointmentSuccess(int aid) {

    }

    @Override
    public void completeAppointmentFail(String msg) {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShort(getContext(), toast);
    }
}
