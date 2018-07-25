package com.huanmedia.videochat.mvp.model.chat;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ChatListRequest;
import com.huanmedia.videochat.mvp.entity.request.ChatSendRequest;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class ChatModelImpl extends BaseMVPModel implements IChatModel {
    @Override
    public void getChatList(Context context, ChatListRequest params, DataCallBack callBack) {
        boolean hasDialog = params.getNewid() == 0 ? false : true;
        MHttpManagerFactory.getMainManager().getChatList(context, params, new HttpResponseHandler<ChatListResults>() {
            @Override
            public void onSuccess(ChatListResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        }, false);
    }

    @Override
    public void chatSend(Context context, ChatSendRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getFileManager().chatSend(context, params, new HttpResponseHandler() {
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

    @Override
    public void appointmentComplain(Context context, int aid, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().appointmentComplain(context, aid, new HttpResponseHandler() {
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
