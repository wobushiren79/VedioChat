package com.huanmedia.videochat.mvp.model.file;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileInfoChangeRequest;

public interface IFileInfoChangeModel {
    /**
     * 文件信息修改
     *
     * @param context
     * @param params
     * @param image
     * @param callBack
     */
    void changeFileInfo(Context context, FileInfoChangeRequest params, String image, DataCallBack callBack);

}
