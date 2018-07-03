package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.RewardRequest;

public interface IRewardModelImpl {

    /**
     * 视频奖励
     * @param context
     * @param params
     * @param callBack
     */
    void videoReward(Context context, RewardRequest params, DataCallBack callBack);
}
