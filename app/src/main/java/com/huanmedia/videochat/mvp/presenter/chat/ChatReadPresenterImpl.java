package com.huanmedia.videochat.mvp.presenter.chat;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ChatReadRequest;
import com.huanmedia.videochat.mvp.model.chat.ChatReadModelImpl;
import com.huanmedia.videochat.mvp.view.chat.IChatReadView;

import java.util.List;

public class ChatReadPresenterImpl extends BaseMVPPresenter<IChatReadView, ChatReadModelImpl> implements IChatReadPresenter {

    public ChatReadPresenterImpl(IChatReadView mMvpView) {
        super(mMvpView, ChatReadModelImpl.class);
    }


    @Override
    public void readAllMsg(int chatUserId) {
        if (mMvpView.getContext() == null)
            return;
        if(chatUserId==0){
            mMvpView.showToast("没有聊天对象ID");
            return;
        }

        ChatReadRequest params = new ChatReadRequest();
        params.setMsgtype(2);
        params.setFromuid(chatUserId);
        mMvpModel.chatRead(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.chatReadSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.chatReadFail(msg);
            }
        });
    }

    @Override
    public void readMsg(int chatUserId, List<Integer> msgIds) {

    }
}
