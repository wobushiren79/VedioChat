package com.huanmedia.videochat.mvp.model.file;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.base.DataFileCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileInfoChangeRequest;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class FileInfoChangeModelImpl extends BaseMVPModel implements IFileInfoChangeModel {

    @Override
    public void changeFileInfo(Context context, FileInfoChangeRequest params, String image, DataCallBack callBack) {
        MHttpManagerFactory.getFileManager().changeFileInfo(context, params, image, new HttpResponseHandler() {
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
