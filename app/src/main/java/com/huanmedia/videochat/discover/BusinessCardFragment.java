package com.huanmedia.videochat.discover;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.widgets.ErrorView;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.BaseMVPFragment;
import com.huanmedia.videochat.common.SimpleLoadMoreView;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.utils.UMengUtils;
import com.huanmedia.videochat.common.utils.VideoChatUtils;
import com.huanmedia.videochat.common.widget.dialog.CommDialogUtils;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.discover.adapter.BusinessCardAdapter;
import com.huanmedia.videochat.discover.adapter.BusinessMultiItem;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.weight.MaskDialog;
import com.huanmedia.videochat.repository.entity.BusinessCardEntity;
import com.huanmedia.videochat.repository.entity.UserEvaluateEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人名片
 */
public class BusinessCardFragment extends BaseMVPFragment<BusinessCardPresenter> implements BusinessCardView {
    private static final String ARG_DATA = "mUid";
    private static final String ARG_DISTANCE = "distance";
    private static final String ARG_SHOWTYPE = "showtype";
    public static String TAG = "/BusinessCardFragment";

    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowType {
        //红人            //普通
        int ReadMan = 1, Normal = 2;
    }

    @BindView(R.id.business_card_rv)
    RecyclerView mBusinessCardRv;
    @BindView(R.id.ll_bt_layout)
    LinearLayout mBTLayout;
    @BindView(R.id.business_card_iv_calling)
    ImageView mBusinessCardIvCalling;
    @BindView(R.id.business_card_iv_appointment_video)
    ImageView mBusinessCardIvAppointmentVideo;
    @BindView(R.id.business_card_iv_appointment)
    ImageView mBusinessCardIvAppointment;
    private BusinessCardAdapter mAdapter;
    private int mUid;

    private @ShowType
    int showType;

    private BusinessCardEntity mData;
    private String distance;
    private HintDialog mHintDialog;
    private SimpleLoadMoreView mLoadmoreView;
    private ErrorView mErrorView;
    private HintDialog mLoadingDialog;


    public BusinessCardFragment() {
        // Required empty public constructor
    }

