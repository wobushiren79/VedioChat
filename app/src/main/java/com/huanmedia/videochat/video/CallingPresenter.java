package com.huanmedia.videochat.video;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huanmedia.ilibray.utils.GsonUtils;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.service.socket.WMessage;
import com.huanmedia.videochat.common.service.socket.WSonMessageListener;
import com.huanmedia.videochat.common.service.socket.WebSocketManager;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.weight.MatchConfig;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;
import com.huanmedia.videochat.video.model.GiftModeMapping;
import com.huanmedia.videochat.video.model.TextChatMode;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.disposables.Disposable;
import mvp.data.net.ApiException;
import mvp.presenter.Presenter;


/**
 * Create by Administrator
 * time: 2017/12/4.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class CallingPresenter extends Presenter<CallingView> {
    public final static String TIMER_JOINSUCCESS = "timer_joinSuccess";
    public final static String TIMER_CREATECALL = "timer_createCall";
    public final static String TIMER_CREATERECEIVE = "timer_createReceive";
    private final MainRepostiory mRepository;
    private ConditionEntity mCondition;
    private WSonMessageListener mWsListener;
    private Gson mGson = GsonUtils.getDefGsonBulder().create();
    private VideoChatEntity mVideoChatEntity;
    private List<ArrayList<GiftEntity>> Gifts;//礼物列表
    private boolean isNeedCallChatPeopleAttentionUpData;//是否需要关注列表更新
    private Disposable mCallTimer;
    public boolean mSendEndCallMsg;
    private int mEndFlag;

    public CallingPresenter() {
        this.mRepository = new MainRepostiory();
    }

    public void setVideoChatEntity(VideoChatEntity videoChatEntity) {
        mVideoChatEntity = videoChatEntity;
    }

    VideoChatEntity getVideoChatEntity() {
        return mVideoChatEntity;
    }

    public List<ArrayList<GiftEntity>> getGifts() {
        return Gifts;
    }

    public void setCondition(ConditionEntity condition) {
        mCondition = condition;
    }

    public ConditionEntity getCondition() {
        return mCondition;
    }

    @Override
    public void setView(@NonNull CallingView view) {
        super.setView(view);
        initWsocket();
        getGiftlist();
    }


    /**
     * 开始匹配
     */
    void beginSearch() {
        Map<String, String> prams = new HashMap<>();
        if (mCondition.getMatchConfig().getType() == ConditionEntity.ConditionTtype.FILTER) {
            prams.put("sex", mCondition.getMatchConfig().getSex() + "");
            prams.put("age", mCondition.getMatchConfig().getAge() + "");
        }
        prams.put("mask", mCondition.getMatchConfig().getMask() + "");
        addDisposable(mRepository.beginSearch(prams).subscribe(
                response -> {
                }, throwable -> {
                    if (!isNullView())
                        getView().showError(0, getGeneralErrorStr(throwable));
                }
        ));
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

    /**
     * 接听
     */
    public void answer() {
        getView().chatOpenSuccess(mVideoChatEntity);
    }

    /**
     * @param callid    会话ID
     * @param type      用户消费类型：1 赠送礼物2 增加发现聊天时间3 聊天充值4 红人陪聊消费5 随机用主 查询消费6 揭面扣金币
     * @param typevalue type==1 礼物的ID type==2 增加聊天时间分钟 [ 1分钟/10金币 3分钟/25金币 10分钟/90金币]
     */
    void chatCoinConsumption(GiftEntity gift, int callid, int type, int typevalue) {

        if (UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() == 0 && typevalue > 0) {
            getView().showBalanceDeficiency();
            return;
        }
        getView().cancelGiftDialog();
        Map<String, String> map = new HashMap<>();
        map.put("callid", callid + "");
        map.put("type", type + "");
        if (gift != null)
            map.put("giftnumbers", (gift.getPayCount() > 0 ? gift.getPayCount() : 1) + "");//默认数量为1
        if (typevalue != 0)
            map.put("typevalue", typevalue + "");
        addDisposable(mRepository.chatCoinConsumption(map).subscribe(response -> {
            if (gift != null) {
                UserManager.getInstance().getCurrentUser().getUserinfo().setCoin(UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() - gift.getCoin() * gift.getPayCount());
//                getView().showGift(from, gid, chatStr, gift.get_localMode());
            } else {
                if (!Check.isEmpty(response.getMessage())) {
                    switch (type) {
                        case 6:
                            UserManager.getInstance().getCurrentUser().getUserinfo().setCoin(UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() - 50);
                            videoCommond("USER_OTHER_MASK", typevalue);
                            getView().chatMaskDisclose(false, typevalue);
                            break;
                        case 2://付费延长时间--http 请求后会Socket通知双方
                            UserManager.getInstance().getCurrentUser().getUserinfo().setCoin(UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() - (int) (typevalue / UserManager.getInstance().getExchangeRate()));
                            break;


                    }
                }
            }
        }, throwable -> {
            if (isNullView()) return;

            if (throwable instanceof ApiException) {
                ApiException apiException = (ApiException) throwable;
                if (apiException.getErrorCode() == -8405) {//余额不足需要充值
                    getView().showBalanceDeficiency();
                } else {
                    getView().showError(0, apiException.getMessage());
                }
            } else {
                getView().showError(0, getGeneralErrorStr(throwable));
            }
        }));
    }

    void chatCoinConsumption(int callid, int type, int typevalue) {
        chatCoinConsumption(null, callid, type, typevalue);
    }


    /**
     * 开启会话（非匹配方式创建会话）
     */
    public void chatBegininfo() {
        Map<String, String> map = new HashMap<>();
        //如果红人是自己就放fan的ID，如果不是就放红人ID
        if (mCondition.getReadMainConfig().getRedManId() == UserManager.getInstance().getCurrentUser().getId()) {
            map.put("touid", mCondition.getReadMainConfig().getVideoChatConfig().getTouid() + "");//对方ID
            map.put("startUid", mCondition.getReadMainConfig().getRedManId() + "");//对方ID
        } else {
            map.put("touid", mCondition.getReadMainConfig().getRedManId() + "");//对方ID
        }

        map.put("chattype", "ACCOMPANY");// 会话类型
        map.put("is1v1search", "0");//是否是匹配模式（匹配模式会自动创建会话，需要传入0）
        addDisposable(mRepository.chatBegininfo(map).subscribe(
                videoChatEntity -> {
                    this.mVideoChatEntity = videoChatEntity;
                    this.mVideoChatEntity.set_location_VideoType(mCondition.getVideoType());
                    getView().chatOpenSuccess(mVideoChatEntity);
                }
                , throwable -> {
                    if (!isNullView()) {
                        if (throwable instanceof ApiException) {
                            if (((ApiException) throwable).getErrorCode() == -8409) {
                                getView().showError(-1, throwable.getMessage());
                            } else {
                                getView().showError(((ApiException) throwable).getErrorCode(), throwable.getMessage());
                                addDisposable(RxCountDown.delay(2).subscribe(//匹配成功后等待3秒执行连接
                                        integer -> {
                                            ((Activity) getContext()).finish();
                                        }
                                ));
                            }
                        } else {
                            getView().showError(0, getGeneralErrorStr(throwable));
                        }
                    }
                }
        ));
    }

    /**
     * 预约聊天
     */
    public void chatAppointment() {
        Map<String, String> map = new HashMap<>();
        map.put("touid", mCondition.getAppointmentConfig().getVideoChatConfig().getTouid() + "");//对方ID
        map.put("startUid", mCondition.getAppointmentConfig().getVideoChatConfig().getFromuid() + "");//对方ID
        map.put("extDataString", mCondition.getAppointmentConfig().getAcceptUserID() + "");//被预约方ID
        map.put("chattype", "APPOINTV2");// 会话类型
        map.put("is1v1search", "0");//是否是匹配模式（匹配模式会自动创建会话，需要传入0）
        map.put("appointv2id", mCondition.getAppointmentConfig().getOrderId() + "");//是否是匹配模式（匹配模式会自动创建会话，需要传入0）
        addDisposable(mRepository.chatBegininfo(map).subscribe(
                videoChatEntity -> {
                    this.mVideoChatEntity = videoChatEntity;
                    this.mVideoChatEntity.set_location_VideoType(mCondition.getVideoType());
                    getView().chatOpenSuccess(mVideoChatEntity);
                }
                , throwable -> {
                    if (!isNullView()) {
                        if (throwable instanceof ApiException) {
                            if (((ApiException) throwable).getErrorCode() == -8409) {
                                getView().showError(-1, throwable.getMessage());
                            } else {
                                getView().showError(((ApiException) throwable).getErrorCode(), throwable.getMessage());
                                addDisposable(RxCountDown.delay(2).subscribe(//匹配成功后等待3秒执行连接
                                        integer -> {
                                            ((Activity) getContext()).finish();
                                        }
                                ));
                            }
                        } else {
                            getView().showError(0, getGeneralErrorStr(throwable));
                        }
                    }
                }
        ));
    }

    /**
     * 开启会话（直聊）
     */
    public void chatDefault(int mask) {
        Map<String, String> map = new HashMap<>();
        map.put("touid", mCondition.getMatchConfig().getUid() + "");//对方ID
        map.put("chattype", "DEFAULT");// 会话类型
        map.put("is1v1search", "0");//是否是匹配模式（匹配模式会自动创建会话，需要传入0）
        map.put("mask", mask + "");//是否带面具

        addDisposable(mRepository.chatBegininfo(map).subscribe(
                videoChatEntity -> {
                    this.mVideoChatEntity = videoChatEntity;
                    this.mVideoChatEntity.set_location_VideoType(mCondition.getVideoType());
                    getView().chatOpenSuccess(mVideoChatEntity);
                }
                , throwable -> {
                    if (!isNullView()) {
                        if (throwable instanceof ApiException) {
                            if (((ApiException) throwable).getErrorCode() == -8409) {
                                getView().showError(-1, throwable.getMessage());
                            } else {
                                getView().showError(((ApiException) throwable).getErrorCode(), throwable.getMessage());
                            }
                        } else {
                            getView().showError(0, getGeneralErrorStr(throwable));
                        }
                    }
                }
        ));
    }

    /**
     * 红人模式发起这金币判断
     *
     * @return
     */
    public boolean isRedMainCoinEnough() {
        if (UserManager.getInstance().getCurrentUser() == null || UserManager.getInstance().getCurrentUser().getUserinfo() == null)
            return false;
        int currCoin = UserManager.getInstance().getCurrentUser().getUserinfo().getCoin();
        try {
            if (currCoin - getCondition().getReadMainConfig().getRedMainStartCoin() > 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public void destroy() {
        EventBus.getDefault().post(getVideoChatEntity() == null ? new VideoChatEntity(mCondition.getVideoType(), mEndFlag) : getVideoChatEntity());
        Intent intent = new Intent(EventBusAction.ACTION_CHATPEOPLEUPATA);
        intent.putExtra("tag", getContext().getString(R.string.calling_people));
        EventBus.getDefault().post(intent);
        if (isNeedCallChatPeopleAttentionUpData) {
            intent.putExtra("tag", getContext().getString(R.string.attention_people));
            EventBus.getDefault().post(intent);
        }
        WebSocketManager.getInstance().removeListener(mWsListener);
        GiftModeMapping.clearMapping();
        super.destroy();
    }

    private void getGiftlist() {
        addDisposable(mRepository.giftlist().subscribe(
                lists -> {
                    this.Gifts = lists;
                    for (List<GiftEntity> gifts : Gifts) {
                        for (GiftEntity giftEntity : gifts) {
                            giftEntity.set_localMode(GiftModeMapping.getGiftMapping().get(giftEntity.getId()));
                        }
                    }
                }, throwable -> {
                    if (!isNullView()) {
                        getView().showError(0, throwable.getMessage());
                    }
                }
        ));
    }

    /**
     * @param flag 1 收藏  0取消收藏
     */
    void favorite(int flag) {
        addDisposable(mRepository.favorite(getVideoChatEntity().getFromuid() + "", flag)
                .doAfterNext(disposable -> getView().showLoading(null))
                .subscribe(response -> {
                            isNeedCallChatPeopleAttentionUpData = true;
                            getView().resultFavoriteSuccess();
                        },
                        throwable -> {
                            if (!isNullView()) {
                                getView().showError(0, throwable.getMessage());
                            }
                        }
                        , () -> getView().hideLoading()));
    }

    //--------------------------------------------------------Socket通讯处理-----------------------------------------

    private void initWsocket() {
        mWsListener = new CallingMessage(this);
        WebSocketManager.getInstance().addMessageListener(mWsListener);
    }

    /**
     * 添加时长后处理数据
     *
     * @param message
     */
    public void addTimeOk(WMessage message) {
        if (message.getBody() != null) {
            Map<String, String> contents = mGson.fromJson(message.getBody().toString(), new TypeToken<TreeMap<String, String>>() {
            }.getType());
            if (!isCurrentChat(contents)) return;
            String from = contents.get("from");//from 不会随着设备改变，from标识发起方
            String freetimes = contents.get("freetimes");
            String coin = "0";
            if (contents.get("coin") != null)
                coin = contents.get("coin");

            int addtime = Integer.parseInt(freetimes);
            String animStr = addtime + " 秒";
            String chatText;
            if (Integer.parseInt(from) == UserManager.getInstance().getId()) {//判断是否是自己添加的时长
                chatText = "你已将你们的聊天时间加长了" + animStr;
            } else {
                chatText = Check.checkReplace(getVideoChatEntity().getTouidinfo().getNickname()) + "已将你们的聊天时间加长了" + animStr + "(+" + coin + "钻石)";
            }
            if (!isNullView()) {
                TextChatMode chatMode = new TextChatMode(chatText, TextChatMode.ChatType.TAGTEXT);
                chatMode.setDeffColorText(animStr);
                getView().setChatText(chatMode);
                getView().animOvertime(addtime, animStr);
            }
        }
    }

    /**
     * 隐藏指定人的视屏
     *
     * @param uid
     */
    public void hintVideo(long uid) {
        WMessage wMessage = new WMessage();
        wMessage.setFrom(getVideoChatEntity().getTouid() + "");
        wMessage.setTo(getVideoChatEntity().getFromuid() + "");
        wMessage.setType("voideCommand");
        Map<String, Object> map = new HashMap<>();
        map.put("from", wMessage.getFrom());
        map.put("to", wMessage.getTo());
        map.put("callid", getVideoChatEntity().getCallid());
        map.put("Commend", "HINT_VIDEO");
        map.put("hintid", uid);
        wMessage.setBody(map);
        WebSocketManager.getInstance().sendMessage(wMessage);
    }

    /**
     * 揭面
     *
     * @param layerNumber 揭面层数，0全部，1，第一层  2第二层。。。。。
     */
    void videoCommond(String commend, int layerNumber) {
        WMessage wMessage = new WMessage();
        wMessage.setFrom(getVideoChatEntity().getTouid() + "");
        wMessage.setTo(getVideoChatEntity().getFromuid() + "");
        wMessage.setType("voideCommand");
        Map<String, Object> map = new HashMap<>();
        map.put("from", wMessage.getFrom());
        map.put("to", wMessage.getTo());
        map.put("callid", getVideoChatEntity().getCallid());
        map.put("Commend", commend);
        map.put("maskLayout", layerNumber);
        wMessage.setBody(map);
        WebSocketManager.getInstance().sendMessage(wMessage);
    }

    /**
     * 揭面成功反馈
     *
     * @param from
     * @param to
     * @param commond
     * @param layerNumber 揭面层数，0全部，1，第一层  2第二层。。。。。
     */
    public void videoCommondReport(String from, String to, String commond, int layerNumber, int hasMask) {
        WMessage wMessage = new WMessage();
        wMessage.setFrom(from);
        wMessage.setTo(to);
        wMessage.setType("voideCommand");
        Map<String, Object> map = new HashMap<>();
        map.put("from", wMessage.getFrom());
        map.put("to", wMessage.getTo());
        map.put("callid", getVideoChatEntity().getCallid());
        map.put("Commend", commond);
        map.put("hasMask", hasMask);
        wMessage.setBody(map);
        WebSocketManager.getInstance().sendMessage(wMessage);
    }

    /**
     * 判断视频命令执行是否是当前聊天中的
     *
     * @param videoCommendData
     * @return
     */
    public boolean isCurrentChat(Map<String, String> videoCommendData) {
        try {
            if (String.valueOf(Integer.parseInt(videoCommendData.get("callid"))).equals(mVideoChatEntity.getCallid())) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 送礼物
     *
     * @param gift
     */
    public void postGiftData(GiftEntity gift) {
        chatCoinConsumption(gift,
                Integer.parseInt(getVideoChatEntity().getCallid()),
                1,
                gift.getId());
    }

    /**
     * 增加时长
     */
    public void postAddTime(GiftEntity gift, int addtime, int postType) {
        chatCoinConsumption(gift,
                Integer.parseInt(getVideoChatEntity().getCallid()),
                postType,
                addtime);
    }

    void callTimerStart(String action) {
        if (mCallTimer == null)
            mCallTimer = RxCountDown.delay(40).subscribe(
                    integer -> {
                        if (!isNullView()) {
                            switch (action) {
                                case TIMER_CREATECALL:
                                    getView().showError(-1, "对方暂时无法接听，请稍后再试");
                                    break;
                                case TIMER_JOINSUCCESS:
                                    getView().showError(-1, "对方无应答，请稍后再试");
                                    break;
                                case TIMER_CREATERECEIVE:
                                    getView().showError(-2, null);
                                    break;
                            }

                        }
                    }, Throwable::printStackTrace
            );
        addDisposable(mCallTimer);
    }

    void callTimerStop() {
        if (mCallTimer != null) {
            mCallTimer.dispose();
            remove(mCallTimer);
        }
        mCallTimer = null;
    }

    public void setSendEndCallMsg(boolean sendEndCallMsg) {
        mSendEndCallMsg = sendEndCallMsg;
    }

    /**
     * 挂断标识
     *
     * @param endFlag 0正常挂断 1 对方挂断 2 超时挂断
     */
    public void setEndFlag(int endFlag) {
        mEndFlag = endFlag;
        if (getVideoChatEntity() != null) {
            getVideoChatEntity().set_endFlag(endFlag);
        }
    }


    /**
     * @param callid  会话ID
     * @param endtype 挂断类型：1对方挂断 0正常挂断
     */
    void chatEnd(String callid, int endtype) {
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
}
