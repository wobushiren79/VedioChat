package com.huanmedia.videochat.mvp.model.chat;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ChatReadRequest;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class ChatReadModelImpl extends BaseMVPModel implements IChatReadModel {

    @Override
    public void chatRead(Context context, ChatReadRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().readChat(context, params, new HttpResponseHandler() {
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
