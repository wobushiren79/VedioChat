package com.huanmedia.videochat.mvp.presenter.file;

import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.base.DataFileCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.model.file.FileUpLoadModelImpl;
import com.huanmedia.videochat.mvp.view.file.IFileUpLoadView;

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
                startUpLoadByAliyun(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.uploadFileFail("上传文件失败：" + msg);
            }
        });
    }

    @Override
    public void startUpLoadByAliyun(FileUpLoadResults results) {
        if (mMvpView.getContext() == null)
            return;
        if (results.getAccessKeyID() == null) {
            mMvpView.showToast("没有文件上传AK");
            return;
        }
        if (results.getAccessKeySecret() == null) {
            mMvpView.showToast("没有文件上传SK");
            return;
        }
        if (results.getBucket() == null) {
            mMvpView.showToast("没有文件上传Bucket");
            return;
        }
        if (results.getFilename() == null) {
            mMvpView.showToast("没有文件上传FileName");
            return;
        }
        if (results.getToken() == null) {
            mMvpView.showToast("没有文件上传Token");
            return;
        }
        mMvpModel.fileUpLoadByAliyun(mMvpView.getContext(), results, new DataFileCallBack<PutObjectResult>() {

            @Override
            public void getDataSuccess(PutObjectResult data) {
                mMvpView.uploadFileFail("上传成功");
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.uploadFileFail("上传文件失败：" + msg);
            }

            @Override
            public void getDataOnProgress(long currentSize, long totalSize) {
                mMvpView.uploadFileOnProgress(currentSize, totalSize);
            }
        });
    }
}
