package com.huanmedia.videochat.mvp.view.file;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IFileUpLoadView extends BaseMVPView {
    /**
     * 上传文件成功
     */
    void uploadFileSuccess();

    /**
     * 上传文件失败
     */
    void uploadFileFail(String msg);

    /**
     * 上传文件进度
     *
     * @param currentSize
     * @param totalSize
     */
    void uploadFileOnProgress(long currentSize, long totalSize);
}
