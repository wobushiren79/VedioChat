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
import com.huanmedia.videochat.mvp.presenter.chat.ChatListPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.chat.IChatListPresenter;
import com.huanmedia.videochat.mvp.view.chat.IChatListView;

import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class ChatPtrLayout extends PtrLayout implements IChatListView, PtrLayout.PtrHandleCallBack {

    private int mChatUserId;
    private ChatAdpater mChatAdapter;
    private IChatListPresenter mListPresenter;

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
        mChatAdapter = new ChatAdpater(getContext());
        setAdapter(mChatAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setCallBack(this);
        getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        this.disablePtr();
        this.enabledOnePtr();

        RxCountDown.delay2(500).subscribe(integer -> {
            setRefresh();
//            mListPresenter.getDefChatList();
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

    //-------------列表数据处理---------------
    @Override
    public void getChatListSuccess(ChatListResults results) {

    }

    @Override
    public void getChatListFail(String msg) {

    }

    @Override
    public int getChatUserId() {
        return mChatUserId;
    }

    @Override
    public void setNewChatListData(List<ChatListResults.Item> listData) {

    }

    @Override
    public void setHistoryChatListData(List<ChatListResults.Item> listData) {

    }

    @Override
    public void setDefChatListData(List<ChatListResults.Item> listData) {
        mChatAdapter.setData(listData);
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
        mListPresenter.getDefChatList();
    }
}
