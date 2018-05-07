package com.huanmedia.videochat.mvp.presenter.chat;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.TalkRoomListRequest;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;
import com.huanmedia.videochat.mvp.model.chat.TalkRoomListModelImpl;
import com.huanmedia.videochat.mvp.view.chat.ITalkRoomListView;

public class TalkRoomListPresenterImpl extends BaseMVPPresenter<ITalkRoomListView, TalkRoomListModelImpl> implements ITalkRoomListPresenter {

    public TalkRoomListPresenterImpl(ITalkRoomListView mMvpView) {
        super(mMvpView, TalkRoomListModelImpl.class);
    }

    @Override
    public void getTalkRoomList() {
        if (mMvpView.getPtrLayout() == null) {
            mMvpView.showToast("没有下拉控件");
        }
        mMvpView.getPtrLayout().setRefresh();
        TalkRoomListRequest params = new TalkRoomListRequest();
        params.setPage(mMvpView.getPtrLayout().getPage());
        params.setPagesize(mMvpView.getPtrLayout().getPageSize());
        mMvpModel.getTalkRoomList(mMvpView.getContext(), params, new DataCallBack<TalkRoomListResults>() {

            @Override
            public void getDataSuccess(TalkRoomListResults data) {
                mMvpView.getPtrLayout().setListDataResults(data, data.getItems());
                mMvpView.getTalkRoomListSuccess(data.getItems());
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getPtrLayout().setRefreshComplete();
                mMvpView.getTalkRoomListFail(msg);
            }
        });
    }
}
