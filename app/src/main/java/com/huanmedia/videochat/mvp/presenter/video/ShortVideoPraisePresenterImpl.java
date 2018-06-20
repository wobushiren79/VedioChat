package com.huanmedia.videochat.mvp.presenter.video;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoPraiseRequest;
import com.huanmedia.videochat.mvp.model.video.ShortVideoPraiseModelImpl;
import com.huanmedia.videochat.mvp.view.video.IShortVideoPraiseView;

public class ShortVideoPraisePresenterImpl extends BaseMVPPresenter<IShortVideoPraiseView, ShortVideoPraiseModelImpl> implements IShortVideoPraisePresenter {

    public ShortVideoPraisePresenterImpl(IShortVideoPraiseView mMvpView) {
        super(mMvpView, ShortVideoPraiseModelImpl.class);
    }

    @Override
    public void shortVideoPraise(int videoId) {
        if (mMvpView.getContext() == null)
            return;
        if (videoId == 0) {
            mMvpView.showToast("没有视频ID");
        }
        ShortVideoPraiseRequest params = new ShortVideoPraiseRequest();
        params.setId(videoId);
        mMvpModel.shortVideoPraise(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.shortVideoPraiseSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.shortVideoPraiseFail(msg);
            }
        });
    }
}
