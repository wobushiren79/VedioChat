package com.huanmedia.videochat.mvp.presenter.info;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupResults;
import com.huanmedia.videochat.mvp.model.info.ArtistsGroupModelImpl;
import com.huanmedia.videochat.mvp.view.info.IArtistsGroupListView;

import java.util.List;

public class ArtistsGroupListPresenterImpl extends BaseMVPPresenter<IArtistsGroupListView, ArtistsGroupModelImpl> implements IArtistsGroupListPresenter {

    public ArtistsGroupListPresenterImpl(IArtistsGroupListView mMvpView) {
        super(mMvpView, ArtistsGroupModelImpl.class);
    }

    @Override
    public void getArtistsGroupList() {
        if (mMvpView.getContext() == null)
            return;
        mMvpModel.getArtistsGroupList(mMvpView.getContext(), new DataCallBack<List<ArtistsGroupResults>>() {

            @Override
            public void getDataSuccess(List<ArtistsGroupResults> data) {
                mMvpView.getArtistsGroupListSuccess(data);
                if (data != null)
                    mMvpView.showArtistsGroupList(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getArtistsGroupListFail(msg);
            }
        });

    }
}
