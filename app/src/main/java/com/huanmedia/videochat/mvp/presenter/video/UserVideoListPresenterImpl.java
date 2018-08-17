package com.huanmedia.videochat.mvp.presenter.video;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.UserVideoListRequest;
import com.huanmedia.videochat.mvp.model.video.UserVideoListModelImpl;
import com.huanmedia.videochat.mvp.view.video.IUserVideoListView;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.List;

public class UserVideoListPresenterImpl extends BaseMVPPresenter<IUserVideoListView, UserVideoListModelImpl> implements IUserVideoListPresenter {
    public UserVideoListPresenterImpl(IUserVideoListView mMvpView) {
        super(mMvpView, UserVideoListModelImpl.class);
    }
    
    @Override
    public void getAllVideo() {
        getVideo(0, 0, 100);
    }

    @Override
    public void getOpenVideo() {
        getVideo(1, 0, 100);
    }

    @Override
    public void getSecretVideo() {
        getVideo(2, 0, 100);
    }

    private void getVideo(int type, int status, int limit) {
        if (mMvpView.getContext() == null)
            return;
        UserVideoListRequest params = new UserVideoListRequest();
        params.setLimit(limit);
//        params.setStatus(status);
        params.setType(type);
        mMvpModel.getUserVideoList(mMvpView.getContext(), params, new DataCallBack<List<VideoEntity>>() {

            @Override
            public void getDataSuccess(List<VideoEntity> data) {
                mMvpView.getUserVideoListSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getUserVideoListFail(msg);
            }
        });
    }
}
