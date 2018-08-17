package com.huanmedia.videochat.mvp.presenter.photo;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.base.DataFileCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileInfoChangeRequest;
import com.huanmedia.videochat.mvp.model.file.FileInfoChangeModelImpl;
import com.huanmedia.videochat.mvp.view.photo.IPhotoInfoChangeView;

public class PhotoInfoChangePresenterImpl extends BaseMVPPresenter<IPhotoInfoChangeView, FileInfoChangeModelImpl> implements IPhotoInfoChangePresenter {

    public PhotoInfoChangePresenterImpl(IPhotoInfoChangeView mMvpView) {
        super(mMvpView, FileInfoChangeModelImpl.class);
    }

    @Override
    public void changePrice(int fileId, int changePrice) {
        baseChange(fileId, changePrice, 0, null);
    }

    @Override
    public void changeType(int fileId, int changeType) {
        baseChange(fileId, 0, changeType, null);
    }

    @Override
    public void changeTag(int fileId, String changeTag) {
        baseChange(fileId, 0, 0, changeTag);
    }

    private void baseChange(int fileId, int changePrice, int changeType, String changeTag) {
        if (mMvpView.getContext() == null)
            return;
        if (fileId == 0) {
            mMvpView.showToast("没有文件ID");
            return;
        }
        FileInfoChangeRequest params = new FileInfoChangeRequest();
        params.setId(fileId);
        if (changeType != 0)
            params.setPlevel(changeType);
        if (changePrice != 0)
            params.setVcoin(changePrice);
        if (changeTag != null && changeTag.length() != 0)
            params.setTag(changeTag);
        mMvpModel.changeImageInfo(mMvpView.getContext(), params, null, new DataCallBack() {

            @Override
            public void getDataSuccess(Object data) {
                mMvpView.changePhotoInfoSuccess(fileId);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.changePhotoInfoFail(msg);
            }
        });
    }
}
