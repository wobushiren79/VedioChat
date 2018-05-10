package com.huanmedia.videochat.mvp.presenter.file;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;

import java.util.List;

public interface IFileUpLoadPresenter {
    /**
     * 获取阿里云上传信息
     */
    void getAliyunUpLoadInfo();

    /**
     * 开始上传阿里云
     */
    OSSAsyncTask startUpLoadByAliyun(FileUpLoadResults results);


}
