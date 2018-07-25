package com.huanmedia.videochat.mvp.view.chat;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IChatReadView extends BaseMVPView {
    /**
     * 阅读聊天成功
     */
    void chatReadSuccess();

    /**
     * 阅读聊天失败
     */
    void chatReadFail(String msg);
}
