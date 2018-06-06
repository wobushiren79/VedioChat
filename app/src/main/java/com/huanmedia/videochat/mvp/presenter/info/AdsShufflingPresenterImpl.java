package com.huanmedia.videochat.mvp.presenter.info;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AdsShufflingRequest;
import com.huanmedia.videochat.mvp.entity.results.AdsShufflingResults;
import com.huanmedia.videochat.mvp.model.info.AdsShufflingModelImpl;
import com.huanmedia.videochat.mvp.view.info.IAdsShufflingView;

import java.util.List;

public class AdsShufflingPresenterImpl extends BaseMVPPresenter<IAdsShufflingView, AdsShufflingModelImpl> implements IAdsShufflingPresenter {

    public AdsShufflingPresenterImpl(IAdsShufflingView mMvpView) {
        super(mMvpView, AdsShufflingModelImpl.class);
    }

    @Override
    public void getShufflingAdsInfo() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getShufflingAdsType() == 0) {
            mMvpView.showToast("没有轮播广告类型");
            return;
        }
        AdsShufflingRequest params = new AdsShufflingRequest();
        params.setLocation(mMvpView.getShufflingAdsType());
        mMvpModel.getAdsInfo(mMvpView.getContext(), params, new DataCallBack<List<AdsShufflingResults>>() {


            @Override
            public void getDataSuccess(List<AdsShufflingResults> data) {
                mMvpView.getShufflingAdsSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getShufflingAdsFail(msg);
            }
        });
    }
}
