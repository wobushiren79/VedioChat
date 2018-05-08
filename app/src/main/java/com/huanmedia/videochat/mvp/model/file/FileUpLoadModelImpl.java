package com.huanmedia.videochat.mvp.model.file;

import android.content.Context;

import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.base.DataFileCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.repository.base.HttpFileResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class FileUpLoadModelImpl extends BaseMVPModel implements IFileUpLoadModel {

    @Override
    public void fileUpLoad(Context context, FileUpLoadResults params, DataFileCallBack callBack) {

    }

    @Override
    public void fileUpLoadByAliyun(Context context, FileUpLoadResults params, DataFileCallBack callBack) {
        MHttpManagerFactory.getFileManager().upLoadFileToAliyun(context, params, new HttpFileResponseHandler<PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectResult result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }

            @Override
            public void onProgress(long currentSize, long totalSize) {
                callBack.getDataOnProgress(currentSize, totalSize);
            }
        });
    }

    @Override
    public void getAliyunUploadInfo(Context context, FileUpLoadRequest params, DataCallBack callBack) {

    }
}
