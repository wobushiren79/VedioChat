package com.huanmedia.videochat.main2.fragment;

import android.view.View;

import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.common.widget.video.VideoPtrLayout;
import com.huanmedia.videochat.main2.MainActivity;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoListResults;
import com.huanmedia.videochat.mvp.presenter.video.IShortVideoListPresenter;
import com.huanmedia.videochat.mvp.presenter.video.ShortVideoListPresenterImpl;
import com.huanmedia.videochat.mvp.view.video.IShortVideoListView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class VideoListFragment extends BaseFragment implements PtrLayout.PtrHandleCallBack, IShortVideoListView, VideoPtrLayout.PageCallBack {
    @BindView(R.id.ptr_layout)
    VideoPtrLayout mPtrLayout;

    private IShortVideoListPresenter mShortVideoListPresenter;
    private int mForce;//是否强制刷新
    private boolean mIsRefresh;

    public VideoListFragment() {
    }


    public static VideoListFragment newInstance() {
        VideoListFragment fragment = new VideoListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_list;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mPtrLayout.setCallBack(this);
        mPtrLayout.setPageCallBack(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mShortVideoListPresenter = new ShortVideoListPresenterImpl(this);
        RxCountDown.delay2(500).subscribe(integer -> {
            mIsRefresh = true;
            mForce = 1;
            mPtrLayout.setRefresh();
        });
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        GSYVideoManager.onResume();
    }


    @Override
    protected void onInvisible() {
        super.onInvisible();
        GSYVideoManager.onPause();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((MainActivity) getActivity()).getCurrentFragment().equals(this)) {
            mPtrLayout.notifyDataSetChanged();
        }
//        GSYVideoManager.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onLoadMoreBegin(PtrFrameLayout frame) {
        mForce = 0;
        mIsRefresh = true;
        mShortVideoListPresenter.getShortVideoList();
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mForce = 1;
        mIsRefresh = true;
        GSYVideoManager.onPause();
        mPtrLayout.getRecyclerView().scrollToPosition(0);
        mPtrLayout.setPlayPosition(0);
        mShortVideoListPresenter.getShortVideoList();
    }

    @Override
    public void getShortVideoListSuccess(ShortVideoListResults data) {
        mIsRefresh = false;
    }

    @Override
    public void getShortVideoListFail(String msg) {
        mIsRefresh = false;
        showToast(msg);
    }

    @Override
    public PtrLayout getPtrLayoutForShortVideo() {
        return mPtrLayout;
    }

    @Override
    public int getPageForShortVideo() {
        return mPtrLayout.getPage();
    }

    @Override
    public int getPageSizeForShortVideo() {
        return mPtrLayout.getPageSize();
    }

    @Override
    public int getForceForShortVideo() {
        return mForce;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }


    @Override
    public void onPageSelected(int position, boolean isBottom) {

    }

    @Override
    public void almostBottom() {
        if (!mIsRefresh) {
            mForce = 0;
            mIsRefresh = true;
            mShortVideoListPresenter.getShortVideoList();
        }
    }

    public void refreshData() {
        if (!mIsRefresh) {
            mPtrLayout.setRefresh();
        }
    }
}
