package com.huanmedia.videochat.mvp.model.audio;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AudioFileRequest;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class AudioFileModelImpl extends BaseMVPModel implements IAudioFileModel {
    @Override
    public void addAudio(Context context, AudioFileRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().addUserAudio(context, params, new HttpResponseHandler() {
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
    public void deleteAudio(Context context, AudioFileRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().deleteUserAudio(context, params, new HttpResponseHandler() {
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
    public void getAudio(Context context, AudioFileRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getUserAudio(context, params, new HttpResponseHandler() {
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
