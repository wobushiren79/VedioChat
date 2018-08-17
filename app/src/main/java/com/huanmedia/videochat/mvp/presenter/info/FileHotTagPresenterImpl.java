package com.huanmedia.videochat.mvp.presenter.info;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.results.FileHotTagResults;
import com.huanmedia.videochat.mvp.model.info.FileHotTagModelImpl;
import com.huanmedia.videochat.mvp.view.file.IFileHotTagView;

import java.util.ArrayList;
import java.util.List;

public class FileHotTagPresenterImpl extends BaseMVPPresenter<IFileHotTagView, FileHotTagModelImpl> implements IFileHotTagPresenter {

    public FileHotTagPresenterImpl(IFileHotTagView mMvpView) {
        super(mMvpView, FileHotTagModelImpl.class);
    }

    @Override
    public void getFileHotTagList() {
        if (mMvpView.getContext() == null)
            return;
        mMvpModel.getHotTagList(mMvpView.getContext(), new DataCallBack<List<FileHotTagResults>>() {

            @Override
            public void getDataSuccess(List<FileHotTagResults> data) {
                mMvpView.getFileHotTagListSuccess(data);
                if (data != null) {
                    List<String> listTagStr = new ArrayList<>();
                    for (FileHotTagResults itemData : data) {
                        listTagStr.add(itemData.getName());
                    }
                    mMvpView.setFileHotTagList(listTagStr);
                }
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getFileHotTagListFail(msg);
            }
        });
    }
}
