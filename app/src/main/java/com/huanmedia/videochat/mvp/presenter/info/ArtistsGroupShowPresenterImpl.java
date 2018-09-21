package com.huanmedia.videochat.mvp.presenter.info;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ArtistsGroupShowRequest;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupShowResults;
import com.huanmedia.videochat.mvp.model.info.ArtistsGroupModelImpl;
import com.huanmedia.videochat.mvp.view.info.IArtistsGroupShowView;

public class ArtistsGroupShowPresenterImpl extends BaseMVPPresenter<IArtistsGroupShowView, ArtistsGroupModelImpl> implements IArtistsGroupShowPresenter {

    public ArtistsGroupShowPresenterImpl(IArtistsGroupShowView mMvpView) {
        super(mMvpView, ArtistsGroupModelImpl.class);
    }

    @Override
    public void getArtistsGroupShowData() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getArtistsGroupId() == 0) {
            mMvpView.showToast("没有艺人组合ID");
            return;
        }
        ArtistsGroupShowRequest params = new ArtistsGroupShowRequest();
        params.setStuid(mMvpView.getArtistsGroupId());
        mMvpModel.getArtistsGroupShow(mMvpView.getContext(), params, new DataCallBack<ArtistsGroupShowResults>() {

            @Override
            public void getDataSuccess(ArtistsGroupShowResults data) {
                mMvpView.getArtistsGroupShowDataSuccess(data);
                if (data == null || mMvpView == null)
                    return;
                if (data.getBase() != null && data.getBase().getImgurl() != null)
                    mMvpView.setArtistsGroupBackGround(data.getBase().getImgurl());
                if (data.getBase() != null && data.getBase().getTitleimgurla() != null)
                    mMvpView.setArtistsGroupTitle(data.getBase().getTitleimgurla());
                if (data.getItems() != null) {
                    for (ArtistsGroupShowResults.Items item : data.getItems()) {
                        item.getInfo().setGroupName(data.getBase().getName());
                    }
                    mMvpView.setArtistsList(data.getItems());
                }
            }

            @Override
            public void getDataFail(String msg) {
                if (mMvpView == null)
                    return;
                mMvpView.getArtistsGroupShowDataFail(msg);
            }
        });
    }
}
