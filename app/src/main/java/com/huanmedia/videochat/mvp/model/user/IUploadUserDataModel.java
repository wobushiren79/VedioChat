package com.huanmedia.videochat.mvp.model.user;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.UploadUserDataRequest;

public interface IUploadUserDataModel {
    void submitEvaluation(Context context, UploadUserDataRequest params, DataCallBack callBack);
}
