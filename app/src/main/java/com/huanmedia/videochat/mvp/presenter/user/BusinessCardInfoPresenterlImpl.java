package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.BusinessCardInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;
import com.huanmedia.videochat.mvp.model.user.BusinessCardInfoModelImpl;
import com.huanmedia.videochat.mvp.view.user.IBusinessCardInfoView;

public class BusinessCardInfoPresenterlImpl extends BaseMVPPresenter<IBusinessCardInfoView, BusinessCardInfoModelImpl> implements IBusinessCardInfoPresenterl {


    public BusinessCardInfoPresenterlImpl(IBusinessCardInfoView mMvpView) {
        super(mMvpView, BusinessCardInfoModelImpl.class);
    }

    @Override
    public void getBusinessCardInfo(int uid) {
        if (uid == 0) {
            mMvpView.showToast("没有用户ID");
            return;
        }
        if (mMvpView.getContext() == null) {
            mMvpView.showToast("没有上下文对象");
            return;
        }
        BusinessCardInfoRequest params = new BusinessCardInfoRequest();
        params.setUid(uid);
        mMvpModel.getContactUnLockInfo(mMvpView.getContext(), params, new DataCallBack<BusinessCardInfoResults>() {

            @Override
            public void getDataSuccess(BusinessCardInfoResults data) {
                mMvpView.getBusinessCardInfoSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getBusinessCardInfoFail(msg);
            }
        });
    }


}
