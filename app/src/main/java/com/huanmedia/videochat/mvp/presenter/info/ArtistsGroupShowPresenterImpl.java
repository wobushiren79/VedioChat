package com.huanmedia.videochat.mvp.presenter.info;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.model.info.ArtistsGroupModelImpl;
import com.huanmedia.videochat.mvp.view.info.IArtistsGroupShowView;

public class ArtistsGroupShowPresenterImpl extends BaseMVPPresenter<IArtistsGroupShowView,ArtistsGroupModelImpl> implements IArtistsGroupShowPresenter {

    public ArtistsGroupShowPresenterImpl(IArtistsGroupShowView mMvpView) {
        super(mMvpView, ArtistsGroupModelImpl.class);
    }

    @Override
    public void getArtistsGroupShowData() {
        if(mMvpView.getContext()==null)
            return;
    }
}
