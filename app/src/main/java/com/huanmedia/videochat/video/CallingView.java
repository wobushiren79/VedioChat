package com.huanmedia.videochat.video;

import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;
import com.huanmedia.videochat.video.model.TextChatMode;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/4.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface CallingView extends LoadDataView {
    /**
     * 会话打开
     * @param videoChatEntity
     */
    void chatOpenSuccess(VideoChatEntity videoChatEntity);

    /**
     * 揭面
     * @param isSelf 是否是自己
     */
    int chatMaskDisclose(boolean isSelf,int maskLayout);

    /**
     * 显示余额不足对话框
     */
    void showBalanceDeficiency();

    /**
     * 关闭礼物dialog
     */
    void cancelGiftDialog();

    /**
     * 关注成功
     */
    void resultFavoriteSuccess();

    /**
     * 送礼物家时长等文本显示
     * @param chatText
     */
    void setChatText(TextChatMode chatText);

    /**
     * 添加时长后动画显示
     * @param addtime
     * @param animStr 文本类容
     */
    void animOvertime(int addtime, String animStr);

    void showError(int errorCode, String message);

    void endCall();

    void showGift(GiftEntity entity);

    /**
     * 对方选择面具，或者带上面具
     */
    void addMaskOther();

    /**
     * 隐藏指定人的视屏
     * @param uid
     */
    void hintVideo(long uid);

    /**
     * 是否可以启用揭面功能
     * @param enabled
     */
    void chatMaskEnable(boolean enabled);
    /**
     * 退出当前聊天通道
     */
    void doLeaveChannel();


}
