package com.huanmedia.videochat.mvp.presenter.chat;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ChatListRequest;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;
import com.huanmedia.videochat.mvp.model.chat.ChatModelImpl;
import com.huanmedia.videochat.mvp.view.chat.IChatListView;

public class ChatListPresenterImpl extends BaseMVPPresenter<IChatListView, ChatModelImpl> implements IChatListPresenter {


    public ChatListPresenterImpl(IChatListView mMvpView) {
        super(mMvpView, ChatModelImpl.class);
    }

    @Override
    public void getHistoryChatList(int msgId) {
        if (mMvpView.getContext() == null)
            return;
        if (msgId == 0) {
            mMvpView.showToast("没有信息ID");
            return;
        }
        if (mMvpView.getChatUserId() == 0) {
            mMvpView.showToast("没有聊天对象ID");
            return;
        }
        ChatListRequest params = new ChatListRequest();
        params.setMsgtype(2);
        params.setOuid(mMvpView.getChatUserId());
        params.setOldid(msgId);
        mMvpModel.getChatList(mMvpView.getContext(), params, new DataCallBack<ChatListResults>() {

            @Override
            public void getDataSuccess(ChatListResults data) {
                mMvpView.getChatListSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getChatListFail(msg);
            }

        });

    }

    @Override
    public void getNewChatList(int chatId) {

    }

    @Override
    public void getDefChatList() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getChatUserId() == 0) {
            mMvpView.showToast("没有聊天对象ID");
            return;
        }
        ChatListRequest params = new ChatListRequest();
        params.setMsgtype(2);
        params.setOuid(mMvpView.getChatUserId());
        mMvpModel.getChatList(mMvpView.getContext(), params, new DataCallBack<ChatListResults>() {

            @Override
            public void getDataSuccess(ChatListResults data) {
                mMvpView.getChatListSuccess(data);
                if (data.getItems() != null)
                    mMvpView.setDefChatListData(data.getItems());
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getChatListFail(msg);
            }

        });

    }
}
