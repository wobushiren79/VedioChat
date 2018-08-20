package com.huanmedia.videochat.mvp.presenter.file;

import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.FileManageRequest;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;
import com.huanmedia.videochat.mvp.model.file.FileManageModelImpl;
import com.huanmedia.videochat.mvp.view.file.IFileManagePayView;
import com.huanmedia.videochat.mvp.view.file.IFileManageCheckView;

public class FileManagePresenterImpl extends BaseMVPPresenter<BaseMVPView, FileManageModelImpl> implements IFileManagePresenter {

    private IFileManagePayView payView;
    private IFileManageCheckView checkView;

    public FileManagePresenterImpl(IFileManagePayView payView, IFileManageCheckView checkView) {
        super(FileManageModelImpl.class);
        this.payView = payView;
        this.checkView = checkView;
    }

    public FileManagePresenterImpl(IFileManagePayView payView) {
        super(FileManageModelImpl.class);
        this.payView = payView;
    }

    public FileManagePresenterImpl(IFileManageCheckView checkView) {
        super(FileManageModelImpl.class);
        this.checkView = checkView;
    }

    @Override
    public void checkHasVideoFile(int fileId) {
        baseCheckHasHandler(fileId, 2);
    }

    @Override
    public void checkHasPhotoFile(int fileId) {
        baseCheckHasHandler(fileId, 1);
    }

    @Override
    public void payVideoFile(int fileId) {
        basePayHandler(fileId, 2);
    }

    @Override
    public void payPhotoFile(int fileId) {
        basePayHandler(fileId, 1);
    }

    private void baseCheckHasHandler(int fileId, int fileType) {
        if (checkView == null || checkView.getContext() == null)
            return;
        FileManageRequest params = new FileManageRequest();
        params.setType(fileType);
        params.setTypeid(fileId);
        mMvpModel.checkHasFile(checkView.getContext(), params, new DataCallBack<FileManageResults>() {

            @Override
            public void getDataSuccess(FileManageResults data) {
                checkView.checkHasFileSuccess(fileId, fileType, data);
            }

            @Override
            public void getDataFail(String msg) {
                checkView.checkHasFileFail(msg);
            }
        });
    }

    private void basePayHandler(int fileId, int fileType) {
        if (payView == null || payView.getContext() == null)
            return;
        FileManageRequest params = new FileManageRequest();
        params.setType(fileType);
        params.setTypeid(fileId);
        mMvpModel.payFile(checkView.getContext(), params, new DataCallBack<FileManageResults>() {

            @Override
            public void getDataSuccess(FileManageResults data) {
                UserManager.getInstance().getCurrentUser().getUserinfo().setCoin(data.getCoin());
                payView.payFileSuccess(fileId,fileType,data);
            }

            @Override
            public void getDataFail(String msg) {
                payView.payFileFail(msg);
            }
        });
    }
}
