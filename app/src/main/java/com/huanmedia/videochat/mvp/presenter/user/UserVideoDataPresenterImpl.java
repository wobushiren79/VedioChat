package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.ilibray.utils.DevUtils;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.UserVideoDataRequest;
import com.huanmedia.videochat.mvp.entity.results.UserVideoDataResults;
import com.huanmedia.videochat.mvp.model.user.UserVideoDataModelImpl;
import com.huanmedia.videochat.mvp.view.user.IUserVideoDataView;

import java.util.ArrayList;
import java.util.List;

public class UserVideoDataPresenterImpl extends BaseMVPPresenter<IUserVideoDataView, UserVideoDataModelImpl> implements IUserVideoDataPresenter {

    public UserVideoDataPresenterImpl(IUserVideoDataView mMvpView) {
        super(mMvpView, UserVideoDataModelImpl.class);
    }

    @Override
    public void uploadUserVideoInfo() {
//        if (mMvpView.getContext() == null)
////            return;

        String videoName = mMvpView.getUpLoadVideoName();
        String videoIcon = mMvpView.getUpLoadVideoIcon();
        if (videoName == null) {
            mMvpView.showToast("没有视频文件名");
            return;
        }
        if (videoIcon == null) {
            mMvpView.showToast("没有视频封面");
            return;
        }
        UserVideoDataRequest params = new UserVideoDataRequest();
        params.setBindfilename(videoName);
        params.setFullname(videoName);
        List<String> images = new ArrayList<>();
        images.add(videoIcon);
        params.setImg(images);
        mMvpModel.uploadUserVideo(mMvpView.getContext(), params, new DataCallBack<UserVideoDataResults>() {
            @Override
            public void getDataSuccess(UserVideoDataResults data) {
                mMvpView.uploadUserVideoSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.uploadUserVideoFail(msg);
            }
        });
    }

    @Override
    public void deleteUserVideoInfo() {
        List<String> ids = mMvpView.getDeleteVideoIds();
        if (ids == null || ids.size() == 0) {
            mMvpView.showToast("还没有选择需要删除的视频");
            return;
        }
        UserVideoDataRequest params = new UserVideoDataRequest();
        String idsStr = DevUtils.ListToStrByMark(ids, ",");
        params.setIds(idsStr);

        mMvpModel.deleteUserVideo(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.deleteUserVideoSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.deleteUserVideoFail(msg);
            }
        });
    }


}