    public static BusinessCardFragment newInstance(int uid, String distance, @ShowType int showType) {
        BusinessCardFragment fragment = new BusinessCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DATA, uid);
        args.putInt(ARG_SHOWTYPE, showType);
        if (!Check.isEmpty(distance))
            args.putString(ARG_DISTANCE, distance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUid = getArguments().getInt(ARG_DATA);
            distance = getArguments().getString(ARG_DISTANCE, null);
            showType = getArguments().getInt(ARG_SHOWTYPE);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_business_ard;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void initData() {
        if (mUid == 0) return;
        getBasePresenter().getBusinessCard(mUid);

    }

    @Override
    protected void initView(View view) {

        BaseActivity activity = (BaseActivity) getActivity();
        activity.getSupportActionBar().setTitle("个人名片");

        mBusinessCardRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mAdapter = new BusinessCardAdapter(getContext(), null, distance, showType);
        mAdapter.bindToRecyclerView(mBusinessCardRv);
        mAdapter.disableLoadMoreIfNotFullPage();
        mErrorView = (ErrorView) getLayoutInflater().inflate(R.layout.base_list_empty, mBusinessCardRv, false);
        mErrorView.setSubtitle(getString(R.string.loading));
        mAdapter.setEmptyView(mErrorView);
        mLoadmoreView = new SimpleLoadMoreView();
        mLoadmoreView.setShowLoadMoreEnd(false);
        mLoadmoreView.setLoadMoreEndGone(true);
        mAdapter.setLoadMoreView(mLoadmoreView);
        mAdapter.enableLoadMoreEndClick(true);//加载失败点事件
        mAdapter.setBusinessAdapterListener((businessCard, checked) -> {
            getBasePresenter().favorite(mUid + "", checked ? 1 : 0);
        });
        mAdapter.setOnLoadMoreListener(() ->
                getBasePresenter().loadMoreData(mUid + ""), mBusinessCardRv);
    }
    //-------------------------------------------------头部数据填充----------------------------

    @SuppressLint("DefaultLocale")
    @Override
    public void showHeadData(BusinessCardEntity businessCard) {
        if (businessCard.getBase() == null) return;
        if (mUid == UserManager.getInstance().getId()) {
            mBTLayout.setVisibility(View.GONE);
        } else {
            mBTLayout.setVisibility(View.VISIBLE);
            if (businessCard.getExt() != null
                    && businessCard.getExt().getAppoint() != null
                    && businessCard.getExt().getAppoint().getHasappoint() == 1
                    && businessCard.getExt().getAppoint().getComplaintflag() == 0) {
                mBusinessCardIvAppointment.setVisibility(View.GONE);
                mBusinessCardIvAppointmentVideo.setVisibility(View.VISIBLE);
            } else {
                mBusinessCardIvAppointment.setVisibility(View.VISIBLE);
                mBusinessCardIvAppointmentVideo.setVisibility(View.GONE);
            }
        }
        if (businessCard.getBase().getAppointmentFlag() == 0) {
            mBusinessCardIvAppointment.setVisibility(View.GONE);
        }
        this.mData = businessCard;
        List<BusinessMultiItem> data = new ArrayList<>();
        data.add(businessCard.getBase());
        data.add(businessCard.getTags());
        if (businessCard.getEvaluates() == null || businessCard.getEvaluates().getItems() == null
                || businessCard.getEvaluates().getItems().size() == 0) {
            mAdapter.loadMoreEnd();
            mAdapter.setEnableLoadMore(false);
        } else {
            data.addAll(businessCard.getEvaluates().getItems());
            mAdapter.setEnableLoadMore(businessCard.getEvaluates().getItems().size() >= businessCard.getEvaluates().getPagesize());
        }

        mAdapter.setNewData(data);
    }

    @Override
    public void showMore(UserEvaluateEntity userEvaluateEntity) {

        if (userEvaluateEntity.getItems() != null) {
            mAdapter.addData(userEvaluateEntity.getItems());
        }
        if (userEvaluateEntity.getPage() >= userEvaluateEntity.getTotalpage()) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void loadMoreFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void resultFavoriteSuccess(int flag) {
        switch (flag) {
            case 1://关注
                showError(HintDialog.HintType.WARN, "关注成功");
                break;
            case 0://取消关注
                showError(HintDialog.HintType.WARN, "已取消关注");
                break;
        }
    }

    //---------------------------------------------------头部数据初始化完成----------------------------------------
    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @SuppressLint("DefaultLocale")
    @OnClick({R.id.business_card_iv_calling, R.id.business_card_iv_appointment, R.id.business_card_iv_appointment_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.business_card_iv_appointment:
                UMengUtils.AppointmentIn(getContext(), mUid);
                getNavigator().navtoAppointment((Activity) getContext(), mUid);
                break;
            case R.id.business_card_iv_appointment_video:
                UMengUtils.AppointmentVideo(getContext(), mUid, 0);
                if (mData != null && mData.getExt() != null && mData.getExt().getAppoint() != null)
                    VideoChatUtils.StartAppointmentCall(
                            (BaseActivity) getActivity(),
                            (int) UserManager.getInstance().getId(),
                            mUid,
                            mData.getExt().getAppoint().getAppointid(),
                            mUid,
                            (int) UserManager.getInstance().getId()
                    );
                break;
            case R.id.business_card_iv_calling:
                if (!UserManager.getInstance().islogin()) {
                    CommDialogUtils.showNavToLogin(getActivity(), (dialog, which) ->
                            UserManager.getInstance().outLogin(null));
                    UserManager.getInstance().exit();
                    return;
                }
                boolean isReadMain =
                        mData.getBase().getStarbutton() == 1
                                && mData.getBase().getIsstarauth() == 1;
                VideoChatUtils.CheckCallVideo(
                        getActivity(),
                        getNavigator(),
                        isReadMain,
                        mData.getBase().getStarcoin(),
                        mData.getBase().getStartime(),
                        mData.getBase().getOnlinestatus(),
                        mData.getBase().getUid());
                break;
        }
    }

    @Override
    public Context context() {
        return getContext();
    }

    public void onBackPressed() {
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
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showRetry() {
        mErrorView.setRetryVisible(true);
        mErrorView.setRetryListener(() -> {
            mErrorView.setRetryVisible(false);
            getBasePresenter().getBusinessCard(mUid);
        });
    }

    @Override
    public void hideRetry() {
        mErrorView.setRetryVisible(false);
    }

    @Override
    public void showError(int flag, String message) {
        if (flag == 0) {
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setSubtitle(message);
        } else {
            showHint(flag, message);
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

}
