package com.huanmedia.videochat.appointment.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseRCMultiAdatper;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.chat.ChatActivity;
import com.huanmedia.videochat.chat.bean.ChatIntentBean;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataOpResults;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentConfirmPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentConfirmPresenter;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentConfirmView;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class AppointmentListOpAdapter
        extends BaseRCMultiAdatper<AppointmentDataOpResults>
        implements IAppointmentConfirmView {

    private final int ReadManType = 1;
    private final int UserType = 2;
    private final int TimeType = 3;
    private IAppointmentConfirmPresenter mConfirmPresenter;
    private RecyclerView.LayoutManager mLayoutManager;

    public AppointmentListOpAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context);
        mConfirmPresenter = new AppointmentConfirmPresenterImpl(this);
        mLayoutManager = layoutManager;
    }


    @Override
    public void convert(BaseViewHolder baseViewHolder, AppointmentDataOpResults appointmentDataOpResults, int position, int viewType) {
        if (viewType == ReadManType) {
            setReadManItem(baseViewHolder, appointmentDataOpResults);
        } else if (viewType == UserType) {
            setUserItem(baseViewHolder, appointmentDataOpResults);
        } else if (viewType == TimeType) {

        }
    }

    /**
     * 设置红人列表
     *
     * @param baseViewHolder
     * @param itemData
     */
    private void setReadManItem(BaseViewHolder baseViewHolder, AppointmentDataOpResults itemData) {
        RoundedImageView ivHead = baseViewHolder.getView(R.id.iv_heard);
        ImageView ivSex = baseViewHolder.getView(R.id.iv_sex);
        TextView tvTime = baseViewHolder.getView(R.id.tv_time);
        TextView tvCallTime = baseViewHolder.getView(R.id.tv_call_time);
        TextView tvPrice = baseViewHolder.getView(R.id.tv_price);

        TextView tvStatus = baseViewHolder.getView(R.id.tv_status);
        TextView tvConfirm = baseViewHolder.getView(R.id.tv_take_order);
        if (itemData == null || itemData.getUinfo() == null || itemData.getAppinfo() == null || itemData.getMsg() == null)
            return;
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
        //设置状态相关
        if(itemData.getAppinfo().getComplaintflag()==0){
            tvStatus.setText(itemData.getAppinfo().getStatusStr());
        }else{
            tvStatus.setText("申诉中");
        }

        if (itemData.getAppinfo().getStatus() == 0) {
            tvStatus.setVisibility(View.GONE);
            tvConfirm.setVisibility(View.VISIBLE);
        } else {
            tvStatus.setVisibility(View.VISIBLE);
            tvConfirm.setVisibility(View.GONE);
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
            intentBean.setChatUserId(itemData.getAppinfo().getFrom());
            ((BaseActivity) mContext).getNavigator().navtoChat((Activity) mContext, intentBean);
        });
    }

    /**
     * 设置用户列表
     *
     * @param baseViewHolder
     * @param itemData
     */
    private void setUserItem(BaseViewHolder baseViewHolder, AppointmentDataOpResults itemData) {
        RoundedImageView ivHead = baseViewHolder.getView(R.id.iv_heard);
        ImageView ivFlag = baseViewHolder.getView(R.id.iv_flag);
        ImageView ivSex = baseViewHolder.getView(R.id.iv_sex);
        TextView tvName = baseViewHolder.getView(R.id.tv_name);
        TextView tvStatus = baseViewHolder.getView(R.id.tv_status);
        TextView tvTime = baseViewHolder.getView(R.id.tv_time);
        TextView tvMsg = baseViewHolder.getView(R.id.tv_msg);
        TextView tvMsgNumber = baseViewHolder.getView(R.id.tv_msg_number);
        if (itemData == null || itemData.getUinfo() == null || itemData.getAppinfo() == null || itemData.getMsg() == null)
            return;
        //设置头像
        GlideUtils.getInstance().loadBitmapNoAnim(getContext(), itemData.getUinfo().getUserphoto_thumb(), ivHead);
        //设置性别相关
        if (itemData.getUinfo().getSex() == 1) {
            ivSex.setImageResource(R.drawable.icon_focus_boy);
            ivFlag.setImageResource(R.drawable.icon_pendant_boy);
        } else {
            ivSex.setImageResource(R.drawable.icon_focus_girl);
            ivFlag.setImageResource(R.drawable.icon_pendant_girl);
        }
        //设置姓名
        tvName.setText(itemData.getUinfo().getNickname());
        //设置状态相关
        if (itemData.getAppinfo().getStatus() == 0) {
            tvStatus.setBackgroundResource(R.drawable.base_bg_round_theme_2);
        } else {
            tvStatus.setBackgroundResource(R.drawable.base_bg_round_theme);
        }
        if(itemData.getAppinfo().getComplaintflag()==0){
            tvStatus.setText(itemData.getAppinfo().getStatusStr());
        }else{
            tvStatus.setText("申诉中");
        }
        //设置时间
        tvTime.setText(TimeUtils.millis2String(itemData.getAppinfo().getCtime() * 1000, new SimpleDateFormat("MM-dd")));
        //详情
        baseViewHolder.itemView.setOnClickListener(view -> {
            ChatIntentBean intentBean = new ChatIntentBean();
            intentBean.setChatType(ChatIntentBean.ChatType.Appointment);
            intentBean.setOrderId(itemData.getAppinfo().getAid());
            intentBean.setChatUserId(itemData.getAppinfo().getTo());
            ((BaseActivity) mContext).getNavigator().navtoChat((Activity) mContext, intentBean);
        });
        //设置聊天信息
        if (itemData.getMsg().getLast() != null && itemData.getMsg().getLast().getMsg() != null)
            tvMsg.setText(itemData.getMsg().getLast().getMsg());
        else
            tvMsg.setText("");
        if (itemData.getMsg().getNoreadcount() != 0) {
            tvMsgNumber.setVisibility(View.VISIBLE);
            tvMsgNumber.setText(itemData.getMsg().getNoreadcount() + "");
        } else {
            tvMsgNumber.setVisibility(View.GONE);
            tvMsgNumber.setText("");
        }
    }


    @Override
    public Map<Integer, Integer> setLayoutMap() {
        Map<Integer, Integer> layoutMaps = new HashMap<>();
        layoutMaps.put(ReadManType, R.layout.item_appointment_list_op_readman);
        layoutMaps.put(UserType, R.layout.item_appointment_list_op_user);
        layoutMaps.put(TimeType, R.layout.item_appointment_list_op_time);
        return layoutMaps;
    }

    @Override
    public int setItemType(int position) {
        AppointmentDataOpResults itemData = mDatas.get(position);
        if (itemData.getAppinfo().getFrom() == 0)
            return TimeType;
        if (itemData.getAppinfo().getFrom() == UserManager.getInstance().getId()) {
            return UserType;
        } else {
            return ReadManType;
        }
    }

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
                AppointmentListOpAdapter.this.notifyItemChanged(position);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(scaleAnimation);
    }
}
