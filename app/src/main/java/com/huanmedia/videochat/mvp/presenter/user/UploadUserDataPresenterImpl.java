package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.UploadUserDataRequest;
import com.huanmedia.videochat.mvp.model.user.UploadUserDataModelImpl;
import com.huanmedia.videochat.mvp.view.user.IUploadUserDataView;

public class UploadUserDataPresenterImpl extends BaseMVPPresenter<IUploadUserDataView, UploadUserDataModelImpl> implements IUploadUserDataPresenter {


    public UploadUserDataPresenterImpl(IUploadUserDataView mMvpView) {
        super(mMvpView, UploadUserDataModelImpl.class);
    }

    @Override
    public void uploadUserData() {
        String lat = mMvpView.getLat();
        String lng = mMvpView.getLng();
        String address = mMvpView.getAddress();
        if (lat == null || lng == null) {
            mMvpView.showToast("获取经纬度失败");
            return;
        }
        UploadUserDataRequest params = new UploadUserDataRequest();
        params.setLat(lat);
        params.setLng(lng);
        params.setAddress(address);
        mMvpModel.submitEvaluation(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.uploadUserDataSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.uploadUserDataFail(msg);
            }
        });
    }
}
