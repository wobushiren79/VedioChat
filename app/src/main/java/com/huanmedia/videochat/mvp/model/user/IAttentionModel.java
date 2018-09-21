package com.huanmedia.videochat.mvp.model.user;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AttentionRequest;

public interface IAttentionModel {
    /**
     * 关注
     *
     * @param params
     * @param callBack
     */
    void attentionUser(Context context, AttentionRequest params, DataCallBack callBack);
}
