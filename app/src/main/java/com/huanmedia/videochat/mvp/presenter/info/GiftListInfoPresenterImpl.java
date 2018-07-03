package com.huanmedia.videochat.mvp.presenter.info;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.model.info.GiftListInfoModelImpl;
import com.huanmedia.videochat.mvp.view.info.IGiftListInfoView;
import com.huanmedia.videochat.repository.entity.GiftEntity;

import java.util.List;

public class GiftListInfoPresenterImpl extends BaseMVPPresenter<IGiftListInfoView, GiftListInfoModelImpl> implements IGiftListInfoPresenter {

    public GiftListInfoPresenterImpl(IGiftListInfoView mMvpView) {
        super(mMvpView, GiftListInfoModelImpl.class);
    }

    @Override
    public void getGiftListInfo() {
        if (mMvpView.getContext() == null)
            return;
        mMvpModel.getGiftList(mMvpView.getContext(), new DataCallBack<List<GiftEntity>>() {

            @Override
            public void getDataSuccess(List<GiftEntity> data) {
                mMvpView.getGiftListSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getGiftListFail(msg);
            }
        });
    }
}
