package com.huanmedia.videochat.mvp.presenter.photo;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.PhotoListRequest;
import com.huanmedia.videochat.mvp.model.photo.PhotoListModelImpl;
import com.huanmedia.videochat.mvp.view.photo.IPhotoListView;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import java.util.List;

public class PhotoListPresenterImpl extends BaseMVPPresenter<IPhotoListView, PhotoListModelImpl> implements IPhotoListPresenter {

    public PhotoListPresenterImpl(IPhotoListView mMvpView) {
        super(mMvpView, PhotoListModelImpl.class);
    }

    @Override
    public void getAllPhoto() {
        getBasePhotoList(0, 0, 100);
    }

    @Override
    public void getOpenPhoto() {
        getBasePhotoList(1, 0, 100);
    }

    @Override
    public void getSecretPhoto() {
        getBasePhotoList(2, 0, 100);
    }

    private void getBasePhotoList(int photoType, int photoStatus, int limit) {
        if (mMvpView.getContext() == null)
            return;
        PhotoListRequest params = new PhotoListRequest();
        params.setLimit(limit);
//        params.setStatus(photoStatus);
        params.setType(photoType);
        mMvpModel.getPhotoList(mMvpView.getContext(), params, new DataCallBack<List<PhotosEntity>>() {

            @Override
            public void getDataSuccess(List<PhotosEntity> data) {
                mMvpView.getPhotoListSuccess(data);
                if (data == null)
                    return;
                if (photoType == 0) {
                    mMvpView.setAllPhotoList(data);
                } else if (photoType == 1) {
                    mMvpView.setOpenPhotoList(data);
                } else if (photoType == 2) {
                    mMvpView.setSecretList(data);
                }
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getPhotoListFail(msg);
            }
        });

    }
}
