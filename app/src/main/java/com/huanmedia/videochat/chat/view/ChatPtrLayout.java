package com.huanmedia.videochat.chat.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.chat.adapter.ChatAdpater;
import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;
import com.huanmedia.videochat.mvp.entity.results.UserInfoResults;
import com.huanmedia.videochat.mvp.presenter.chat.ChatListPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.chat.ChatReadPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.chat.IChatListPresenter;
import com.huanmedia.videochat.mvp.presenter.chat.IChatReadPresenter;
import com.huanmedia.videochat.mvp.view.chat.IChatListView;
import com.huanmedia.videochat.mvp.view.chat.IChatReadView;

import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class ChatPtrLayout extends PtrLayout implements IChatListView, IChatReadView, PtrLayout.PtrHandleCallBack {

    private int mChatUserId;
    private ChatAdpater mChatAdapter;
    private IChatListPresenter mListPresenter;
    private IChatReadPresenter mReadPresenter;

    public ChatPtrLayout(Context context) {
        this(context, null);
    }

    public ChatPtrLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mListPresenter = new ChatListPresenterImpl(this);
        mReadPresenter = new ChatReadPresenterImpl(this);
        mChatAdapter = new ChatAdpater(getContext());
        setAdapter(mChatAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setCallBack(this);
        getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        this.disablePtr();
        this.enabledOnePtr();

        RxCountDown.delay2(500).subscribe(integer -> {
            getNewChatData();
        });
    }

    /**
     * 设置聊天对象ID
     *
     * @param chatUserId
     */
    public void setChatUserId(int chatUserId) {
        mChatUserId = chatUserId;
    }

    /**
     * 获取最新数据
     */
    public void getNewChatData() {
        if (mChatAdapter.getData().size() == 0) {
            mListPresenter.getNewChatList(0);
        } else {
            mListPresenter.getNewChatList(mChatAdapter.getData().get(mChatAdapter.getData().size() - 1).getId());
        }

    }

    /**
     * 获取列表数据
     *
     * @return
     */
    public List<ChatListResults.Item> getListData() {
        return mChatAdapter.getData();
    }

    /**
     * 设置所有数据已读
     */
    public void setAllMsgRead() {
        for (ChatListResults.Item itemData : getListData()) {
            if (itemData.getHandle_type() == 0)
                itemData.setHandle_type(1);
        }
        mChatAdapter.notifyDataSetChanged();
    }

    //-------------列表数据处理---------------
    @Override
    public void getChatListSuccess(ChatListResults results) {

    }

    @Override
    public void getChatListFail(String msg) {
        setRefreshComplete();
    }

    @Override
    public int getChatUserId() {
        return mChatUserId;
    }

    @Override
    public void setNewChatListData(List<ChatListResults.Item> listData) {
        mChatAdapter.addData(listData);
        getRecyclerView().smoothScrollToPosition(mChatAdapter.getData().size() - 1);
        mReadPresenter.readAllMsg(mChatUserId);
    }

    @Override
    public void setHistoryChatListData(List<ChatListResults.Item> listData) {
        mChatAdapter.addDataInIndex(listData, 0);
        setRefreshComplete();
        getRecyclerView().smoothScrollToPosition(0);

    }

    @Override
    public void setSelfData(UserInfoResults results) {
        mChatAdapter.setSelfInfo(results);
    }

    @Override
    public void setOtherData(UserInfoResults results) {
        mChatAdapter.setOtherInfo(results);
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }

    //----------上下拉刷新------------
    @Override
    public void onLoadMoreBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (mChatAdapter.getData().size() == 0) {
            mListPresenter.getHistoryChatList(0);
        } else {
            mListPresenter.getHistoryChatList(mChatAdapter.getData().get(0).getId());
        }
    }


    //----------阅读状态改变-----------------
    @Override
    public void chatReadSuccess() {

    }

    @Override
    public void chatReadFail(String msg) {

    }

}
