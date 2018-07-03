package com.huanmedia.videochat.common.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.DevUtils;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.widget.dialog.adapter.GiftAdpater;
import com.huanmedia.videochat.mvp.entity.results.RewardResults;
import com.huanmedia.videochat.mvp.presenter.info.GiftListInfoPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.info.IGiftListInfoPresenter;
import com.huanmedia.videochat.mvp.presenter.info.IRewardPresenter;
import com.huanmedia.videochat.mvp.presenter.info.RewardPresenterImpl;
import com.huanmedia.videochat.mvp.view.info.IGiftListInfoView;
import com.huanmedia.videochat.mvp.view.info.IRewardView;
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.video.model.GiftCountMode;
import com.huanmedia.videochat.video.widget.GiftFragmentDialog;

import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class GiftDialog extends Dialog implements IGiftListInfoView, IRewardView, View.OnClickListener, GiftAdpater.CallBack {
    private TextView mTVTitle;
    private RecyclerView mListView;
    private TextView mDialogGiftBtnOk;
    private TextView mDialogGiftBtnCount;
    private TextView mDialogGiftTvCoin;
    private TextView mTVPayMore;
    private TextView mTVSubmit;

    private String mTitleStr;
    private IGiftListInfoPresenter mGiftListPresenter;
    private IRewardPresenter mRewardPresenter;
    private GiftAdpater mAdapter;

    private int mVideoId;
    private int mUid;


    public GiftDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
        mGiftListPresenter = new GiftListInfoPresenterImpl(this);
        mRewardPresenter = new RewardPresenterImpl(this);
        mTitleStr = "选个礼物打赏给TA吧~";
    }

    /**
     * 设置视频ID
     *
     * @param videoId
     */
    public void setVideoId(int videoId) {
        mVideoId = videoId;
    }

    /**
     * 设置用户ID
     *
     * @param uid
     */
    public void setUid(int uid) {
        mUid = uid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common_gift);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.BOTTOM;
            getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
        initData();
    }

    private void initView() {
        mListView = findViewById(R.id.recycler_view);
        mTVTitle = findViewById(R.id.tv_title);
        mDialogGiftBtnOk = findViewById(R.id.dialog_gift_btn_ok);
        mDialogGiftBtnCount = findViewById(R.id.dialog_gift_btn_count);
        mDialogGiftTvCoin = findViewById(R.id.dialog_gift_tv_coin);
        mTVPayMore = findViewById(R.id.dialog_gift_tv_pay);
        mTVSubmit = findViewById(R.id.dialog_gift_btn_ok);

        mDialogGiftBtnCount.setOnClickListener(this);
        mTVPayMore.setOnClickListener(this);
        mTVSubmit.setOnClickListener(this);

        //设置横向滑动
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mAdapter = new GiftAdpater(getContext());
        mAdapter.setCallBack(this);
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        mTVTitle.setText(mTitleStr);
        mGiftListPresenter.getGiftListInfo();
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitleStr = title;
    }

    @Override
    public void getGiftListSuccess(List<GiftEntity> listData) {
        mAdapter.setData(listData);
    }

    @Override
    public void getGiftListFail(String msg) {
        ToastUtils.showToastShortInCenter(getContext(), msg);
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }

    @Override
    public int getRewardUid() {
        return mUid;
    }

    @Override
    public int getRewardCoin() {
        return mAdapter.getSelectGift().getCoin();
    }

    @Override
    public int getRewardVideoId() {
        return mVideoId;
    }

    @Override
    public int getRewardGiftId() {
        return mAdapter.getSelectGift().getId();
    }

    @Override
    public int getRewardGiftNum() {
        return Integer.valueOf(mDialogGiftBtnCount.getText().toString());
    }

    @Override
    public void rewardSuccess(RewardResults results) {
        showToast("打赏成功");
        this.cancel();
    }

    @Override
    public void rewardFail(String msg) {
        showToast(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_gift_btn_ok:
                //确定
                sumbitGift();
                break;
            case R.id.dialog_gift_tv_pay:
                //充值
                payMoreMoney();
                break;
            case R.id.dialog_gift_btn_count:
                //展示数量选择
                showCountItem();
                break;
        }
    }

    /**
     * 充值
     */
    private void payMoreMoney() {
        Navigator navigator = new Navigator();
        navigator.navtoCoinPay(DevUtils.scanForActivity(getContext()), null);
    }

    /**
     * 提交礼物
     */
    private void sumbitGift() {
        mRewardPresenter.videoReward();
    }

    private ListPopupWindow mListPopupWindow;
    private List<GiftCountMode> data;
    private GiftFragmentDialog.PopAdapter mAopAdapter;
    private Disposable subscription;

    @SuppressLint("RestrictedApi")
    private void showCountItem() {
        if (data == null)
            data = Arrays.asList(
                    new GiftCountMode("一生一世", 1314),
                    new GiftCountMode("我爱你", 520),
                    new GiftCountMode("要抱抱", 188),
                    new GiftCountMode("长长久久", 99),
                    new GiftCountMode("一切顺利", 66),
                    new GiftCountMode("十全十美", 10),
                    new GiftCountMode("一心一意", 1)
            );
        if (mListPopupWindow == null) {
            mListPopupWindow = new ListPopupWindow(getContext());
            mListPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_list_item));
            mListPopupWindow.setWidth(mDialogGiftBtnOk.getWidth() + mDialogGiftBtnCount.getWidth());
            mListPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            mAopAdapter = new GiftFragmentDialog.PopAdapter(data, Integer.parseInt(mDialogGiftBtnCount.getText().toString()));
            mAopAdapter.setPopSelectListener((position, mode) -> {
                mDialogGiftBtnCount.setText(mode.getCount() + "");
                setCoin();
                mListPopupWindow.dismiss();
            });
            mListPopupWindow.setAdapter(mAopAdapter);
            mListPopupWindow.setDropDownAlwaysVisible(false);
            mListPopupWindow.setForceIgnoreOutsideTouch(false);
        } else {
            mAopAdapter.setPosDefault(Integer.parseInt(mDialogGiftBtnCount.getText().toString()));
        }

        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
            subscription = null;
        }
        subscription = RxCountDown.delay2(200).subscribe(
                integer -> {
                    if (mListPopupWindow != null && mListPopupWindow.getListView() != null && mListPopupWindow.getListView().getAdapter() != null) {
                        GiftFragmentDialog.PopAdapter adapter = (GiftFragmentDialog.PopAdapter) mListPopupWindow.getListView().getAdapter();
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        mListPopupWindow.setAnchorView(mDialogGiftBtnCount);
        mListPopupWindow.show();
        if (mListPopupWindow.getListView() != null) {
            mListPopupWindow.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mListPopupWindow.getListView().setCacheColorHint(Color.TRANSPARENT);
            mListPopupWindow.getListView().setDividerHeight(DisplayUtil.dip2px(getContext(), 1));
            mListPopupWindow.getListView().setDivider(ContextCompat.getDrawable(getContext(), R.drawable.divider_ll));
        }
    }


    private void setCoin() {
        if (mDialogGiftTvCoin != null) {
            if (mAdapter.getSelectGift() != null) {
                int totalPrice = Integer.valueOf(mDialogGiftBtnCount.getText().toString()) * mAdapter.getSelectGift().getCoin();
                mDialogGiftTvCoin.setText(Check.checkReplace(totalPrice + "", "0"));
            }
        }
    }

    @Override
    public void selectGift(GiftEntity giftEntity) {
        setCoin();
    }


}
