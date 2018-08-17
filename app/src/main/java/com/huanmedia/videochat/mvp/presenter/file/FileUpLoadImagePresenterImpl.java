package com.huanmedia.videochat.mvp.presenter.file;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.UploadImagesRequest;
import com.huanmedia.videochat.mvp.model.file.FileUpLoadModelImpl;
import com.huanmedia.videochat.mvp.view.file.IFileUpLoadImageView;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import java.util.ArrayList;
import java.util.List;

public class FileUpLoadImagePresenterImpl extends BaseMVPPresenter<IFileUpLoadImageView, FileUpLoadModelImpl> implements IFileUpLoadImagePresenter {

    public FileUpLoadImagePresenterImpl(IFileUpLoadImageView mMvpView) {
        super(mMvpView, FileUpLoadModelImpl.class);
    }


    @Override
    public void uploadImage(UploadImagesRequest params, List<String> images) {
        if (mMvpView.getContext() == null)
            return;
        if (images == null || images.size() == 0) {
            mMvpView.showToast("没有需要上传的照片");
            return;
        }
        mMvpModel.imageUpLoad(mMvpView.getContext(), params, images, new DataCallBack<ArrayList<PhotosEntity>>() {
            @Override
            public void getDataSuccess(ArrayList<PhotosEntity> data) {
                mMvpView.uploadImageSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.uploadImageFail(msg);
            }
        });
    }
}
