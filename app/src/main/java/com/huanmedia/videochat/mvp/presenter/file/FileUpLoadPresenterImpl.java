package com.huanmedia.videochat.mvp.presenter.file;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.base.DataFileCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.request.VideoInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.model.file.FileUpLoadModelImpl;
import com.huanmedia.videochat.mvp.view.file.IFileUpLoadView;
import com.huanmedia.videochat.repository.entity.PhpotsEntity;

import java.util.ArrayList;
import java.util.List;

public class FileUpLoadPresenterImpl extends BaseMVPPresenter<IFileUpLoadView, FileUpLoadModelImpl> implements IFileUpLoadPresenter {


    public FileUpLoadPresenterImpl(IFileUpLoadView mMvpView) {
        super(mMvpView, FileUpLoadModelImpl.class);
    }

    @Override
    public void getAliyunUpLoadInfo() {
        if (mMvpView.getContext() == null)
            return;
        FileUpLoadRequest params = new FileUpLoadRequest();
        mMvpModel.getAliyunUploadInfo(mMvpView.getContext(), params, new DataCallBack<FileUpLoadResults>() {

            @Override
            public void getDataSuccess(FileUpLoadResults data) {
                mMvpView.getAliyunUpLoadInfoSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getAliyunUpLoadInfoFail("上传文件失败：" + msg);
            }
        });
    }

    @Override
    public OSSAsyncTask startUpLoadByAliyun(FileUpLoadResults results) {
        if (mMvpView.getContext() == null) {
            mMvpView.showToast("没有上下文对象");
            return null;
        }
        VideoInfoRequest videoInfo = mMvpView.getVideoInfo();
        if (videoInfo == null) {
            mMvpView.showToast("选择上传视频失败");
            return null;
        }
        if (results == null) {
            mMvpView.showToast("获取上传信息失败");
            return null;
        }
//        if (videoInfo.getDuration() == -1) {
//            mMvpView.showToast("视频无效 没有视频时间");
//            return;
//        }
//        if (videoInfo.getDuration() > 60000) {
//            mMvpView.showToast("视频时长不能超过60秒");
//            return null;
//        }
        if (videoInfo.getSize() > 30000000) {
            mMvpView.showToast("视频大小不能超过30M");
            return null;
        }
        if (videoInfo.getVideoPath() == null) {
            mMvpView.showToast("没有上传文件地址");
            return null;
        }

        if (results.getAccessKeyID() == null) {
            mMvpView.showToast("没有文件上传AK");
            return null;
        }
        if (results.getAccessKeySecret() == null) {
            mMvpView.showToast("没有文件上传SK");
            return null;
        }
        if (results.getBucket() == null) {
            mMvpView.showToast("没有文件上传Bucket");
            return null;
        }
        if (results.getFilename() == null) {
            mMvpView.showToast("没有文件上传FileName");
            return null;
        }
        if (results.getToken() == null) {
            mMvpView.showToast("没有文件上传Token");
            return null;
        }
        results.setFilePath(videoInfo.getVideoPath());
        mMvpView.startUploadAliyun(results);
        return mMvpModel.fileUpLoadByAliyun(mMvpView.getContext(), results, new DataFileCallBack<PutObjectResult>() {

            @Override
            public void getDataSuccess(PutObjectResult data) {
                mMvpView.uploadFileByAliyunSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.uploadFileByAliyunFail("上传文件失败：" + msg);
            }

            @Override
            public void getDataOnProgress(long currentSize, long totalSize) {
                mMvpView.uploadFileOnProgress(currentSize, totalSize);
            }
        });

    }


}
