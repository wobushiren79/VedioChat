package com.huanmedia.videochat.mvp.model.file;

import android.content.Context;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.base.DataFileCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.request.UploadImagesRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;

import java.util.List;

public interface IFileUpLoadModel {
    /**
     * 文件上传
     *
     * @param context
     * @param params
     * @param callBack
     */
    void fileUpLoad(Context context, FileUpLoadResults params, DataFileCallBack callBack);

    /**
     * 文件上传 by 阿里云
     *
     * @param context
     * @param params
     * @param callBack
     */
    void imageUpLoad(Context context, UploadImagesRequest params, List<String> images, DataCallBack callBack);

    /**
     * 文件上传 by 阿里云
     *
     * @param context
     * @param params
     * @param callBack
     */
    OSSAsyncTask fileUpLoadByAliyun(Context context, FileUpLoadResults params, DataFileCallBack callBack, boolean isShowDialog);


    /**
     * 获取阿里云上传数据
     *
     * @param context
     * @param params
     * @param callBack
     */
    void getAliyunUploadInfo(Context context, FileUpLoadRequest params, DataCallBack callBack, boolean isShowDialog);
}
