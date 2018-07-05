package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.RewardRequest;
import com.huanmedia.videochat.mvp.entity.results.RewardResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class RewardModelImpl extends BaseMVPModel implements IRewardModelImpl {
    @Override
    public void videoReward(Context context, RewardRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().reward(context, params, new HttpResponseHandler<RewardResults>() {
            @Override
            public void onSuccess(RewardResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
