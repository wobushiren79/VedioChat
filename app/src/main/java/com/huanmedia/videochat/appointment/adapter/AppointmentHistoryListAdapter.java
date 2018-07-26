package com.huanmedia.videochat.appointment.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.appointment.AppointmentHistoryListActivity;
import com.huanmedia.videochat.chat.bean.ChatIntentBean;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.BusinessCardDialog;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataOpResults;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentConfirmPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentConfirmPresenter;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentConfirmView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;

import mvp.data.store.glide.GlideUtils;

public class AppointmentHistoryListAdapter extends BaseRCAdapter<AppointmentDataOpResults> implements IAppointmentConfirmView {
    private IAppointmentConfirmPresenter mConfirmPresenter;
    private RecyclerView.LayoutManager mLayoutManager;

    public AppointmentHistoryListAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context, R.layout.item_appointment_history_list);
        mConfirmPresenter = new AppointmentConfirmPresenterImpl(this);
        mLayoutManager = layoutManager;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, AppointmentDataOpResults itemData, int position) {
        RoundedImageView ivHead = baseViewHolder.getView(R.id.iv_heard);
        ImageView ivSex = baseViewHolder.getView(R.id.iv_sex);
        TextView tvTitle = baseViewHolder.getView(R.id.tv_title);
        TextView tvTime = baseViewHolder.getView(R.id.tv_time);
        TextView tvCallTime = baseViewHolder.getView(R.id.tv_call_time);
        TextView tvPrice = baseViewHolder.getView(R.id.tv_price);

        TextView tvStatus = baseViewHolder.getView(R.id.tv_status);
        TextView tvStatus2 = baseViewHolder.getView(R.id.tv_status_2);
        TextView tvConfirm = baseViewHolder.getView(R.id.tv_take_order);
        LinearLayout llMore = baseViewHolder.getView(R.id.ll_more);

        if (itemData == null || itemData.getUinfo() == null || itemData.getAppinfo() == null || itemData.getMsg() == null)
            return;
        int orderStatus = itemData.getAppinfo().getStatus();

        //设置头像
        GlideUtils.getInstance().loadBitmapNoAnim(getContext(), itemData.getUinfo().getUserphoto_thumb(), ivHead);
        //设置性别相关
        if (itemData.getUinfo().getSex() == 1) {
            ivSex.setImageResource(R.drawable.icon_focus_boy);
        } else {
            ivSex.setImageResource(R.drawable.icon_focus_girl);
        }
        //设置金额
        tvPrice.setText("金额" + itemData.getAppinfo().getPaycoins() + "钻石");
        //设置通话时长
        tvCallTime.setText(itemData.getAppinfo().getYtime() + "分钟");
        //设置时间
        tvTime.setText("下单时间 " + TimeUtils.millis2String(itemData.getAppinfo().getCtime() * 1000, new SimpleDateFormat("MM-dd HH:mm")));

        tvStatus.setVisibility(View.GONE);
        tvConfirm.setVisibility(View.GONE);
        llMore.setVisibility(View.GONE);
        //标题设置
        if (itemData.getAppinfo().getFrom() == UserManager.getInstance().getId()) {
            tvTitle.setText("下单：");
            //点击
            llMore.setOnClickListener(view -> {
                ((BaseActivity) getContext()).getNavigator().navtoAppointment((Activity) getContext(), itemData.getAppinfo().getTo());
            });
            switch (orderStatus) {
                case AppointmentDataOpResults.OrderStatus.NoConfirm:
                case AppointmentDataOpResults.OrderStatus.ReadManConfirm:
                    tvStatus.setVisibility(View.VISIBLE);
                    break;
                case AppointmentDataOpResults.OrderStatus.Complete:
                case AppointmentDataOpResults.OrderStatus.ReadManCancel:
                case AppointmentDataOpResults.OrderStatus.SelfCancel:
                case AppointmentDataOpResults.OrderStatus.OverTime:
                    llMore.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        } else {
            //点击
            tvTitle.setText("接单：");
            llMore.setOnClickListener(null);
            switch (orderStatus) {
                case AppointmentDataOpResults.OrderStatus.NoConfirm:
                    tvConfirm.setVisibility(View.VISIBLE);
                    break;
                default:
                    tvStatus.setVisibility(View.VISIBLE);
                    break;
            }
        }

        //状态相关设置
        switch (orderStatus) {
            case AppointmentDataOpResults.OrderStatus.NoConfirm:
                tvTitle.append("订单待确认~");
                break;
            case AppointmentDataOpResults.OrderStatus.ReadManConfirm:
                tvTitle.append("订单执行中~");
                break;
            case AppointmentDataOpResults.OrderStatus.Complete:
                tvTitle.append("订单已结束，已收到" + itemData.getAppinfo().getPaycoins() + "钻~");
                break;
            case AppointmentDataOpResults.OrderStatus.ReadManCancel:
            case AppointmentDataOpResults.OrderStatus.SelfCancel:
                tvTitle.append("订单已被取消~");
                break;
            case AppointmentDataOpResults.OrderStatus.OverTime:
                tvTitle.append("订单已超时~");
                break;
            default:
                tvTitle.append("状态未知~");
                break;
        }
        if (itemData.getAppinfo().getComplaintflag() == 0) {
            tvStatus.setText(itemData.getAppinfo().getStatusStr());
            tvStatus2.setText(itemData.getAppinfo().getStatusStr());
        } else {
            tvStatus.setText("申诉中");
            tvStatus2.setText("申诉中");
        }
        //确认订单
        tvConfirm.setOnClickListener(view -> {
            mConfirmPresenter.confirmAppointmentOp(itemData.getAppinfo().getAid());
        });
        //详情
        baseViewHolder.itemView.setOnClickListener(view -> {
            ChatIntentBean intentBean = new ChatIntentBean();
            intentBean.setChatType(ChatIntentBean.ChatType.Appointment);
            intentBean.setOrderId(itemData.getAppinfo().getAid());
            intentBean.setChatUserId(itemData.getAppinfo().getTo());
            ((BaseActivity) mContext).getNavigator().navtoChat((Activity) mContext, intentBean);
        });
        //头像点击
        ivHead.setOnClickListener(view -> {
            if (itemData.getUinfo().getIsstart() == 1) {
                ((BaseActivity) getContext()).getNavigator().navDiscoverInfo
                        ((Activity) getContext(), itemData.getUinfo().getUid(), "0", BusinessCardFragment.ShowType.ReadMan);
            } else {
                BusinessCardDialog dialog = new BusinessCardDialog(getContext());
                dialog.setUid(itemData.getUinfo().getUid());
                dialog.setDistance("0");
                dialog.show();
            }
        });
    }

    //------------------接单拒单-------------------------
    @Override
    public void confirmAppointmentSuccess(int aid) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getAppinfo().getAid() == aid) {
                mDatas.get(i).getAppinfo().setStatus(1);
                TextView tvConfirm = mLayoutManager.findViewByPosition(i).findViewById(R.id.tv_take_order);
                startConfirmAnim(i, tvConfirm);
                break;
            }
        }
    }

    @Override
    public void confirmAppointmentFail(String msg) {
        showToast("提交失败：" + msg);
    }

    @Override
    public void cancelAppointmentSuccess(int aid) {

    }

    @Override
    public void cancelAppointmentFail(String msg) {

    }

    @Override
    public void completeAppointmentSuccess(int aid) {

    }

    @Override
    public void completeAppointmentFail(String msg) {

    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }


    /**
     * 接单成功动画
     *
     * @param position
     * @param view
     */
    private void startConfirmAnim(int position, View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (1, 0, 1, 0,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                AppointmentHistoryListAdapter.this.notifyItemChanged(position);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(scaleAnimation);
    }
}
