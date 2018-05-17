package com.huanmedia.videochat.main2.fragment;

import android.annotation.SuppressLint;
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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
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
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.common.widget.dialog.ReportDialog;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.weight.MaskDialog;
import com.huanmedia.videochat.main2.weight.MatchConfig;
import com.huanmedia.videochat.main2.weight.OPtionPopWindows;
import com.huanmedia.videochat.repository.entity.ChatPeopleEntity;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import mvp.data.net.ApiException;
import mvp.data.store.glide.GlideUtils;
import mvp.view.LoadDataView;

public class ComeAcrossFriendFragment extends BaseMVPFragment<ComeAcrossFriendPresenter> implements ComeAcroossView {

    @BindView(R.id.come_across_fm_rv)
    RecyclerView mComeAcrossFmRv;
    @BindView(R.id.come_across_fm_sv)
    SpringView mComeAcrossFmSv;
    private MainInteractionListener mListener;
    private BaseQuickAdapter<ChatPeopleEntity.ItemsEntity, BaseViewHolder> mAdapter;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private ErrorView mErrorView;
    private ListPopupWindow mPopWindow;

    public ComeAcrossFriendFragment() {
    }

    public static ComeAcrossFriendFragment newInstance() {
        ComeAcrossFriendFragment fragment = new ComeAcrossFriendFragment();
        return fragment;
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        if (mErrorView != null && !UserManager.getInstance().islogin()) {
            mErrorView.setSubtitleVisible(false)
                    .setTitleVisible(false)
                    .setRetryVisible(true)
                    .setRetryText("未登录，点击登录")
                    .setRetryColor(ContextCompat.getColor(context(), R.color.base_black))
                    .setRetryListener(() -> {
                        UserManager.getInstance().outLogin(null);
                        UserManager.getInstance().exit();
                    });
            mComeAcrossFmSv.setEnable(false);
            mAdapter.setEnableLoadMore(false);
        } else {
            getBasePresenter().addDisposable(RxCountDown.delay2(500).subscribe(integer -> {
                mComeAcrossFmSv.callFresh();
            }));
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void initView(View view) {
        getBasePresenter().setTag(getString(R.string.calling_people));
        DefaultHeader header = new DefaultHeader(context(), R.drawable.defult_progress_small_white, R.drawable.arrow);
        mComeAcrossFmSv.setHeader(header);
        mComeAcrossFmSv.setGive(SpringView.Give.TOP);
        mComeAcrossFmSv.setType(SpringView.Type.FOLLOW);
        mComeAcrossFmSv.setListener(new SpringView.OnFreshListener() {
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
                helper.setText(R.id.item_main_tv_time,
                        TimeUtils.getFriendlyTimeSpanByNow(item.getLastuptime() * 1000L))
                        .setText(R.id.item_main_tv_distance, Check.checkReplace(item.getDistance(), "未知"))
                        .setText(R.id.item_main_tv_name, Check.checkReplace(item.getNickname()))
                        .setImageDrawable(R.id.item_main_friend_iv_sex,
                                ContextCompat.getDrawable(context(), sexRes));
                GlideUtils.getInstance().loadBitmapNoAnim(
                        ComeAcrossFriendFragment.this,
                        Check.checkReplace(item.getUserphoto_thumb()),
                        ivIcon);

                helper.setOnClickListener(R.id.item_main_friend_iv_video, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!UserManager.getInstance().islogin()) {
                            CommDialogUtils.showNavToLogin(getActivity(), (dialog, which) ->
                                    UserManager.getInstance().outLogin(null));
                            UserManager.getInstance().exit();
                            return;
                        }
                        ChatPeopleEntity.ItemsEntity data = mAdapter.getItem(helper.getLayoutPosition());

                        if (UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() >= 200) {
                            new MaterialDialog.Builder(getActivity())
                                    .content("再次发起视频聊天需花费200钻")
                                    .negativeColorRes(R.color.base_gray)
                                    .negativeText("取消")
                                    .positiveText("确定")
                                    .positiveColorRes(R.color.base_yellow)
                                    .onPositive((dialog, which) -> {
                                        ConditionEntity condition = new ConditionEntity();
                                        condition.setVideoType(ConditionEntity.VideoType.MATCH);
                                        condition.getMatchConfig().setRequestType(ConditionEntity.RequestType.SELF);
                                        condition.getMatchConfig().setMask(MaskDialog.getCurrentMask());
                                        condition.getMatchConfig().setUid(data.getUid());
                                        condition.getMatchConfig().setMatchType(MatchConfig.MatchType.CALL);
                                        getNavigator().navtoCalling(getActivity(), condition, "连接中...; ");

                                    }).show();
                        } else {
                            CommDialogUtils.showInsufficientBalance(getActivity(), (dialog, which) -> getNavigator().navtoCoinPay(getActivity(), null));
                        }
                    }
                });
            }
        };

        mComeAcrossFmRv.setAdapter(mAdapter);
        mErrorView = (ErrorView) getLayoutInflater().inflate(R.layout.base_list_empty, mComeAcrossFmRv, false);
        mErrorView.setSubtitle(getString(R.string.loading));
        mAdapter.setEmptyView(mErrorView);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setOnLoadMoreListener(() -> {
            getBasePresenter().loadMoreData(LoadDataView.LOADING_STATUS_MORE, getBasePresenter().getPage().nextpage());
        }, mComeAcrossFmRv);

//
//        mComeAcrossFmRv.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ChatPeopleEntity.ItemsEntity mCallingData = mAdapter.getData().get(position);
//                getNavigator().navDiscoverInfo(getActivity(), mCallingData.getUid(),mCallingData.getDistance());
//            }
//        });
//        mComeAcrossFmRv.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ChatPeopleEntity.ItemsEntity mCallingData = mAdapter.getData().get(position);
//                getNavigator().navDiscoverInfo(getActivity(), mCallingData.getUid(), mCallingData.getDistance(), BusinessCardFragment.ShowType.Normal);
//            }
//        });
        mComeAcrossFmRv.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mPopWindow == null) {
                    mPopWindow = OPtionPopWindows.getComeAcrossListOption(context());
                    mPopWindow.setHorizontalOffset(DisplayUtil.getDisplayWidth(context()) - DisplayUtil.dip2px(context(), 142));
                    mPopWindow.setWidth(DisplayUtil.dip2px(context(), 120));
                    mPopWindow.setModal(true);
                }
                ChatPeopleEntity.ItemsEntity itemData = mAdapter.getItem(position);
                mPopWindow.setOnItemClickListener((parent, view12, position1, id) -> {
                    switch (position1) {
                        case 0://删除
                            new MaterialDialog.Builder(context())
                                    .title("提示")
                                    .content("确定删除？")
                                    .positiveText("确定")
                                    .positiveColorRes(R.color.base_black)
                                    .negativeColorRes(R.color.base_yellow)
                                    .negativeText("取消")
                                    .onPositive((dialog, which) ->
                                            getBasePresenter().removeComeAcrossFriend(itemData)).build().show();
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
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_come_across_friend;
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
            mComeAcrossFmSv.onFinishFreshAndLoad();
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
        if (getActivity() != null && mComeAcrossFmSv != null) {
            getActivity().runOnUiThread(() -> {
                mComeAcrossFmSv.onFinishFreshAndLoad();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
