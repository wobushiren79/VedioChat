package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.DynamicListRequset;
import com.huanmedia.videochat.mvp.entity.results.DynamicListResults;
import com.huanmedia.videochat.mvp.model.user.DynamicModelImpl;
import com.huanmedia.videochat.mvp.view.user.IDynamicListView;

public class DynamicListPresenterImpl extends BaseMVPPresenter<IDynamicListView, DynamicModelImpl> implements IDynamicListPresenter {
    public DynamicListPresenterImpl(IDynamicListView mMvpView) {
        super(mMvpView, DynamicModelImpl.class);
    }

    @Override
    public void getDynamicList(int userId) {
        if (mMvpView.getContext() == null)
            return;
        DynamicListRequset params = new DynamicListRequset();
        mMvpModel.getDynamicList(params, new DataCallBack<DynamicListResults>() {
            @Override
            public void getDataSuccess(DynamicListResults data) {
                mMvpView.getDynamicListSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getDynamicListFail(msg);
            }
        });
    }
}
