/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huanmedia.videochat.common.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.huanmedia.videochat.appointment.AppointmentActivity;
import com.huanmedia.videochat.appointment.AppointmentListActivity;
import com.huanmedia.videochat.appointment.AppointmentOpActivity;
import com.huanmedia.videochat.common.LocalHtmlWebActivity;
import com.huanmedia.videochat.discover.ArtistActivity;
import com.huanmedia.videochat.discover.BusinessCardAcitivty;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.main.NotificationMessageActivity;
import com.huanmedia.videochat.main2.MainActivity;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.media.MediaPlayActivity;
import com.huanmedia.videochat.media.MediaUpLoadActivity;
import com.huanmedia.videochat.my.AboutActivity;
import com.huanmedia.videochat.my.BoundIDCardActivity;
import com.huanmedia.videochat.my.FeedBackActivity;
import com.huanmedia.videochat.my.HelpActivity;
import com.huanmedia.videochat.my.LevelActivity;
import com.huanmedia.videochat.my.MonitorActivity;
import com.huanmedia.videochat.my.PhotosActivity;
import com.huanmedia.videochat.my.ReadMainCertificateActivity;
import com.huanmedia.videochat.my.RedMianAuthActivity;
import com.huanmedia.videochat.my.SettingActivity;
import com.huanmedia.videochat.my.TrustValueActivity;
import com.huanmedia.videochat.my.UserInfoEditActivity;
import com.huanmedia.videochat.pay.AccountBoundActivity;
import com.huanmedia.videochat.pay.AccountParticularsActivity;
import com.huanmedia.videochat.pay.CoinPayActivity;
import com.huanmedia.videochat.pay.ExchangeActivity;
import com.huanmedia.videochat.pay.MyAccountActivity;
import com.huanmedia.videochat.pay.MyWalletActivity;
import com.huanmedia.videochat.repository.entity.PayCoinTypeMode;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.huanmedia.videochat.video.CallingActivity;
import com.huanmedia.videochat.video.MonitorVideoActivity;
import com.huanmedia.videochat.wxapi.WXPayEntryActivity;

import java.util.ArrayList;

/**
 * Class used to navigate through the application.
 */
public class Navigator {

    public Navigator() {
        //empty
    }


    public void navToMain(Context context, boolean autoLogin) {
        Intent intent = MainActivity.getCallingIntent(context, autoLogin);
        context.startActivity(intent);
    }

    public void navtoCalling(Activity context, ConditionEntity conditionEntity, String hint) {
        context.startActivity(CallingActivity.getCallingIntent(context, conditionEntity, hint));
    }

    public void navtoCoinPay(Activity context, String cion) {
        context.startActivity(CoinPayActivity.getCallingIntent(context, cion));
    }

    public void navtoOlderPay(Activity activity, PayCoinTypeMode payCoinTypeMode, String olderNum) {
        if (payCoinTypeMode == null) return;
        activity.startActivity(WXPayEntryActivity.getCallingIntent(activity, payCoinTypeMode, olderNum));
    }

    public void navtoMyAccound(Activity activity) {
        activity.startActivity(MyAccountActivity.getCallingIntent(activity));
    }

    public void navtoSetting(Activity activity) {
        activity.startActivity(SettingActivity.getCallingIntent(activity));
    }

    /**
     * 发现详情（个人资料 红人）
     *
     * @param activity
     * @param uid
     */
    public void navDiscoverInfo(Activity activity, int uid, String distance, @BusinessCardFragment.ShowType int showType) {
        if (uid == 0) return;
        activity.startActivity(BusinessCardAcitivty.getCallingIntent(activity, uid, distance, showType));
    }

    /**
     * 账户绑定
     *
     * @param activity
     * @param isBound
     * @param bountType
     */
    public void navtoAccoundBound(Activity activity, boolean isBound, int bountType) {
        activity.startActivity(AccountBoundActivity.getCallingIntent(activity, isBound, bountType));

    }

    /**
     * 红人认证资料界面
     *
     * @param activity
     */
    public void navtoRedMianAuth(Activity activity) {
        activity.startActivity(RedMianAuthActivity.getCallingIntent(activity));
    }

    /**
     * 红人认证
     *
     * @param activity
     */
    public void navtoReadMainCertificate(Activity activity) {
        activity.startActivity(ReadMainCertificateActivity.getCallingIntent(activity));
    }

    /**
     * 跳转到账户明细
     *
     * @param activity
     */
    public void navtoAccoundParticulars(Activity activity) {
        activity.startActivity(AccountParticularsActivity.getCallingIntent(activity));
    }

    /**
     * 跳转到Web页面
     *
     * @param activity
     * @param url
     * @param title
     */
    public void navtoWebActivity(Activity activity, String url, String title) {
        activity.startActivity(LocalHtmlWebActivity.getCallingIntent(activity, url, title));
    }

