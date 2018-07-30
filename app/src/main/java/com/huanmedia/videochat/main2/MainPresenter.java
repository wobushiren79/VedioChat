package com.huanmedia.videochat.main2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huanmedia.ilibray.utils.GsonUtils;
import com.huanmedia.ilibray.utils.Installation;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.data.cipher.Base64Cipher;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.appointment.AppointmentHistoryListActivity;
import com.huanmedia.videochat.appointment.AppointmentListActivity;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.service.notifserver.ringtone.NotificationHandler;
import com.huanmedia.videochat.common.service.notifserver.ringtone.NotificationMode;
import com.huanmedia.videochat.common.service.notifserver.ringtone.NotificationReceiver;
import com.huanmedia.videochat.common.service.notifserver.ringtone.RingtoneManager;
import com.huanmedia.videochat.common.service.socket.WMessage;
import com.huanmedia.videochat.common.service.socket.WSonMessageListener;
import com.huanmedia.videochat.common.service.socket.WebSocketManager;
import com.huanmedia.videochat.common.utils.LocationHandler;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.common.widget.dialog.MainHintDialog;
import com.huanmedia.videochat.main.NotificationMessageActivity;
import com.huanmedia.videochat.main.mode.SystemMessage;
import com.huanmedia.videochat.main.mode.mapping.SystemMessageDataMapper;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.weight.MatchConfig;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.entity.AppointmentEntity;
import com.huanmedia.videochat.repository.entity.CoinEntity;
import com.huanmedia.videochat.repository.entity.MessageEntity;
import com.huanmedia.videochat.repository.entity.SMsgEntity;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;
import com.huanmedia.videochat.video.CallingActivity;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.DataSupport;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import mvp.data.dispose.interactor.ThreadExecutorHandler;
import mvp.data.net.converter.RetryWithDelay;
import mvp.presenter.Presenter;

import static com.huanmedia.videochat.main2.fragment.MatchFragment.hasMatchFragment;

/**
 * @TODO Created by yb(yb498869020@hotmail.com) on 2017-11-26.
 */

public class MainPresenter extends Presenter<MainView> {
    private final MainRepostiory mRepository;
    private WSonMessageListener mWsListener;
    private Gson mGson = GsonUtils.getDefGsonBulder().create();

    private NotificationReceiver notificationReceiver;

    public MainPresenter() {
        this.mRepository = new MainRepostiory();
    }

    @Override
    public void setView(@NonNull MainView view) {
        super.setView(view);
        countBoot();
        NotificationHandler.createChannelId(NotificationHandler.channelId_Notice, "通知");
        NotificationHandler.createChannelId(NotificationHandler.channelId_videoCall, "视频连线");
        NotificationHandler.createChannelNoSound(NotificationHandler.channelId_NoticeNoSound, "通知无声音");

        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(RingtoneManager.ACTION_DISMIS);
        filter.addAction(RingtoneManager.ACTION_CONTENT);
        filter.addAction(RingtoneManager.ACTION_START_VIDEOCALL);
        getContext().registerReceiver(notificationReceiver, filter);
    }

    /**
     * 启动次数统计
     */
    private void countBoot() {
        addDisposable(mRepository.countboot(Installation.id(getContext()))
                .subscribe(response -> {
                }, Throwable::printStackTrace));
    }

