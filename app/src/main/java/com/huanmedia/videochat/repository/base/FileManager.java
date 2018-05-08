package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;


public interface FileManager {

    /**
     * 阿里云文件上传
     */
    public OSSAsyncTask upLoadFileToAliyun(Context context, FileUpLoadResults upLoadResults, HttpFileResponseHandler<PutObjectResult> handler);


}
