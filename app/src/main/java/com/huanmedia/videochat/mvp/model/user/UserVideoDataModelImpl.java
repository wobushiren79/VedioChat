package com.huanmedia.videochat.mvp.model.user;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.UserVideoDataRequest;
import com.huanmedia.videochat.mvp.entity.results.UserVideoDataResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.List;

public class UserVideoDataModelImpl extends BaseMVPModel implements IUserVideoDataModel {
    @Override
    public void uploadUserVideo(Context context, UserVideoDataRequest params, List<String> videoImages, DataCallBack callBack) {
        MHttpManagerFactory.getFileManager().userVideoUpLoad(context, params, videoImages, new HttpResponseHandler<UserVideoDataResults>() {
            @Override
            public void onSuccess(UserVideoDataResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }

    @Override
    public void deleteUserVideo(Context context, UserVideoDataRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().userVideoDelete(context, params, new HttpResponseHandler() {
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
