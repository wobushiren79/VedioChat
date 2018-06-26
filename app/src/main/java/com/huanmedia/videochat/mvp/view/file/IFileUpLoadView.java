package com.huanmedia.videochat.mvp.view.file;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.request.VideoInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import java.util.ArrayList;
import java.util.List;

public interface IFileUpLoadView extends BaseMVPView {
    /**
     * 上传文件成功 阿里云
     */
    void uploadFileByAliyunSuccess();

    /**
     * 上传文件失败 阿里云
     */
    void uploadFileByAliyunFail(String msg);


    /***
     * 获取阿里云上传信息成功
     */
    void getAliyunUpLoadInfoSuccess(FileUpLoadResults results);

    /**
     * 获取阿里云上传信息失败
     *
     * @param msg
     */
    void getAliyunUpLoadInfoFail(String msg);


    /**
     * 上传文件进度
     *
     * @param currentSize
     * @param totalSize
     */
    void uploadFileOnProgress(long currentSize, long totalSize);

    /**
     * 获取上传的视频数据
     *
     * @return
     */
    VideoInfoRequest getVideoInfo();


    /**
     * 开始上传阿里云
     *
     * @param results
     */
    void startUploadAliyun(FileUpLoadResults results);
}
