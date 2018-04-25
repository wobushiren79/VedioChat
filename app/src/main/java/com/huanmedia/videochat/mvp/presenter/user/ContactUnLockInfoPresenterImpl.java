package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ContactUnLockInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.ContactUnLockInfoResults;
import com.huanmedia.videochat.mvp.entity.results.UnLockContactResults;
import com.huanmedia.videochat.mvp.model.user.ContactUnLockInfoModelImpl;
import com.huanmedia.videochat.mvp.view.user.IContactUnLockView;

public class ContactUnLockInfoPresenterImpl extends BaseMVPPresenter<IContactUnLockView, ContactUnLockInfoModelImpl> implements IContactUnLockInfoPresenter {

    public ContactUnLockInfoPresenterImpl(IContactUnLockView mMvpView) {
        super(mMvpView, ContactUnLockInfoModelImpl.class);
    }

    @Override
    public void getContactUnLockInfo() {
        long readManid = mMvpView.getReadManId();
        if (readManid == 0) {
            mMvpView.showToast("没有红人ID");
            return;
        }
        ContactUnLockInfoRequest params = new ContactUnLockInfoRequest();
        params.setOuid((int) readManid);
        mMvpModel.getContactUnLockInfo(params, new DataCallBack<ContactUnLockInfoResults>() {
            @Override
            public void getDataSuccess(ContactUnLockInfoResults data) {
                mMvpView.getContactUnLockInfoSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getContactUnLockInfoFail(msg);
            }
        });
    }

    @Override
    public void unLockContact() {
        long readManid = mMvpView.getReadManId();
        if (readManid == 0) {
            mMvpView.showToast("没有红人ID");
            return;
        }
        ContactUnLockInfoRequest params = new ContactUnLockInfoRequest();
        params.setOuid((int) readManid);
        mMvpModel.unlockContact(params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.unlockSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.unlockFail(msg);
            }
        });
    }
}
