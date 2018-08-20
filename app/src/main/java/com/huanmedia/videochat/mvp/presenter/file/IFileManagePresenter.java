package com.huanmedia.videochat.mvp.presenter.file;

public interface IFileManagePresenter {

    /**
     * 检测是否拥有视频文件
     * @param fileId
     */
    void checkHasVideoFile(int fileId);

    /**
     * 检测是否拥有照片文件
     * @param fileId
     */
    void checkHasPhotoFile(int fileId);

    /**
     * 支付视频文件
     * @param fileId
     */
    void payVideoFile(int fileId);


    /**
     * 支付图片文件
     * @param fileId
     */
    void payPhotoFile(int fileId);
}
