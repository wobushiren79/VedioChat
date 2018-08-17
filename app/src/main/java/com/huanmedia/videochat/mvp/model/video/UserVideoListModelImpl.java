package com.huanmedia.videochat.mvp.model.video;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.UserVideoListRequest;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.List;

public class UserVideoListModelImpl extends BaseMVPModel implements IUserVideoListModel {
    @Override
    public void getUserVideoList(Context context, UserVideoListRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getUserVideoList(context, params, new HttpResponseHandler<List<VideoEntity>>() {
            @Override
            public void onSuccess(List<VideoEntity> result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