    public void autoLogin(boolean autoLogin) {
        if (autoLogin) {
            RetryWithDelay retryWithDelay = new RetryWithDelay(Integer.MAX_VALUE, 3000);
            mRepository.syncTime().flatMap(response -> {
                Map<String, String> result = new Gson().fromJson(response.getResult().toString(), new TypeToken<Map<String, String>>() {
                }.getType());
                String time = result.get("time");
                UserManager.getInstance().saveTimeSync(time);
                Map<String, String> prmas = new HashMap<>();
                prmas.put("mobile", UserManager.getInstance().getCurrentUser().getMobile());
                prmas.put("autoString", UserManager.getInstance().getCurrentUser().getAutoString());
                prmas.put("deviceid", Installation.id(getContext()));
                prmas.put("os", 1 + "");
                prmas.put("version", BuildConfig.VERSION_CODE + "");
                prmas.put("channelid", BuildConfig.appChannel + "");
                Location location = new LocationHandler().getLocation();
                if (location != null) {
                    prmas.put("longitude", location.getLongitude() + "");
                    prmas.put("latiude", location.getLatitude() + "");
                }
                return mRepository.login(prmas);
            }).retryWhen(retryWithDelay)
                    .subscribe(
                            this::initUserForApp,
                            throwable -> {
                                if (!isNullView()) {
                                    Toast.makeText(getContext(), getGeneralErrorStr(throwable), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
        } else {
            initUserForApp(UserManager.getInstance().getCurrentUser());
        }


    }

    private void initUserForApp(UserEntity userEntity) {
        String dbName = String.valueOf(userEntity.getId()) + userEntity.getMobile();
        LitePal.use(LitePalDB.fromDefault(new String(new Base64Cipher().encrypt(dbName.getBytes()))));
        UserManager.getInstance().saveUser(userEntity);
        EventBus.getDefault().post(new Intent(EventBusAction.ACTION_USERDATA_CHANGE));
        getNoRedCound();
        FApplication.initLocalSocket();
        initWsocket();
    }

    void upUserData() {
        addDisposable(mRepository.myinfo().subscribe(
                userinfoEntity -> {
                    UserManager.getInstance().getCurrentUser().setUserinfo(userinfoEntity);
                    UserManager.getInstance().saveUser();
                    EventBus.getDefault().post(new Intent(EventBusAction.ACTION_USERDATA_CHANGE));
                },
                throwable -> {
                    if (!isNullView()) {
                        Toast.makeText(getContext(), getGeneralErrorStr(throwable), Toast.LENGTH_SHORT).show();
                    }
                }
        ));
    }

    void coinChange() {
        addDisposable(mRepository.myinfo().subscribe(
                userinfoEntity -> {
                    UserManager.getInstance().getCurrentUser().setUserinfo(userinfoEntity);
                    UserManager.getInstance().saveUser();
                    EventBus.getDefault().post(new Intent(EventBusAction.ACTION_COIN_CHANGED));
                },
                throwable -> {
                    if (!isNullView()) {
                        Toast.makeText(getContext(), getGeneralErrorStr(throwable), Toast.LENGTH_SHORT).show();
                    }
                }
        ));
    }

    /**
     * 结束本次通话
     * 向服务器报告
     *
     * @param callid
     */
    void chatEnd(int callid) {
        chatEnd(callid, 0);
    }

    /**
     * @param callid  会话ID
     * @param endtype 挂断类型：1对方挂断 0正常挂断
     */
    void chatEnd(int callid, int endtype) {
        Map<String, String> map = new HashMap<>();
        map.put("callid", callid + "");//对方ID
        map.put("endtype", String.valueOf(endtype));
        addDisposable(mRepository.chatEnd(map).subscribe(response -> {
        }, Throwable::printStackTrace));
        addDisposable(mRepository.getUserInfo((int) UserManager.getInstance().getId()).subscribe(response -> {
            UserManager.getInstance().getCurrentUser().getUserinfo().setCoin(response.getCoin());
            UserManager.getInstance().getCurrentUser().getUserinfo().setExchangecoin(response.getExchangecoin());
        }, Throwable::printStackTrace));
    }

    /**
     * 主动取消搜索
     */
    protected void forceOutSearch() {
        addDisposable(mRepository.forceOutSearch().subscribe(
                response -> {
                }, Throwable::printStackTrace
        ));
    }

    @Override
    public void destroy() {
        FApplication.destoryLocalSocket();
        WebSocketManager.getInstance().removeListener(mWsListener);
        getContext().unregisterReceiver(notificationReceiver);
        super.destroy();
    }

    public void getNoRedCound() {
        Intent intent = new Intent(EventBusAction.ACTION_SYSTEM_MESSAGE);
        intent.putExtra("msgCount", UserManager.getInstance().getNoRedMsg());
        EventBus.getDefault().post(intent);
//        SystemMessage.select("isRed").where("isRed = 0")
//                .countAsync(SystemMessage.class).listen(count -> {
//            if (!isNullView())
//                getView().upSysMsgNoRed(count);
//        });
    }

//    public void redAllSysMsg() {
//        ContentValues value = new ContentValues();
//        value.put("isRed", 1);
//        SystemMessage.updateAllAsync(SystemMessage.class, value, "isRed=0").listen(rowsAffected -> {
//        });
//    }


    //----------------------------------------------------WSocket--------------------------------------------


    private void initWsocket() {
        mWsListener = new WSonMessageListener() {
            @Override
            public void onMessage(WMessage message) {
                if (message.getType().equals("beginACCOMPANYchatnotice")) {
                    VideoChatEntity mVideoChatEntity = mGson.fromJson(mGson.toJson(message.getBody()), VideoChatEntity.class);
                    if (CallingActivity.getmCallingState() == CallingActivity.CallingType.LEISURE) {//红人
                        ConditionEntity condition = new ConditionEntity();
                        condition.setVideoType(ConditionEntity.VideoType.REDMAN);
                        condition.getReadMainConfig().setRequestType(ConditionEntity.RequestType.PERSON);
                        condition.getReadMainConfig().setVideoChatConfig(mVideoChatEntity);
                        if (hasMatchFragment) {
                            condition.setNeedCloseFU(0);
                        }
                        if (message.getBody().containsKey("startUid") && !(message.getBody().get("startUid")).equals("0") && (message.getBody().get("startUid").toString()).equals(message.getBody().get("fromuid").toString())) {
                            condition.getReadMainConfig().setRedManId(Integer.valueOf(message.getBody().get("startUid").toString()));
                            ResourceManager.getInstance().getNavigator().navtoCalling((Activity) getContext(), condition, mVideoChatEntity.getTouidinfo().getNickname() + "\n向你发起视频聊天，是否接受?;(聊天花费" + mVideoChatEntity.getChatcoin() + "{钻石}/分钟)");
                        } else {
                            ResourceManager.getInstance().getNavigator().navtoCalling((Activity) getContext(), condition, mVideoChatEntity.getTouidinfo().getNickname() + "\n向你发起视频聊天，是否接受?;(接受可获" + mVideoChatEntity.getChatcoin() + "{钻石}/分钟)");
                        }
                        NotificationHandler.sendNotification(mVideoChatEntity);
                    } else {
                        chatEnd(Integer.parseInt(mVideoChatEntity.getCallid()), 1);
                    }
                } else if (message.getType().equals("beginDEFAULTchatnotice")) {//非红人直接聊天
                    VideoChatEntity mVideoChatEntity = mGson.fromJson(mGson.toJson(message.getBody()), VideoChatEntity.class);
                    if (CallingActivity.getmCallingState() == CallingActivity.CallingType.LEISURE) {

                        ConditionEntity condition = new ConditionEntity();
                        condition.setVideoType(ConditionEntity.VideoType.MATCH);
                        condition.getMatchConfig().setRequestType(ConditionEntity.RequestType.PERSON);
                        condition.getMatchConfig().setVideoChatConfig(mVideoChatEntity);
                        condition.getMatchConfig().setMatchType(MatchConfig.MatchType.CALL);
                        if (hasMatchFragment) {
                            condition.setNeedCloseFU(0);
                        }
                        ResourceManager.getInstance().getNavigator().navtoCalling((Activity) getContext(), condition, mVideoChatEntity.getTouidinfo().getNickname() + "\n向你发起视频聊天，是否接受?");
                        NotificationHandler.sendNotification(mVideoChatEntity);
                    } else {
                        chatEnd(Integer.parseInt(mVideoChatEntity.getCallid()), 1);
                    }
                } else if (message.getType().equals("beginAPPOINTV2chatnotice")) {
                    VideoChatEntity mVideoChatEntity = mGson.fromJson(mGson.toJson(message.getBody()), VideoChatEntity.class);
                    if (CallingActivity.getmCallingState() == CallingActivity.CallingType.LEISURE) {//红人
                        ConditionEntity condition = new ConditionEntity();
                        String[] extData = mVideoChatEntity.getExtDataString().split(",");
                        condition.setVideoType(ConditionEntity.VideoType.APPOINTMENT);
                        condition.setRequestType(ConditionEntity.RequestType.PERSON);
                        condition.getAppointmentConfig().setVideoChatConfig(mVideoChatEntity);
                        if (extData != null && extData.length >= 2) {
                            condition.getAppointmentConfig().setAcceptUserID(Integer.valueOf(extData[0]));
                            condition.getAppointmentConfig().setOrderId(Integer.valueOf(extData[1]));
                        }
                        if (hasMatchFragment) {
                            condition.setNeedCloseFU(0);
                        }
                        ResourceManager.getInstance().getNavigator().navtoCalling((Activity) getContext(), condition, mVideoChatEntity.getTouidinfo().getNickname() + "\n向你发起视频聊天，是否接受?");
                        NotificationHandler.sendNotification(mVideoChatEntity);
                    } else {
                        chatEnd(Integer.parseInt(mVideoChatEntity.getCallid()), 1);
                    }
                } else if (message.getType().equals("NOTICE")) {//通知消息
                    switch (message.getStype()) {
                        case "SYSTEMNOTICE"://系统通知
                        case "MESSAGE":
                        case "WARNING":
                            Object items = message.getBody().get("items");
                            if (items == null) {
                                sysNotice(message, null);
                            } else {
                                sysNotices(message, null);
                            }
                            break;
                        case "STARAUTH"://红人认证通知
                            readMainNotice(message);
                            break;
                        case "CASHTOACCOUNT"://提现通知
                        case "SYSTEMDIAMOND"://系统发放钻石
                            systemMsg(message, null);
                            break;
                        case "OUTACCOUNT"://踢下线
                            ToastUtils.showToastShort(getContext(), "您已被封号");
                            UserManager.getInstance().outLogin(null);
                            UserManager.getInstance().exit();
                            break;
                        case "ChatMessageCount"://踢下线
                            MessageEntity data = mGson.fromJson(mGson.toJson(message.getBody()), MessageEntity.class);
                            Intent intent = new Intent(EventBusAction.ACTION_CHAT_MESSAGE_APPOINTMENT);
                            intent.putExtra("msgCount", data.getCount());
                            EventBus.getDefault().post(intent);
                            break;
                        default:
                            break;
                    }
                } else if (message.getType().equals("appointmentNotice")) {
                    AppointmentEntity data = mGson.fromJson(mGson.toJson(message.getBody()), AppointmentEntity.class);
                    switch (message.getStype()) {
                        case "daojishi":
                            getView().showAppointmentHint((int) (data.getApptime() - data.getSystime()), data.getUidfrom(), data.getUidto());
                            break;
                        case "cancel":
                        case "appointconfirm":
                            sysNotice(message, AppointmentHistoryListActivity.class);
                            break;
                        case "hasnewappoint":
                            systemMsg(message, AppointmentHistoryListActivity.class);
                            break;
                        case "timeget":
                            String contentStr;
                            Activity currentActivity = ActivitManager.getAppManager().currentActivity();
                            boolean isCalling = ActivitManager.getAppManager().existsActivity(CallingActivity.class);
                            if (isCalling) {
                                contentStr = "您与" + data.getUnickname() + "已到预约视频聊天时间，点击发起视频，并挂掉当前视频通话";
                            } else {
                                contentStr = "您与" + data.getUnickname() + "已到预约视频聊天时间，点击发起视频";
                            }
                            GeneralDialog dialog = new GeneralDialog(currentActivity);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog
                                    .setContent(contentStr)
                                    .setCallBack(new GeneralDialog.CallBack() {
                                        @Override
                                        public void submitClick(Dialog dialog) {
                                            if (isCalling) {
                                                currentActivity.finish();
                                            }
                                            UserEntity myInfo = UserManager.getInstance().getCurrentUser();
                                            ConditionEntity condition = new ConditionEntity();
                                            condition.setVideoType(ConditionEntity.VideoType.REDMAN);
                                            condition.getReadMainConfig().setRedManId((int) myInfo.getId());
                                            condition.getReadMainConfig().setRequestType(ConditionEntity.RequestType.SELF);
                                            condition.getReadMainConfig().setRedMainStartCoin(myInfo.getUserinfo().getStarcoin());
                                            condition.getReadMainConfig().setReadMainStartTime(myInfo.getUserinfo().getStartime());
                                            VideoChatEntity videoChatEntity = new VideoChatEntity();
                                            videoChatEntity.setTouid(data.getUidfrom());
                                            condition.getReadMainConfig().setVideoChatConfig(videoChatEntity);
                                            ((BaseActivity) getContext()).getNavigator().navtoCalling((Activity) getContext(), condition, "您已进入约聊中;");
                                        }

                                        @Override
                                        public void cancelClick(Dialog dialog) {

                                        }
                                    })
                                    .show();
                            break;
                    }
                } else if (message.getType().equals("coin")) {
                    CoinEntity data = mGson.fromJson(mGson.toJson(message.getBody()), CoinEntity.class);
                    switch (message.getStype()) {
                        case "coin":
                            UserManager.getInstance().getCurrentUser().getUserinfo().setCoin(data.getCoin());
                            systemMsg(message, null);
                            break;
                        case "registercoin":
                            UserManager.getInstance().getCurrentUser().getUserinfo().setCoin(data.getCoin());
                            MainHintDialog coinDialog = new MainHintDialog(getContext(), MainHintDialog.MainHintType.Coin);
                            coinDialog.setContentText(data.getTxt());
                            coinDialog.show();
                            break;
                        case "ads":
                            MainHintDialog normalDialog = new MainHintDialog(getContext(), MainHintDialog.MainHintType.Normal);
                            normalDialog.setContentText(data.getTxt());
                            normalDialog.setTitleStr(data.getTitle());
                            normalDialog.setImageUrl(data.getImgurl());
                            normalDialog.setJumpUrl(data.getJmpurl());
                            normalDialog.show();
                            break;
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                if (!(ex instanceof UnknownHostException)) {
                    Log.v("this", "" + Log.getStackTraceString(ex));
                    WebSocketManager.getInstance().reConnection();
//                    Toast.makeText(getContext(), Log.getStackTraceString(ex), Toast.LENGTH_SHORT).show();
                }
//                    Toast.makeText(getContext(), "亲，您的网络有异常哟", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), Log.getStackTraceString(ex), Toast.LENGTH_SHORT).show();
            }
        };
        WebSocketManager.getInstance().addMessageListener(mWsListener);
    }

    private void systemMsg(WMessage message, Class intentActivity) {
        Observable.just(message)
                .compose(ThreadExecutorHandler.toMain(ResourceManager.getInstance().getDefaultThreadProvider()))
                .map(message1 -> {
                    SMsgEntity sMsgEntity = mGson.fromJson(mGson.toJson(message1.getBody()), SMsgEntity.class);
                    SystemMessage systemMessage = new SystemMessageDataMapper().transform(sMsgEntity);
                    systemMessage.save();
                    EventBus.getDefault().post(systemMessage);
                    EventBus.getDefault().post(new Intent(EventBusAction.ACTION_USERINFO_UPDATE));//更新用户信息
                    NotificationMode mode = new NotificationMode();
                    mode.setNotifiID(Integer.valueOf(message.getFrom()));
                    mode.setTitle(systemMessage.getTitle());
                    mode.setContent(systemMessage.getDesc());
                    if (intentActivity != null)
                        mode.setIntentActivity(intentActivity);
                    NotificationHandler.sendNotification(mode);
                    int count = -1;
                    if (!ActivitManager.getAppManager().existsActivity(NotificationMessageActivity.class)) {
                        count = SystemMessage.where("isRed = 0").count(SystemMessage.class);
                    }
                    return count;
                }).subscribe(integer -> {
            //添加小红点
            if (!isNullView() && integer >= 0) {
                UserManager.getInstance().setNoRedMsg(integer);
                getNoRedCound();
            }
        }, throwable -> {
            Logger.e(getGeneralErrorStr(throwable));
        });
    }

    private void readMainNotice(WMessage message) {
        Observable.just(message)
                .compose(ThreadExecutorHandler.toMain(ResourceManager.getInstance().getDefaultThreadProvider()))
                .map(message1 -> {
                    SMsgEntity sMsgEntity = mGson.fromJson(mGson.toJson(message1.getBody()), SMsgEntity.class);
                    SystemMessage systemMessage = new SystemMessageDataMapper().transform(sMsgEntity);
                    systemMessage.save();
                    EventBus.getDefault().post(systemMessage);
                    EventBus.getDefault().post(new Intent(EventBusAction.ACTION_USERINFO_UPDATE));//更新用户信息
                    NotificationMode mode = new NotificationMode();
                    mode.setNotifiID(Integer.valueOf(message.getFrom()));
                    mode.setTitle(systemMessage.getTitle());
                    mode.setContent(systemMessage.getDesc());
                    NotificationHandler.sendNotification(mode);
                    int count = -1;
                    if (!ActivitManager.getAppManager().existsActivity(NotificationMessageActivity.class)) {
                        count = SystemMessage.where("isRed = 0").count(SystemMessage.class);
                    }
                    return count;
                }).subscribe(integer -> {
            //添加小红点
            if (!isNullView() && integer >= 0) {
                UserManager.getInstance().setNoRedMsg(integer);
                getNoRedCound();
            }
        }, throwable -> {
            Logger.e(getGeneralErrorStr(throwable));
        });
    }

    private void sysNotices(WMessage message, Class intentActivity) {//登录后返回的未推送系统消息列表
        Observable.just(message)
                .compose(ThreadExecutorHandler.toMain(ResourceManager.getInstance().getDefaultThreadProvider()))
                .map(message1 -> {
                    Object items = message1.getBody().get("items");
                    List<SMsgEntity> tabdatas = mGson.fromJson(mGson.toJson(items), new TypeToken<List<SMsgEntity>>() {
                    }.getType());
                    List<SystemMessage> systemMessages = (List<SystemMessage>) new SystemMessageDataMapper().transform(tabdatas);
                    DataSupport.saveAll(systemMessages);
                    if (systemMessages.size() > 0) {
                        EventBus.getDefault().post(systemMessages);
                        for (int i = 0; i < systemMessages.size(); i++) {
                            SystemMessage systemMessage = systemMessages.get(i);
                            NotificationMode mode = new NotificationMode();
                            mode.setNotifiID(systemMessage.getMsgId());
                            if (systemMessage.getTitle() != null)
                                mode.setTitle(systemMessage.getTitle());
                            if (systemMessage.getDesc() != null)
                                mode.setContent(systemMessage.getDesc());
                            if (intentActivity != null)
                                mode.setIntentActivity(intentActivity);
                            if (i == 0) {
                                NotificationHandler.sendNotification(mode);
                            } else {
                                NotificationHandler.sendNotification(mode, false, NotificationHandler.channelId_NoticeNoSound);
                            }
                        }
                        UserManager.getInstance().setSystemMsgMaxId(systemMessages.get(0).getMsgId());
                    }
                    int count = -1;
                    if (!ActivitManager.getAppManager().existsActivity(NotificationMessageActivity.class)) {
                        count = SystemMessage.where("isRed = 0").count(SystemMessage.class);
                    }
                    return count;
                }).subscribe(integer -> {
            //添加小红点
            if (!isNullView() && integer >= 0) {
                UserManager.getInstance().setNoRedMsg(integer);
                getNoRedCound();
            }
        }, throwable -> Logger.e(getGeneralErrorStr(throwable)));

    }

    private void sysNotice(WMessage message, Class intentActivity) {
        Observable.just(message)
                .compose(ThreadExecutorHandler.toMain(ResourceManager.getInstance().getDefaultThreadProvider()))
                .map(message1 -> {
                    SMsgEntity sMsgEntity = mGson.fromJson(mGson.toJson(message1.getBody()), SMsgEntity.class);
                    SystemMessage systemMessage = new SystemMessageDataMapper().transform(sMsgEntity);
                    systemMessage.save();
                    EventBus.getDefault().post(systemMessage);
                    NotificationMode mode = new NotificationMode();
                    mode.setNotifiID(Integer.valueOf(message.getFrom()));
                    if (systemMessage.getTitle() != null)
                        mode.setTitle(systemMessage.getTitle());
                    if (systemMessage.getDesc() != null)
                        mode.setContent(systemMessage.getDesc());
                    if (intentActivity != null)
                        mode.setIntentActivity(intentActivity);
                    NotificationHandler.sendNotification(mode);
                    UserManager.getInstance().setSystemMsgMaxId(systemMessage.getMsgId());
                    int count = -1;
                    if (!ActivitManager.getAppManager().existsActivity(NotificationMessageActivity.class)) {
                        count = SystemMessage.where("isRed = 0").count(SystemMessage.class);
                    }
                    return count;
                }).subscribe(integer -> {
            //添加小红点
            if (!isNullView() && integer >= 0) {
                UserManager.getInstance().setNoRedMsg(integer);
                getNoRedCound();
            }
        }, throwable -> {
            Logger.e(getGeneralErrorStr(throwable));
        });

    }


}
