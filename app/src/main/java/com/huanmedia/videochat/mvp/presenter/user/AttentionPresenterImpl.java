package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AttentionRequest;
import com.huanmedia.videochat.mvp.entity.results.AttentionResults;
import com.huanmedia.videochat.mvp.model.user.AttentionModelImpl;
import com.huanmedia.videochat.mvp.view.user.IAttentionView;

public class AttentionPresenterImpl extends BaseMVPPresenter<IAttentionView, AttentionModelImpl> implements IAttentionPresenter {

    public AttentionPresenterImpl(IAttentionView mMvpView) {
        super(mMvpView, AttentionModelImpl.class);
    }

    @Override
    public void attentionUser(int userId, int flag) {
        if (mMvpView.getContext() == null)
            return;
        AttentionRequest params = new AttentionRequest();
        params.setId(userId);
        params.setFlag(flag);
        mMvpModel.attentionUser(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.attentionUserSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.attentionUserFail(msg);
            }
        });
    }
}
