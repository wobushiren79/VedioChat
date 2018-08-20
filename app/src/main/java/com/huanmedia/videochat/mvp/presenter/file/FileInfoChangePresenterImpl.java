package com.huanmedia.videochat.mvp.presenter.file;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileInfoChangeRequest;
import com.huanmedia.videochat.mvp.model.file.FileInfoChangeModelImpl;
import com.huanmedia.videochat.mvp.view.file.IFileInfoChangeView;

public class FileInfoChangePresenterImpl extends BaseMVPPresenter<IFileInfoChangeView, FileInfoChangeModelImpl> implements com.huanmedia.videochat.mvp.presenter.file.IFileInfoChangePresenter {

    public FileInfoChangePresenterImpl(IFileInfoChangeView mMvpView) {
        super(mMvpView, FileInfoChangeModelImpl.class);
    }

    @Override
    public void changePhotoPrice(int fileId, int changePrice) {
        baseChange(fileId, 1, changePrice, 0, null);
    }

    @Override
    public void changeVideoPrice(int fileId, int changePrice) {
        baseChange(fileId, 2, changePrice, 0, null);
    }

    @Override
    public void changePhotoType(int fileId, int changeType) {
        baseChange(fileId, 1, 0, changeType, null);
    }

    @Override
    public void changeVideoType(int fileId, int changeType) {
        baseChange(fileId, 2, 0, changeType, null);
    }

    @Override
    public void changePhotoTag(int fileId, String changeTag) {
        baseChange(fileId, 1, 0, 0, changeTag);
    }

    @Override
    public void changeVideoTag(int fileId, String changeTag) {
        baseChange(fileId, 2, 0, 0, changeTag);
    }


    private void baseChange(int fileId, int fileType, int changePrice, int changeType, String changeTag) {
        if (mMvpView.getContext() == null)
            return;
        if (fileId == 0) {
            mMvpView.showToast("没有文件ID");
            return;
        }
        FileInfoChangeRequest params = new FileInfoChangeRequest();
        params.setType(fileType);
        params.setId(fileId);
        if (changeType != 0)
            params.setPlevel(changeType);
        if (changePrice != 0)
            params.setVcoin(changePrice);
        if (changeTag != null && changeTag.length() != 0)
            params.setTag(changeTag);
        mMvpModel.changeFileInfo(mMvpView.getContext(), params, null, new DataCallBack() {

            @Override
            public void getDataSuccess(Object data) {
                mMvpView.changeFileInfoSuccess(fileId);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.changeFileInfoFail(msg);
            }
        });
    }


}
