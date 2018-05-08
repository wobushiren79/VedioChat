package com.huanmedia.videochat.mvp.presenter.file;

import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;

public interface IFileUpLoadPresenter {
    /**
     * 获取阿里云上传信息
     */
    void getAliyunUpLoadInfo();

    /**
     * 开始上传阿里云
     */
    void startUpLoadByAliyun(FileUpLoadResults results);
}
