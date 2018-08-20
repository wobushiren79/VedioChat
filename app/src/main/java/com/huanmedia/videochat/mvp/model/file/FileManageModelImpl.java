package com.huanmedia.videochat.mvp.model.file;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileManageRequest;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class FileManageModelImpl extends BaseMVPModel implements IFileManageModel {

    @Override
    public void checkHasFile(Context context, FileManageRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().checkHasFile(context, params, new HttpResponseHandler<FileManageResults>() {
            @Override
            public void onSuccess(FileManageResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }

    @Override
    public void payFile(Context context, FileManageRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().payFile(context, params, new HttpResponseHandler<FileManageResults>() {
            @Override
            public void onSuccess(FileManageResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
