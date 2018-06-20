package com.huanmedia.videochat.mvp.model.video;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoPraiseRequest;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class ShortVideoPraiseModelImpl extends BaseMVPModel implements IShortVideoPraiseModel {
    @Override
    public void shortVideoPraise(Context context, ShortVideoPraiseRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().shortVideoPraise(context, params, new HttpResponseHandler() {
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
