package com.huanmedia.videochat.main2.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.utils.recycledecoration.RecyclerViewItemDecoration;
import com.huanmedia.ilibray.widgets.ErrorView;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPFragment;
import com.huanmedia.videochat.common.SimpleLoadMoreView;
import com.huanmedia.videochat.common.widget.ShufflingViewPager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.entity.DiscoverEntity;
import com.huanmedia.videochat.repository.entity.DiscoverPageEntity;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import butterknife.BindView;
import mvp.data.store.glide.GlideUtils;
import mvp.view.LoadDataView;

public class DiscoverFragment extends BaseMVPFragment<DiscoverPresenter> implements DiscoverView {
    @BindView(R.id.discover_fm_rv)
    RecyclerView mDiscoverFmRv;
    @BindView(R.id.discover_fm_sv)
    SpringView mDiscoverFmSv;


    private ShufflingViewPager mShufflingVP;

    private MainInteractionListener mListener;
    private ErrorView mEmptyView;
    private BaseQuickAdapter<DiscoverEntity, BaseViewHolder> mAdapter;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;

    public DiscoverFragment() {
    }

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isLazyLoad() {
        return true;
    }

    @Override
    protected void initData() {
        mDiscoverFmSv.callFresh();
        getBasePresenter().getPageBean().reset();
        getBasePresenter().getDiscoverData(LoadDataView.LOADING_STATUS_REFRESH);
    }

    @Override
    protected void initView(View view) {
        DefaultHeader header = new DefaultHeader(context(), R.drawable.defult_progress_small_white, R.drawable.arrow);
        mDiscoverFmSv.setHeader(header);
        mDiscoverFmSv.setGive(SpringView.Give.TOP);
        mDiscoverFmSv.setType(SpringView.Type.FOLLOW);
        mDiscoverFmSv.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                getBasePresenter().getPageBean().reset();
                getBasePresenter().getDiscoverData(LoadDataView.LOADING_STATUS_REFRESH);
            }

            @Override
            public void onLoadmore() {
            }
        });
        mAdapter = new BaseQuickAdapter<DiscoverEntity, BaseViewHolder>(R.layout.item_discover_grids, null) {
            @Override
            protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
                //item_discover_grids_iv_photo
                BaseViewHolder viewHolder = super.onCreateDefViewHolder(parent, viewType);
                GridLayoutManager lm = (GridLayoutManager) mDiscoverFmRv.getLayoutManager();
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)
                        viewHolder.getView(R.id.item_discover_grids_iv_photo).getLayoutParams();
                int width = DisplayUtil.getDisplayWidth(context()) / lm.getSpanCount();
                lp.height = (width / 5) * 6;
                return viewHolder;
            }

            @SuppressLint("DefaultLocale")
            @Override
            protected void convert(BaseViewHolder helper, DiscoverEntity item) {
                helper.setText(R.id.item_discover_grids_tv_name, item.getNickname())
                        .setText(R.id.item_discover_grids_tv_charge, String.format("%d钻/分钟", item.getStarcoin()))
                        .setText(R.id.item_discover_grids_tv_distance, Check.checkReplace(item.getDistance(), "未知"));

                if (item.getOnlinestatus() == 0) {
                    helper.setText(R.id.item_discover_grids_tv_status,
                            TimeUtils.getFriendlyTimeSpanByFrom(item.getLogintime() * 1000L
                                    , item.getSystemtime() * 1000L));
                } else if (item.getOnlinestatus() == 1) {
                    helper.setText(R.id.item_discover_grids_tv_status, "在线");
                } else if (item.getOnlinestatus() == 2) {
                    helper.setText(R.id.item_discover_grids_tv_status, "忙碌");
                }

                ImageView imageView = helper.getView(R.id.item_discover_grids_iv_status);
                imageView.getDrawable().setLevel(item.getOnlinestatus());
                String url = null;
                if (item.getPhotos() != null && item.getPhotos().size() > 0) {
                    url = item.getPhotos().get(0).getPhoto();
                }
                GlideUtils.getInstance().loadBitmapNoAnim(DiscoverFragment.this, Check.checkReplace(url, item.getUserphoto_thumb()), helper.getView(R.id.item_discover_grids_iv_photo));
            }
        };
        RecyclerViewItemDecoration mCurrentItemDecoration = new RecyclerViewItemDecoration.Builder(getContext())
                .color(ContextCompat.getColor(getContext(), R.color.windows_background))
                .thickness(DisplayUtil.dip2px(getContext(), 4))
                .gridTopVisible(true)
                .gridBottomVisible(true)
                .gridHorizontalSpacing(DisplayUtil.dip2px(getContext(), 4))
                .gridVerticalSpacing(DisplayUtil.dip2px(getContext(), 4))
                .create();

        mDiscoverFmRv.addItemDecoration(mCurrentItemDecoration);
        mEmptyView = (ErrorView) getLayoutInflater().inflate(R.layout.base_list_empty, mDiscoverFmRv, false);
        mDiscoverFmRv.setAdapter(mAdapter);

        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setOnLoadMoreListener(() -> {
            getBasePresenter().getDiscoverData(LoadDataView.LOADING_STATUS_MORE);
        }, mDiscoverFmRv);

        mDiscoverFmRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                DiscoverEntity mCallingData = mAdapter.getData().get(position);
                getNavigator().navDiscoverInfo(getActivity(), mCallingData.getUid(), mCallingData.getDistance());
            }
        });
        mShufflingVP = new ShufflingViewPager(getContext());
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.addHeaderView(mShufflingVP);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_discover;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainInteractionListener) {
            mListener = (MainInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        mShufflingVP.closeTimer();
        super.onDestroyView();
    }

    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mLoadingDialog.show();
            mLoadingDialog.setTitleText(getString(R.string.loading));
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(int flag, String message) {
        loadDataError(HintDialog.HintType.WARN, message);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void resultDiscoverData(int flag, DiscoverPageEntity rdata) {
        if (flag == LoadDataView.LOADING_STATUS_REFRESH) {
            if (rdata.getItems().size() == 0) {
                mEmptyView.setSubtitle("没有更多数据,下拉刷新试试");
            }
            mDiscoverFmSv.onFinishFreshAndLoad();
            mAdapter.setNewData(rdata.getItems());
            mAdapter.setEnableLoadMore(rdata.getItems().size() >= rdata.getPagesize());
        } else if (flag == LoadDataView.LOADING_STATUS_MORE) {
            loadMore(rdata);
        }
    }

    /**
     * 加载更多数据
     *
     * @param cashListEntity
     */
    public void loadMore(DiscoverPageEntity cashListEntity) {

        if (cashListEntity.getItems() != null) {
            mAdapter.addData(cashListEntity.getItems());
        }
        if (cashListEntity.getPage() >= cashListEntity.getTotalpage()) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void loadDataError(int type, String msg) {
        switch (type) {
            case HintDialog.HintType.WARN:
                if (mHintDialog == null) {
                    mHintDialog = new HintDialog(context(), HintDialog.HintType.WARN);
                    mHintDialog.showToast(HintDialog.LENGTH_SHORT);
                    mHintDialog.setTitleText(msg);
                } else if (!mHintDialog.isShowing()) {
                    mHintDialog.setTitleText(msg);
                    mHintDialog.showToast(HintDialog.LENGTH_SHORT);
                }
                break;
        }
    }

    @Override
    public void svComputeScroll() {
        if (getActivity() != null && mDiscoverFmSv != null) {
            getActivity().runOnUiThread(() -> mDiscoverFmSv.onFinishFreshAndLoad());
        }
    }
}
