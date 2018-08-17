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
        uploadUserVideoInfoBase(0, null, 1);
    }

    @Override
    public void uploadUserVideoInfoBySercet(int price, String tag) {
        uploadUserVideoInfoBase(price, tag, 2);
    }

    private void uploadUserVideoInfoBase(int price, String tag, int type) {
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
        params.setPlevel(type);
        if (price != 0)
            params.setVcoin(price);
        if (tag != null)
            params.setTag(tag);

        List<String> images = new ArrayList<>();
        images.add(videoIcon);
        mMvpModel.uploadUserVideo(mMvpView.getContext(), params, images, new DataCallBack<UserVideoDataResults>() {
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
