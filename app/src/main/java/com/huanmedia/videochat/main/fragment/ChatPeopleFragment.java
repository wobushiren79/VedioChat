package com.huanmedia.videochat.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.widgets.ErrorView;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPFragment;
import com.huanmedia.videochat.repository.entity.ChatPeopleEntity;

import butterknife.BindView;
import mvp.data.store.glide.GlideUtils;

public class ChatPeopleFragment extends BaseMVPFragment<ChatPeoplePresenter> implements ChatPeopleView {
    @BindView(R.id.chat_pople_rv_talked)
    RecyclerView mChatPopleRvTalked;
    @BindView(R.id.errorView)
    ErrorView mErrorView;
    private ChatPeopleEntity mDatas;

    private OnChatPeopleFragmentListener mListener;
    private String mTag;

    public ChatPeopleFragment() {
        // Required empty public constructor
    }

    public static ChatPeopleFragment newInstance(String tag) {
        ChatPeopleFragment fragment = new ChatPeopleFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        if (mTag != null)
            getBasePresenter().getDatas(mTag);
    }

    @Override
    protected void initView(View view) {
        mErrorView.setRetryListener(() -> {
            if (mTag != null) {
                getBasePresenter().getDatas(mTag);
            }
        });
        mTag = getArguments().getString("tag");
        getBasePresenter().setTag(mTag);
        mChatPopleRvTalked.setAdapter(new BaseQuickAdapter<ChatPeopleEntity.ItemsEntity, BaseViewHolder>(R.layout.item_drawer_talked, mDatas == null ? null : mDatas.getItems()) {
            @Override
            protected void convert(BaseViewHolder helper, ChatPeopleEntity.ItemsEntity item) {
                helper.setText(R.id.item_chat_people_fm_toked_tv_name, Check.checkReplace(item.getNickname()));
                GlideUtils.getInstance().loadBitmapNoAnim(ChatPeopleFragment.this, Check.checkReplace(item.getUserphoto_thumb()), helper.getView(R.id.item_chat_people_fm_toked_iv_header));
                if (item.getSex() != 0)
                    helper.setImageResource(R.id.item_chat_people_fm_toked_iv_sex, item.getSex() == 1 ? R.drawable.icon_gender_man : R.drawable.icon_gender_woman);
            }
        });
        mChatPopleRvTalked.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                onButtonPressed((ChatPeopleEntity.ItemsEntity) adapter.getData().get(position));
            }
        });
     mChatPopleRvTalked.addOnScrollListener(new RecyclerView.OnScrollListener() {
         @Override
         public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

             RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//判断是当前layoutManager是否为LinearLayoutManager
//只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
             if (layoutManager instanceof GridLayoutManager) {
                 GridLayoutManager linearManager = (GridLayoutManager) layoutManager;
                 //获取最后一个可见view的位置
                 int lastItemPosition = linearManager.findLastVisibleItemPosition();
                 if (lastItemPosition<0)return;
                 //获取第一个可见view的位置
                 int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                 if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItemPosition + 1 == recyclerView.getAdapter().getItemCount()) {
                    if (mListener!=null){
                        mListener.drawerCanClose(false);
                    }
                 }
                 //同理检测是否为顶部itemView时,只需要判断其位置是否为0即可
                 if (newState == RecyclerView.SCROLL_STATE_IDLE && firstItemPosition == 0) {
                     if (mListener!=null){
                         mListener.drawerCanClose(true);
                     }
                 }

             }
         }
         @Override
         public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

         }
     });
    }

    public void onButtonPressed(ChatPeopleEntity.ItemsEntity chatPeopleEntity) {
        if (mListener != null) {
            mListener.onFragmentInteraction(chatPeopleEntity);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatPeopleFragmentListener) {
            mListener = (OnChatPeopleFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChatPeopleFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_people;
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(int flag, String message) {
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setSubtitle(message);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showDatas(ChatPeopleEntity chatPeopleEntity) {
        if (chatPeopleEntity.getItems()==null || chatPeopleEntity.getItems().size()==0){
            showError(0, "你还没有发起过聊天！");
            ((BaseQuickAdapter) mChatPopleRvTalked.getAdapter()).getData().clear();
            ((BaseQuickAdapter) mChatPopleRvTalked.getAdapter()).notifyDataSetChanged();
        }else {
            mErrorView.setVisibility(View.GONE);
            if (chatPeopleEntity.getItems() != null) {
                this.mDatas = chatPeopleEntity;
            }
            ((BaseQuickAdapter) mChatPopleRvTalked.getAdapter()).setNewData(chatPeopleEntity.getItems());
        }
    }

    public interface OnChatPeopleFragmentListener {
        void onFragmentInteraction(ChatPeopleEntity.ItemsEntity entity);
        void drawerCanClose(boolean isCanClose);
    }
}
