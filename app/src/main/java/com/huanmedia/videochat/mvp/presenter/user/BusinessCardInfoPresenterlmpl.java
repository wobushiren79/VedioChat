package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.BusinessCardInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;
import com.huanmedia.videochat.mvp.model.user.BusinessCardInfoModelImpl;
import com.huanmedia.videochat.mvp.view.user.IBusinessCardInfoView;

public class BusinessCardInfoPresenterlmpl extends BaseMVPPresenter<IBusinessCardInfoView, BusinessCardInfoModelImpl> implements IBusinessCardInfoPresenter {


    public BusinessCardInfoPresenterlmpl(IBusinessCardInfoView mMvpView) {
        super(mMvpView, BusinessCardInfoModelImpl.class);
    }

    @Override
    public void getBusinessCardInfo(int uid, int plevel) {
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
        params.setPlevel(plevel);
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
