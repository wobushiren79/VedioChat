package com.huanmedia.videochat.mvp.model.file;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileInfoChangeRequest;
import com.huanmedia.videochat.mvp.entity.request.FileManageRequest;

public interface IFileManageModel {
    /**
     * 检测是否拥有此文件
     *
     * @param context
     * @param params
     * @param callBack
     */
    void checkHasFile(Context context, FileManageRequest params, DataCallBack callBack);

    /**
     * 支付此文件
     * @param context
     * @param params
     * @param callBack
     */
    void payFile(Context context, FileManageRequest params, DataCallBack callBack);
}
