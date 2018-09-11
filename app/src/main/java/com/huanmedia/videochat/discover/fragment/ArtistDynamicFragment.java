package com.huanmedia.videochat.discover.fragment;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.discover.adapter.DynamicListAdapter;
import com.huanmedia.videochat.mvp.entity.results.DynamicListResults;
import com.huanmedia.videochat.mvp.presenter.user.DynamicListPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.user.IDynamicListPresenter;
import com.huanmedia.videochat.mvp.view.user.IDynamicListView;

import java.util.List;

import butterknife.BindView;

@SuppressLint("ValidFragment")
public class ArtistDynamicFragment extends BaseFragment implements IDynamicListView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private int mUserId;
    private IDynamicListPresenter mDynamicListPresenter;
    private DynamicListAdapter mListAdapter;
    private List<DynamicListResults.DynamicItem> mListData;

    @SuppressLint("ValidFragment")
    public ArtistDynamicFragment(int userId) {
        mUserId = userId;
    }

    public static ArtistDynamicFragment newInstance(int userId) {
        ArtistDynamicFragment fragment = new ArtistDynamicFragment(userId);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_artist_dynamic;
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (mListData != null)
            mListAdapter.setData(mListData);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mListAdapter = new DynamicListAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mListAdapter);
        mDynamicListPresenter = new DynamicListPresenterImpl(this);
        mDynamicListPresenter.getDynamicList(mUserId);
    }

    @Override
    public void getDynamicListSuccess(DynamicListResults results) {
        mListData = results.getList();
    }

    @Override
    public void getDynamicListFail(String msg) {

    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }
}
