package com.huanmedia.videochat.common.utils;

import android.app.Activity;
import android.app.Dialog;

import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.widget.dialog.CommDialogUtils;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.weight.MaskDialog;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;

public class VideoChatUtils {

    /**
     * 发起视频的检测
     *
     * @param activity
     * @param navigator
     * @param isReadMan    是否是红人
     * @param starcoin     计算价格
     * @param startime     计算价格单位时间
     * @param onlinestatus 在线状态
     * @param userId       用户ID
     */
    public static void CheckCallVideo(Activity activity, Navigator navigator, boolean isReadMan, int starcoin, int startime, int onlinestatus, int userId) {
        if (userId == UserManager.getInstance().getCurrentUser().getId()) {
            ToastUtils.showToastShortInCenter(activity, "亲，不能自己跟自己视频哟");
            return;
        }
        if (UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() < starcoin) {
            CommDialogUtils.showInsufficientBalance(activity, new GeneralDialog.CallBack() {
                @Override
                public void submitClick(Dialog dialog) {
                    navigator.navtoCoinPay(activity, null);
                }

                @Override
                public void cancelClick(Dialog dialog) {

                }
            });
        } else if (onlinestatus == 2) {
            ToastUtils.showToastShortInCenter(activity, "TA正在连线哟~");
        } else if (onlinestatus == 0) {
            ToastUtils.showToastShortInCenter(activity, "TA正在休息哦~");
        } else if ((!isReadMan && UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() >= 20) ||//如果是视频直聊需要20钻石 固定
                UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() >= starcoin) {
            GeneralDialog dialog = new GeneralDialog(activity);
            dialog
                    .setContent("视频聊天需花费 " + String.format("%d钻/分钟", starcoin))
                    .setCallBack(new GeneralDialog.CallBack() {
                        @Override
                        public void submitClick(Dialog dialog) {
                            if (isReadMan) {
                                ConditionEntity condition = new ConditionEntity();
                                condition.setVideoType(ConditionEntity.VideoType.REDMAN);
                                condition.getReadMainConfig().setRedManId(userId);
                                condition.getReadMainConfig().setRequestType(ConditionEntity.RequestType.SELF);
                                condition.getReadMainConfig().setRedMainStartCoin(starcoin);
                                condition.getReadMainConfig().setReadMainStartTime(startime);
                                navigator.navtoCalling(activity, condition, "连接中...; ");
                            } else {
                                ConditionEntity condition = new ConditionEntity();
                                condition.setVideoType(ConditionEntity.VideoType.MATCH);
                                condition.getMatchConfig().setRequestType(ConditionEntity.RequestType.SELF);
                                condition.getMatchConfig().setMask(MaskDialog.getCurrentMask());
                                condition.getMatchConfig().setUid(userId);
                                navigator.navtoCalling(activity, condition, "连接中...; ");
                            }
                        }

                        @Override
                        public void cancelClick(Dialog dialog) {

                        }
                    })
                    .show();
        } else {
            CommDialogUtils.showInsufficientBalance(activity, new GeneralDialog.CallBack() {
                @Override
                public void submitClick(Dialog dialog) {
                    navigator.navtoCoinPay(activity, null);
                }

                @Override
                public void cancelClick(Dialog dialog) {

                }
            });

        }
    }

    public static void StartAppointmentCall(BaseActivity activity, int initiateUserId, int acceptUserId, int appointmentId, int touid, int fromuid) {
        ConditionEntity condition = new ConditionEntity();
        condition.setVideoType(ConditionEntity.VideoType.APPOINTMENT);
        condition.setRequestType(ConditionEntity.RequestType.SELF);
        condition.getAppointmentConfig().setInitiateUserId(initiateUserId);
        condition.getAppointmentConfig().setAcceptUserID(acceptUserId);
        condition.getAppointmentConfig().setOrderId(appointmentId);
        VideoChatEntity videoChatEntity = new VideoChatEntity();
        videoChatEntity.setTouid(touid);
        videoChatEntity.setFromuid(fromuid);
        condition.getAppointmentConfig().setVideoChatConfig(videoChatEntity);
        activity.getNavigator().navtoCalling(activity, condition, "连接中...; ");
    }
}
