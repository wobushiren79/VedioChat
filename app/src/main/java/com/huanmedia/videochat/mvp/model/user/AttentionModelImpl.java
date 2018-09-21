package com.huanmedia.videochat.mvp.model.user;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AttentionRequest;
import com.huanmedia.videochat.mvp.entity.results.AttentionResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class AttentionModelImpl extends BaseMVPModel implements IAttentionModel {
    @Override
    public void attentionUser(Context context, AttentionRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().attentionUser(context, params, new HttpResponseHandler<Object>() {
                @Override
            public void onSuccess(Object result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
