package com.huanmedia.videochat.main2.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.widgets.ErrorView;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPFragment;
import com.huanmedia.videochat.common.SimpleLoadMoreView;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.CommDialogUtils;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.common.widget.dialog.ReportDialog;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.weight.OPtionPopWindows;
import com.huanmedia.videochat.repository.entity.ChatPeopleEntity;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import mvp.data.net.ApiException;
import mvp.data.store.glide.GlideUtils;
import mvp.view.LoadDataView;

public class AttentionFragment extends BaseMVPFragment<AttentionPresenter> implements AttentionView {

    @BindView(R.id.attention_fm_rv)
    RecyclerView mAttentionFmRv;
    @BindView(R.id.attention_fm_sv)
    SpringView mAttentionFmSv;
    private MainInteractionListener mListener;
    private BaseQuickAdapter<ChatPeopleEntity.ItemsEntity, BaseViewHolder> mAdapter;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private ErrorView mErrorView;
    private ListPopupWindow mPopWindow;

    public AttentionFragment() {
    }

    public static AttentionFragment newInstance() {
        AttentionFragment fragment = new AttentionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        if (mErrorView != null && !UserManager.getInstance().islogin()) {
//            app:ev_subtitleVisible="false"
//            app:ev_titleVisible="false"
//            app:ev_retryText="未登录，点击登录"
//            app:ev_retryColor="@color/base_black"
            mErrorView.setSubtitleVisible(false)
                    .setTitleVisible(false)
                    .setRetryVisible(true)
                    .setRetryText("未登录，点击登录")
                    .setRetryColor(ContextCompat.getColor(context(), R.color.base_black))
                    .setRetryListener(() -> {
                        UserManager.getInstance().outLogin(null);
                        UserManager.getInstance().exit();
                    });
            mAttentionFmSv.setEnable(false);
            mAdapter.setEnableLoadMore(false);
        } else {
            getBasePresenter().addDisposable(RxCountDown.delay2(500).subscribe(integer -> {
                mAttentionFmSv.callFresh();
            }));
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attention;
    }


    @SuppressLint("DefaultLocale")
    @Override
    protected void initView(View view) {
        super.initView(view);
        getBasePresenter().setTag(getString(R.string.attention_people));
        DefaultHeader header = new DefaultHeader(context(), R.drawable.defult_progress_small_white, R.drawable.arrow);
        mAttentionFmSv.setHeader(header);
        mAttentionFmSv.setGive(SpringView.Give.TOP);
        mAttentionFmSv.setType(SpringView.Type.FOLLOW);
        mAttentionFmSv.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                getBasePresenter().resetPage();
                getBasePresenter().loadMoreData(LoadDataView.LOADING_STATUS_REFRESH, 1);
            }

            @Override
            public void onLoadmore() {
            }
        });
        mAdapter = new BaseQuickAdapter<ChatPeopleEntity.ItemsEntity, BaseViewHolder>(R.layout.item_main_friend, null) {
            @Override
            protected void convert(BaseViewHolder helper, ChatPeopleEntity.ItemsEntity item) {
                RoundedImageView ivIcon = helper.getView(R.id.item_main_friend_iv_heard);
                ImageView ivMark = helper.getView(R.id.item_main_friend_iv_flag);

                int sexRes = item.getSex() == 1 ? R.drawable.icon_focus_boy :
                        R.drawable.icon_focus_girl;
                boolean isRadmain = item.getIsstarauth() == 1 && item.getStarbutton() == 1;
                boolean isGirl = item.getSex() == 2;
                if (isRadmain) {
                    ivMark.setVisibility(View.VISIBLE);
                    int attention = isGirl ? R.drawable.icon_pendant_girl :
                            R.drawable.icon_pendant_boy;
                    helper.setImageDrawable(R.id.item_main_friend_iv_flag,
                            ContextCompat.getDrawable(context(), attention));
                } else {
                    ivMark.setVisibility(View.GONE);
                }
                if (isGirl) {
                    ivIcon.setBorderColor(getContext().getResources().getColor(R.color.color_f65aa0));
                } else {
                    ivIcon.setBorderColor(getContext().getResources().getColor(R.color.color_1082ff));
                }
                helper.addOnClickListener(R.id.item_main_friend_iv_video);
                helper.setText(R.id.item_main_tv_time, TimeUtils.getFriendlyTimeSpanByNow(item.getLastuptime() * 1000L));
                helper.setText(R.id.item_main_tv_distance, Check.checkReplace(item.getDistance()));
                helper.setText(R.id.item_main_tv_name, Check.checkReplace(item.getNickname()));
                helper.setImageDrawable(R.id.item_main_friend_iv_sex,
                        ContextCompat.getDrawable(context(), sexRes));
                GlideUtils.getInstance().loadBitmapNoAnim(
                        AttentionFragment.this,
                        Check.checkReplace(item.getUserphoto_thumb()),
                        ivIcon);
            }
        };

        mAttentionFmRv.setAdapter(mAdapter);
        mErrorView = (ErrorView) getLayoutInflater().inflate(R.layout.base_list_empty, mAttentionFmRv, false);
        mErrorView.setSubtitle(getString(R.string.loading));
        mAdapter.setEmptyView(mErrorView);
        mAdapter.setEnableLoadMore(false);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setOnLoadMoreListener(() ->
                getBasePresenter().loadMoreData(LoadDataView.LOADING_STATUS_MORE, getBasePresenter().getPage().nextpage()), mAttentionFmRv);


        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            ChatPeopleEntity.ItemsEntity mCallingData = mAdapter.getData().get(position);
            getNavigator().navDiscoverInfo(getActivity(), mCallingData.getUid(), mCallingData.getDistance(), BusinessCardFragment.ShowType.ReadMan);
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mPopWindow == null) {
                    mPopWindow = OPtionPopWindows.getAttectionListOption(context());
                    mPopWindow.setHorizontalOffset(DisplayUtil.getDisplayWidth(context()) - DisplayUtil.dip2px(context(), 142));
                    mPopWindow.setWidth(DisplayUtil.dip2px(context(), 120));
                    mPopWindow.setModal(true);
                }
                ChatPeopleEntity.ItemsEntity itemData = mAdapter.getItem(position);
                mPopWindow.setOnItemClickListener((parent, view12, position1, id) -> {
                    switch (position1) {
                        case 0://取关
                            new MaterialDialog.Builder(context())
                                    .title("取消关注")
                                    .content("你确定要取消对Ta的关注？")
                                    .positiveText("确定")
                                    .positiveColorRes(R.color.base_black)
                                    .negativeColorRes(R.color.base_yellow)
                                    .negativeText("关闭")
                                    .onPositive((dialog, which) -> {
                                        assert itemData != null;
                                        getBasePresenter().favorite(itemData.getUid() + "");
                                    }).build().show();
                            break;
                        case 1://举报
                            new ReportDialog(context())
                                    .setReportClickLisitener((type, item) -> {
                                        getBasePresenter().reportothe(itemData.getUid() + "", type);
                                    })
                                    .show();
                            break;
                    }
                    mPopWindow.dismiss();
                });
                mPopWindow.setAnchorView(view);
                mPopWindow.show();
                return true;
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view12, position) -> {
            if (view12.getId() == R.id.item_main_friend_iv_video) {
                if (!UserManager.getInstance().islogin()) {
                    CommDialogUtils.showNavToLogin(getActivity(), (dialog, which) ->
                            UserManager.getInstance().outLogin(null));
                    UserManager.getInstance().exit();
                    return;
                }
                ChatPeopleEntity.ItemsEntity data = mAdapter.getItem(position);
                if ((UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() >= 20) ||//如果是视频直聊需要20钻石 固定
                        UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() >= data.getStarcoin()) {
                    int cost = data.getStarcoin() / (data.getStartime() == 0 ? 1 : data.getStartime());
                    GeneralDialog dialog = new GeneralDialog(getContext());
                    dialog
                            .setContent("视频聊天需花费" + String.format("%d钻石/分钟", cost))
                            .setCallBack(new GeneralDialog.CallBack() {
                                @Override
                                public void submitClick(Dialog dialog) {
                                    ConditionEntity condition = new ConditionEntity();
                                    condition.setVideoType(ConditionEntity.VideoType.REDMAN);
                                    condition.getReadMainConfig().setRedManId(data.getUid());
                                    condition.getReadMainConfig().setRequestType(ConditionEntity.RequestType.SELF);
                                    condition.getReadMainConfig().setRedMainStartCoin(data.getStarcoin());
                                    condition.getReadMainConfig().setReadMainStartTime(data.getStartime());
                                    getNavigator().navtoCalling(getActivity(), condition, "连接中...; ");
                                }

                                @Override
                                public void cancelClick(Dialog dialog) {

                                }
                            })
                            .show();
                } else {
                    CommDialogUtils.showInsufficientBalance(getActivity(), (dialog, which) -> getNavigator().navtoCoinPay(getActivity(), null));
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainInteractionListener) {
            mListener = (MainInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
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
        if (flag == ApiException.NULL) {
            mErrorView.setSubtitle("没有更多数据,下拉刷新试试");
            mAdapter.setNewData(null);
        } else {
            showHint(HintDialog.HintType.WARN, message);
        }
    }

    public void showHint(int type, String hint) {
        switch (type) {
            case HintDialog.HintType.WARN:
                if (mHintDialog == null) {
                    mHintDialog = new HintDialog(context(), HintDialog.HintType.WARN);
                    mHintDialog.showToast(HintDialog.LENGTH_SHORT);
                    mHintDialog.setTitleText(hint);
                } else if (!mHintDialog.isShowing()) {
                    mHintDialog.setTitleText(hint);
                    mHintDialog.showToast(HintDialog.LENGTH_SHORT);
                }
                break;
        }
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void showDatas(ChatPeopleEntity rdata) {
        if (rdata.getPage() == 1) {
            if (rdata.getItems().size() == 0) {
                mErrorView.setSubtitle("没有更多数据,下拉刷新试试");
            }
            mAttentionFmSv.onFinishFreshAndLoad();
            mAdapter.setNewData(rdata.getItems());
            mAdapter.setEnableLoadMore(rdata.getItems().size() >= rdata.getPagesize());
        } else if (rdata.getPage() > 1) {
            loadMore(rdata);
        }
    }

    /**
     * 加载更多数据
     *
     * @param cashListEntity
     */
    public void loadMore(ChatPeopleEntity cashListEntity) {

        if (cashListEntity.getItems() != null) {
            mAdapter.addData(cashListEntity.getItems());
        }
        if (cashListEntity.getPage() >= cashListEntity.getTotalpage()) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }


    /**
     * 加载更多出错
     */
    @Override
    public void loadMoreError() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void svComputeScroll() {
        if (getActivity() != null && mAttentionFmSv != null) {
            getActivity().runOnUiThread(() -> {
                mAttentionFmSv.onFinishFreshAndLoad();
            });
        }
    }
}
