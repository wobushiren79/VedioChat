package com.huanmedia.videochat.mvp.model.chat;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.TalkRoomListRequest;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class TalkRoomListModelImpl extends BaseMVPModel implements ITalkRoomListModel {

    @Override
    public void getTalkRoomList(Context context, TalkRoomListRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().talkRoomList(context, params, new HttpResponseHandler<TalkRoomListResults>() {
            @Override
            public void onSuccess(TalkRoomListResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
