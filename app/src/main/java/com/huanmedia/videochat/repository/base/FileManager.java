package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.huanmedia.videochat.mvp.entity.request.UserVideoDataRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.UserVideoDataResults;
import com.huanmedia.videochat.repository.entity.PhpotsEntity;

import java.util.ArrayList;
import java.util.List;


public interface FileManager {

    /**
     * 阿里云文件上传
     */
    public OSSAsyncTask upLoadFileToAliyun(Context context, FileUpLoadResults upLoadResults, HttpFileResponseHandler<PutObjectResult> handler);

    /**
     * 上传文件
     * @param context
     * @param upLoadResults
     * @param handler
     */
    public void upLoadFile(Context context, FileUpLoadResults upLoadResults, HttpFileResponseHandler handler);

    /**
     * 上传图片
     * @param context
     * @param images
     * @param handler
     */
    public void upLoadImage(Context context,List<String> images, HttpResponseHandler<ArrayList<PhpotsEntity>> handler);

    /**
     * 用户上传视频信息
     * @param context
     * @param params
     * @param handler
     */
    void userVideoUpLoad(Context context, UserVideoDataRequest params, HttpResponseHandler<UserVideoDataResults> handler);

}
