package com.huanmedia.videochat.mvp.presenter.file;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;

import java.util.List;

public interface IFileUpLoadPresenter {
    /**
     * 获取阿里云上传信息
     * @param type 1视频 2音频
     */
    void getAliyunUpLoadInfo(int type);

    /**
     * 开始上传阿里云  视频
     */
    OSSAsyncTask startUpLoadByAliyunForVideo(FileUpLoadResults results);

    /**
     * 开始上传阿里云  音频
     */
    OSSAsyncTask startUpLoadByAliyunForAudio(FileUpLoadResults results);
}
