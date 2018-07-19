package com.huanmedia.videochat.mvp.presenter.info;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AdsLuanchRequest;
import com.huanmedia.videochat.mvp.entity.results.AdsLuanchResults;
import com.huanmedia.videochat.mvp.model.info.AdsLuanchModelImpl;
import com.huanmedia.videochat.mvp.view.info.IAdsLuanchView;

public class AdsLuanchPresenterImpl extends BaseMVPPresenter<IAdsLuanchView, AdsLuanchModelImpl> implements IAdsLuanchPresenter {

    public AdsLuanchPresenterImpl(IAdsLuanchView mMvpView) {
        super(mMvpView, AdsLuanchModelImpl.class);
    }


    @Override
    public void getAdsLuanchInfo() {
        if (mMvpView.getContext() == null)
            return;
        AdsLuanchRequest params = new AdsLuanchRequest();
        mMvpModel.getAdsInfo(mMvpView.getContext(), params, new DataCallBack<AdsLuanchResults>() {

            @Override
            public void getDataSuccess(AdsLuanchResults data) {
                mMvpView.getLuanchInfoAdsSuccess(data);
                mMvpView.setLuanchImage(data.getImgurl());
                if (data.getJmpurl() != null && data.getJmpurl().length() != 0 && !data.getJmpurl().equals("#")) {
                    mMvpView.setJumpUrl(data.getJmpurl());
                }
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getLuanchInfoAdsFail(msg);
            }
        });
    }
}
