package com.huanmedia.videochat.mvp.presenter.chat;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ChatSendRequest;
import com.huanmedia.videochat.mvp.model.chat.ChatModelImpl;
import com.huanmedia.videochat.mvp.view.chat.IChatSendView;

public class ChatSendPresenterImpl extends BaseMVPPresenter<IChatSendView, ChatModelImpl> implements IChatSendPresenter {
    public ChatSendPresenterImpl(IChatSendView mMvpView) {
        super(mMvpView, ChatModelImpl.class);
    }

    @Override
    public void chatSend(int chatUserId) {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getChatSendMsg() == null || mMvpView.getChatSendMsg().length() == 0) {
            mMvpView.showToast("还没有输入聊天信息");
            return;
        }
        if (chatUserId == 0) {
            mMvpView.showToast("没有聊天对象ID");
            return;
        }
        ChatSendRequest params = new ChatSendRequest();
        params.setMessage(mMvpView.getChatSendMsg());
        params.setMstype(2);
        params.setTouid(chatUserId);
        mMvpModel.chatSend(mMvpView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.chatSendSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.chatSendFail(msg);
            }
        });
    }
}
