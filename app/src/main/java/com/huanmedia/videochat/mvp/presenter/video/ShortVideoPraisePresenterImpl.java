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
        shortVideoHandler(videoId, 1);
    }

    @Override
    public void shortVideoCancelPraise(int videoId) {
        shortVideoHandler(videoId, 0);
    }


    private void shortVideoHandler(int videoId, int flag) {
        if (mMvpView.getContext() == null)
            return;
        if (videoId == 0) {
            mMvpView.showToast("没有视频ID");
        }
        ShortVideoPraiseRequest params = new ShortVideoPraiseRequest();
        params.setId(videoId);
        params.setFlag(flag);
        mMvpModel.shortVideoPraise(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                if (flag == 1) {
                    mMvpView.shortVideoPraiseSuccess();
                } else {
                    mMvpView.shortVideoCancelPraiseSuccess();
                }
            }

            @Override
            public void getDataFail(String msg) {
                if (flag == 1) {
                    mMvpView.shortVideoPraiseFail(msg);
                } else {
                    mMvpView.shortVideoCanelPraiseFail(msg);
                }

            }
        });
    }
}
