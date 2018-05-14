package com.huanmedia.videochat.mvp.presenter.info;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ShufflingAdsRequest;
import com.huanmedia.videochat.mvp.entity.results.ShufflingAdsResults;
import com.huanmedia.videochat.mvp.model.info.ShufflingAdsModelImpl;
import com.huanmedia.videochat.mvp.view.info.IShufflingAdsView;

import java.util.List;

public class ShufflingAdsPresenterImpl extends BaseMVPPresenter<IShufflingAdsView, ShufflingAdsModelImpl> implements IShufflingAdsPresenter {

    public ShufflingAdsPresenterImpl(IShufflingAdsView mMvpView) {
        super(mMvpView, ShufflingAdsModelImpl.class);
    }

    @Override
    public void getShufflingAdsInfo() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getShufflingAdsType() == 0) {
            mMvpView.showToast("没有轮播广告类型");
            return;
        }
        ShufflingAdsRequest params = new ShufflingAdsRequest();
        params.setLocation(mMvpView.getShufflingAdsType());
        mMvpModel.getAdsInfo(mMvpView.getContext(), params, new DataCallBack<List<ShufflingAdsResults>>() {


            @Override
            public void getDataSuccess(List<ShufflingAdsResults> data) {
                mMvpView.getShufflingAdsSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getShufflingAdsFail(msg);
            }
        });
    }
}
