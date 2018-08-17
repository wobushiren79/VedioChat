package com.huanmedia.videochat.mvp.model.file;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileInfoChangeRequest;

public interface IFileInfoChangeModel {
    /**
     * 图片文件信息修改
     *
     * @param context
     * @param params
     * @param image
     * @param callBack
     */
    void changeImageInfo(Context context, FileInfoChangeRequest params, String image, DataCallBack callBack);

    /**
     * 视频文件信息修改
     *
     * @param context
     * @param params
     * @param videoImage
     * @param callBack
     */
    void changeVideoInfo(Context context, FileInfoChangeRequest params, String videoImage, DataCallBack callBack);
}