    /**
     * 跳转到Web页面
     *
     * @param activity
     * @param url
     * @param title
     */
    public void navtoWebActivity(Activity activity, String url, String title, boolean hasLogin) {
        activity.startActivity(LocalHtmlWebActivity.getCallingIntent(activity, url, title, hasLogin));
    }

    /**
     * 跳转到Web页面
     *
     * @param activity
     * @param url
     * @param title
     */
    public void navtoWebActivityForResult(Activity activity, String url, String title, int requestCode) {
        activity.startActivityForResult(LocalHtmlWebActivity.getCallingIntent(activity, url, title), requestCode);
    }

    /**
     * 用户编辑
     *
     * @param activity
     * @param authMsg
     */
    public void navtoUserInfoEdit(Activity activity, boolean isAuth, String authMsg) {
        activity.startActivity(UserInfoEditActivity.getCallingIntent(activity, isAuth, authMsg));
    }

    /**
     * 照片墙
     *
     * @param activity
     * @param data
     */
    public void navtoPhotos(Activity activity, ArrayList<PhotosEntity> data) {
        activity.startActivity(PhotosActivity.getCallingIntent(activity, data));
    }

    /**
     * 意见反馈
     *
     * @param activity
     */
    public void navtoFeedBack(Activity activity) {
        activity.startActivity(FeedBackActivity.getCallingIntent(activity));
    }

    /**
     * 系统消息
     *
     * @param activity
     */
    public void navtoSystemMessage(Activity activity) {
        activity.startActivity(NotificationMessageActivity.getCallingIntent(activity));
    }

    /**
     * 关于我们
     *
     * @param activity
     */
    public void navtoAboutUs(Activity activity) {
        activity.startActivity(AboutActivity.getCallingIntent(activity));
    }

    /**
     * 绑定用户信息
     *
     * @param activity
     */
    public void navtoBoundIDCard(Activity activity, int boundType, String user_id) {
        activity.startActivity(BoundIDCardActivity.getCallingIntent(activity, boundType, user_id));
    }

    /**
     * 信任值
     *
     * @param activity
     */
    public void navtoTrustValue(Activity activity) {
        activity.startActivity(TrustValueActivity.getCallingIntent(activity));
    }

    /**
     * 我的钱包
     *
     * @param activity
     */
    public void navtoMyWallet(Activity activity) {
        activity.startActivity(MyWalletActivity.getCallingIntent(activity));
    }

    /**
     * M币兑换钻石
     *
     * @param activity
     */
    public void navtoExchange(Activity activity) {
        activity.startActivity(ExchangeActivity.getCallingIntent(activity));
    }

    /**
     * 我的等级
     *
     * @param activity
     */
    public void navtoLevel(Activity activity) {
        activity.startActivity(LevelActivity.getCallingIntent(activity));
    }


    /**
     * 我的等级
     *
     * @param activity
     */
    public void navtoHelp(Activity activity) {
        activity.startActivity(HelpActivity.getCallingIntent(activity));
    }

    /**
     * 监控主界面
     *
     * @param activity
     */
    public void navtoMonitor(Activity activity) {
        activity.startActivity(MonitorActivity.getCallingIntent(activity));
    }

    /**
     * 监控主界面
     *
     * @param activity
     */
    public void navtoMonitorVideo(Activity activity, String channelName, int uid1, int uid2) {
        activity.startActivity(MonitorVideoActivity.getCallingIntent(activity, channelName, uid1, uid2));
    }

    /**
     * 视频播放
     *
     * @param context
     */
    public void navtoMediaPlay(Activity context, ArrayList<VideoEntity> vedios, int position) {
        context.startActivity(MediaPlayActivity.getCallingIntent(context, vedios, position));
    }

    /**
     * 视频上传界面
     *
     * @param context
     */
    public void navtoMediaUpLoad(Activity context, ArrayList<VideoEntity> videos, boolean isOpenUserEdit) {
        context.startActivity(MediaUpLoadActivity.getCallingIntent(context, videos, isOpenUserEdit));
    }

    /**
     * 预约界面
     *
     * @param context
     */
    public void navtoAppointment(Activity context, int uid) {
        context.startActivity(AppointmentOpActivity.getCallingIntent(context, uid));
    }

    /**
     * 预约列表界面
     *
     * @param context
     */
    public void navtoAppointmentList(Activity context, int tabPosition) {
        context.startActivity(AppointmentListActivity.getCallingIntent(context, tabPosition));
    }

    /**
     * 艺人主界面
     */
    public void navtoArtist(Activity context, int uid) {
        context.startActivity(ArtistActivity.getCallingIntent(context, uid));
    }

}
