package com.huanmedia.videochat.mvp.view.user;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IAttentionView extends BaseMVPView {
    /**
     * 关注用户成功
     */
    void attentionUserSuccess();

    /**
     * 关注用户失败
     * @param msg
     */
    void attentionUserFail(String msg);
}
