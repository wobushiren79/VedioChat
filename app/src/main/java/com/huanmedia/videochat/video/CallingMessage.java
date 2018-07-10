package com.huanmedia.videochat.video;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huanmedia.ilibray.utils.GsonUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.service.socket.WMessage;
import com.huanmedia.videochat.common.service.socket.WSonMessageListener;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.weight.MatchConfig;
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;
import com.huanmedia.videochat.video.model.GiftModeMapping;
import com.huanmedia.videochat.video.model.TextChatMode;

import java.util.Map;
import java.util.TreeMap;

public class CallingMessage implements WSonMessageListener {

    private CallingPresenter mPresenter;
    private Gson mGson = GsonUtils.getDefGsonBulder().create();

    public CallingMessage(CallingPresenter presenter) {
        this.mPresenter = presenter;
    }


    @Override
    public void onMessage(WMessage message) {
        Map<String, String> contents;
        String callid;
        String chatStr;
        switch (message.getType()) {
            case "sendGiftnotice"://收到礼物
                if (mPresenter.isNullView()) break;
                contents = mGson.fromJson(message.getBody().toString(), new TypeToken<TreeMap<String, String>>() {
                }.getType());
                String gid = contents.get("giftid");
                String from = contents.get("from");
                String fromName = contents.get("fromname");
                String to = contents.get("to");
                String toName = contents.get("toname");
                String giftnumbers = contents.get("giftnumbers");
                String giftcoin = contents.get("giftcoin");
                String name = contents.get("giftname");

                if (Integer.valueOf(from) == UserManager.getInstance().getId()) {
                    chatStr = "你赠送了" + giftnumbers + "个 " + name + " 给" + mPresenter.getVideoChatEntity().getTouidinfo().getNickname();
                } else {
                    chatStr = mPresenter.getVideoChatEntity().getTouidinfo().getNickname() + "给你赠送了" + giftnumbers + "个 " + name + " ";
                }
                if (mPresenter.isCurrentChat(contents)) {
                    TextChatMode chatEntity = new TextChatMode(chatStr, TextChatMode.ChatType.TAGTEXT);
                    chatEntity.setDeffColorText(name);
                    mPresenter.getView().setChatText(chatEntity);
                    GiftEntity giftEntity = new GiftEntity();
                    giftEntity.setId(Integer.valueOf(gid));
                    giftEntity.setName(name);
                    giftEntity.setReceiveUserId(to);
                    giftEntity.setReceiveUserName(toName);
                    giftEntity.setSendUserId(from);
                    giftEntity.setSendUserName(fromName);
                    giftEntity.set_localMode(GiftModeMapping.getGiftMapping().get(Integer.parseInt(gid)));
                    giftEntity.setPayCount(Integer.parseInt(giftnumbers));
                    mPresenter.getView().showGift(giftEntity);
                }
                break;
            case "endChatnotice"://挂断视频
                if (!mPresenter.isNullView()) {
                    contents = mGson.fromJson(message.getBody().toString(), new TypeToken<TreeMap<String, String>>() {
                    }.getType());
                    if (mPresenter.isCurrentChat(contents)) {
                        String type = contents.get("endtype");
                        if (type.equals("2")) {//超时
                            return;
                        }
                        if (type.equals("3")) {//钻石不足
                            mPresenter.getView().showError(-3, message.getMsg());
                            mPresenter.getView().endCall();
                            return;
                        }
                        if (mPresenter.mSendEndCallMsg && type.equals("1") && !mPresenter.isNullView()) {
                            chatStr = "对方已挂断";
                            // 如果是匹配状态需要重新进入匹配 如果是通话状态需要直接挂断
                            mPresenter.getView().showError(0, chatStr);
                            if (mPresenter.getCondition().getVideoType() == ConditionEntity.VideoType.MATCH && mPresenter.getCondition().getMatchConfig().getMatchType() == MatchConfig.MatchType.DEF) {
                                mPresenter.getView().doLeaveChannel();
                            } else {
                                mPresenter.getView().endCall();
                            }

                        } else {
                            // 如果是匹配状态需要重新进入匹配 如果是通话状态需要直接挂断
                            if (mPresenter.getCondition().getVideoType() == ConditionEntity.VideoType.MATCH && mPresenter.getCondition().getMatchConfig().getMatchType() == MatchConfig.MatchType.DEF) {
                                mPresenter.getView().doLeaveChannel();
                            } else {
                                mPresenter.getView().endCall();
                            }
                        }
                    } else {
                        // 如果是匹配状态需要重新进入匹配 如果是通话状态需要直接挂断
                        if (mPresenter.getCondition().getVideoType() == ConditionEntity.VideoType.MATCH && mPresenter.getCondition().getMatchConfig().getMatchType() == MatchConfig.MatchType.DEF) {
                            mPresenter.getView().doLeaveChannel();
                        } else {
                            mPresenter.getView().endCall();
                        }
                    }
                }
                break;
            case "SearchSuccess"://匹配成功
                if (!mPresenter.isNullView()) {
                    mPresenter.setVideoChatEntity(mGson.fromJson(mGson.toJson(message.getBody()), VideoChatEntity.class));
                    mPresenter.getVideoChatEntity().set_location_VideoType(mPresenter.getCondition().getVideoType());
                    mPresenter.getView().chatOpenSuccess(mPresenter.getVideoChatEntity());
                }
                break;
            case "SearchTimeOut"://匹配失败
                if (!mPresenter.isNullView())
                    mPresenter.getView().showError(0, "SearchTimeOut::" + Check.checkReplace(message.getMsg(), "未知异常"));
                break;
            case "voideCommand"://视频操作命令
                if (!mPresenter.isNullView()) {
                    contents = mGson.fromJson(message.getBody().toString(), new TypeToken<TreeMap<String, String>>() {
                    }.getType());
                    TextChatMode chatEntity;
                    int maskLayout = 0;
                    if (contents.get("maskLayout") != null)
                        maskLayout = Integer.valueOf(contents.get("maskLayout").toString());
                    switch (contents.get("Commend")) {
                        case "USER_SELF_MASK"://对方用户自己主动揭面
                            if (mPresenter.isCurrentChat(contents)) {
                                mPresenter.getVideoChatEntity().setTomask(0);
//                                        chatStr = Check.checkReplace(getVideoChatEntity().getTouidinfo().getNickname()) + " 已揭自己的面";
//                                        chatEntity = new TextChatMode(chatStr, TextChatMode.ChatType.TEXT);
//                                        getView().setChatText(chatEntity);
                                mPresenter.getView().setChatText(null);
                                mPresenter.getView().chatMaskDisclose(false, maskLayout);
                                mPresenter.videoCommondReport(contents.get("to"), contents.get("from"), "USER_SELF_MASK_OK", 0, 0);
                            }
                            break;
                        case "USER_SELF_MASK_OK"://确认自己已揭面成功
                            if (mPresenter.isCurrentChat(contents)) {
//                                        chatStr = "您已向" + getVideoChatEntity().getTouidinfo().getNickname() + "揭面";
//                                        chatEntity = new TextChatMode(chatStr, TextChatMode.ChatType.TEXT);
//                                        getView().setChatText(chatEntity);
                                mPresenter.getView().setChatText(null);
                                mPresenter.getVideoChatEntity().setFrommask(0);
                                mPresenter.getView().chatMaskDisclose(true, maskLayout);
                            }
                            break;
                        case "USER_OTHER_MASK"://揭面 对方
                            if (mPresenter.isCurrentChat(contents)) {
                                chatStr = Check.checkReplace(mPresenter.getVideoChatEntity().getTouidinfo().getNickname()) + " 已揭您的面";
                                chatEntity = new TextChatMode(chatStr, TextChatMode.ChatType.TEXT);
                                mPresenter.getView().setChatText(chatEntity);
                                mPresenter.getVideoChatEntity().setFrommask(0);
                                mPresenter.getView().chatMaskEnable(false);//对方揭面关闭面具功能
                                int hasMask = mPresenter.getView().chatMaskDisclose(true, maskLayout);
                                mPresenter.videoCommondReport(contents.get("to"), contents.get("from"), "USER_OTHER_MASK_OK", maskLayout, hasMask);
                            }
                            break;
                        case "USER_OTHER_MASK_OK"://确认对方已揭面
                            int hasMask = 0;
                            if (contents.get("hasMask") != null)
                                hasMask = Integer.valueOf(contents.get("hasMask").toString());
                            chatStr = "您已揭" + mPresenter.getVideoChatEntity().getTouidinfo().getNickname() + "面";
                            chatEntity = new TextChatMode(chatStr, TextChatMode.ChatType.TEXT);
                            mPresenter.getView().setChatText(chatEntity);
                            if (mPresenter.isCurrentChat(contents)) {
                                mPresenter.getVideoChatEntity().setTomask(hasMask);
                                mPresenter.getView().chatMaskDisclose(false, maskLayout);
                            }
                            break;
                        case "USER_SELF_ADD_MASK"://对方带上面具
                            if (mPresenter.isCurrentChat(contents)) {
                                mPresenter.getVideoChatEntity().setTomask(1);
                                mPresenter.getView().addMaskOther();
                            }
                            break;
                        case "HINT_VIDEO":
                            if (mPresenter.isCurrentChat(contents)) {
                                int hintid = 0;
                                if (contents.get("hintid") != null)
                                    hintid = Integer.valueOf(contents.get("hintid").toString());
                                mPresenter.getView().hintVideo(hintid);
                            }
                            break;
                    }
                }
                break;
            case "addtimesCoin"://免费延长时间--http type =7 请求后会Socket通知双方
            case "addFreetimes":
                mPresenter.addTimeOk(message);
                break;
//                    case"beginACCOMPANYchatnotice":
//                        break;
        }
    }

    @Override
    public void onError(Throwable ex) {
        mPresenter.getView().showError(0, mPresenter.getGeneralErrorStr(ex));
    }
}
