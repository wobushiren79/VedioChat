package com.huanmedia.videochat.mvp.presenter.video;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoListRequest;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoListResults;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoResults;
import com.huanmedia.videochat.mvp.model.video.ShortVideoListModelImpl;
import com.huanmedia.videochat.mvp.view.video.IShortVideoListView;

import java.util.List;

public class ShortVideoListPresenterImpl extends BaseMVPPresenter<IShortVideoListView, ShortVideoListModelImpl> implements IShortVideoListPresenter {

    public ShortVideoListPresenterImpl(IShortVideoListView mMvpView) {
        super(mMvpView, ShortVideoListModelImpl.class);
    }

    @Override
    public void getShortVideoList() {
        if (mMvpView.getContext() == null)
            return;
        ShortVideoListRequest params = new ShortVideoListRequest();
        params.setForce(mMvpView.getForceForShortVideo());
        params.setPage(mMvpView.getPageForShortVideo());
        params.setPagesize(mMvpView.getPageSizeForShortVideo());
        mMvpModel.getShortVideoList(mMvpView.getContext(), params, new DataCallBack<ShortVideoListResults>() {
            @Override
            public void getDataSuccess(ShortVideoListResults data) {
                if(mMvpView==null)
                    return;
                mMvpView.getShortVideoListSuccess(data);
                mMvpView.getPtrLayoutForShortVideo().setListDataResults(data, data.getItems());
            }

            @Override
            public void getDataFail(String msg) {
                if(mMvpView==null)
                    return;
                mMvpView.getPtrLayoutForShortVideo().setRefreshComplete();
                mMvpView.getShortVideoListFail(msg);
            }
        });
    }
}
