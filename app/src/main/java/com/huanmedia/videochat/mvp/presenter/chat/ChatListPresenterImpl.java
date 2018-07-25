package com.huanmedia.videochat.mvp.presenter.chat;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ChatListRequest;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;
import com.huanmedia.videochat.mvp.model.chat.ChatModelImpl;
import com.huanmedia.videochat.mvp.view.chat.IChatListView;

import java.util.Collections;

public class ChatListPresenterImpl extends BaseMVPPresenter<IChatListView, ChatModelImpl> implements IChatListPresenter {


    public ChatListPresenterImpl(IChatListView mMvpView) {
        super(mMvpView, ChatModelImpl.class);
    }

    private boolean isRefreshNewData = false;

    @Override
    public void getHistoryChatList(int msgId) {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getChatUserId() == 0) {
            mMvpView.showToast("没有聊天对象ID");
            return;
        }
        ChatListRequest params = new ChatListRequest();
        params.setMsgtype(2);
        params.setOuid(mMvpView.getChatUserId());
        if (msgId != 0) {
            params.setOldid(msgId);
        }
        mMvpModel.getChatList(mMvpView.getContext(), params, new DataCallBack<ChatListResults>() {

            @Override
            public void getDataSuccess(ChatListResults data) {
                mMvpView.getChatListSuccess(data);
                if (data.getMyinfo() != null)
                    mMvpView.setSelfData(data.getMyinfo());
                if (data.getUinfo() != null)
                    mMvpView.setOtherData(data.getUinfo());
                if (data.getItems() != null) {
                    Collections.sort(data.getItems());
                    mMvpView.setHistoryChatListData(data.getItems());
                }
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getChatListFail(msg);
            }

        });

    }

    @Override
    public void getNewChatList(int msgId) {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getChatUserId() == 0) {
            mMvpView.showToast("没有聊天对象ID");
            return;
        }
        if (isRefreshNewData)
            return;
        isRefreshNewData = true;
        ChatListRequest params = new ChatListRequest();
        params.setMsgtype(2);
        params.setOuid(mMvpView.getChatUserId());
        if (msgId != 0) {
            params.setNewid(msgId);
        }
        mMvpModel.getChatList(mMvpView.getContext(), params, new DataCallBack<ChatListResults>() {

            @Override
            public void getDataSuccess(ChatListResults data) {
                mMvpView.getChatListSuccess(data);
                if (data.getMyinfo() != null)
                    mMvpView.setSelfData(data.getMyinfo());
                if (data.getUinfo() != null)
                    mMvpView.setOtherData(data.getUinfo());
                if (data.getItems() != null) {
                    Collections.sort(data.getItems());
                    mMvpView.setNewChatListData(data.getItems());
                }
                isRefreshNewData = false;
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getChatListFail(msg);
                isRefreshNewData = false;
            }

        });
    }

}
