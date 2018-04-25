package com.huanmedia.videochat.pay;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.TextViewDrawableUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.utils.recycledecoration.RecyclerViewItemDecoration;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.PageBean;
import com.huanmedia.videochat.common.SimpleLoadMoreView;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.my.OnCashFragmentListener;
import com.huanmedia.videochat.pay.mode.MyWalletMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindView;

import static com.huanmedia.videochat.pay.MyWalletFragment.WallPageType.COIN;
import static com.huanmedia.videochat.pay.MyWalletFragment.WallPageType.MCION;

public class MyWalletFragment extends BaseFragment {
    @BindView(R.id.my_wallet_tv_money)
    TextView mMyWalletTvMoney;
    @BindView(R.id.my_wallet_tv_unit)
    TextView mMyWalletTvUnit;
    @BindView(R.id.my_wallet_tv_exchange)
    TextView mMyWalletTvExchange;
    @BindView(R.id.my_wallet_tv_withdraw)
    TextView mMyWalletTvWithdraw;
    @BindView(R.id.my_wallet_tv_particulars_title)
    TextView mMyWalletTvParticularsTitle;
    @BindView(R.id.my_wallet_rv)
    RecyclerView mMyWalletRv;
    private int mType;
    private BaseSectionQuickAdapter<MyWalletMode, BaseViewHolder> mAdapter;
    private OnCashFragmentListener mListener;
    private PageBean mPage = new PageBean();
    private String mDateTime;


    @IntDef({MCION, COIN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WallPageType {
        int MCION = 1, COIN = 0;//m币 和 钻石 类型
    }

    public MyWalletFragment() {
    }

    public static MyWalletFragment newInstance(@WallPageType int type) {
        MyWalletFragment fragment = new MyWalletFragment();
        Bundle bundles = new Bundle();
        bundles.putInt("type", type);
        fragment.setArguments(bundles);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAction(Intent action) {
        if (action == null || action.getAction() == null) return;
        switch (action.getAction()) {
            case EventBusAction.ACTION_COIN_CHANGED://金币更变
                initData();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCashFragmentListener) {
            mListener = (OnCashFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCashFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_wallet;
    }


    @Override
    protected void initData() {
        upCoin();
        mPage.reset();
        loadData(mPage.nextpage(), mDateTime);
    }

    /**
     * 更新钻石数量或者M币数量
     */
    private void upCoin() {
        switch (mType) {
            case COIN:
                mMyWalletTvMoney.setText(String.valueOf(UserManager.getInstance().getCurrentUser().getUserinfo().getCoin()));
                break;
            case MCION:
                mMyWalletTvMoney.setText(String.valueOf(UserManager.getInstance().getRealMoneyStr(UserManager.getInstance().getCurrentUser().getUserinfo().getExchangecoin())));
                break;
        }
    }

    @Override
    protected void initView(View view) {
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
        }
        switch (mType) {
            case COIN://钻石
                TextViewDrawableUtils.setDrawable(mMyWalletTvUnit, null, null, ContextCompat.getDrawable(getContext(), R.drawable.tab_icon_jewel), null);
                mMyWalletTvExchange.setVisibility(View.GONE);
                mMyWalletTvWithdraw.setText("充值");
                mMyWalletTvWithdraw.setOnClickListener(v -> getNavigator().navtoCoinPay(getActivity(), null));
                break;
            case MCION://M币
                mMyWalletTvUnit.setText("M币");
                mMyWalletTvExchange.setText("兑换");
                mMyWalletTvExchange.setOnClickListener(v -> getNavigator().navtoExchange(getActivity()));
                mMyWalletTvWithdraw.setText("提现");
                mMyWalletTvWithdraw.setOnClickListener(v -> {
                    getNavigator().navtoMyAccound(getActivity());
                });
                break;
        }
        mAdapter = new BaseSectionQuickAdapter<MyWalletMode, BaseViewHolder>(R.layout.item_my_wallet, R.layout.item_my_wallet_header, null) {
            @Override
            protected void convertHead(BaseViewHolder helper, MyWalletMode item) {
                helper.setText(R.id.item_my_wallet_tv_time, item.header);
            }

            @Override
            protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = super.onCreateDefViewHolder(parent, viewType);
                if (viewType != SECTION_HEADER_VIEW && mType == COIN) {
                    Drawable drawableJewel = ContextCompat.getDrawable(parent.getContext(), R.drawable.tab_icon_jewel);
                    TextView tv = holder.getView(R.id.item_my_wallet_tv_price);
                    tv.setCompoundDrawablePadding(DisplayUtil.dip2px(parent.getContext(), 4));
                    TextViewDrawableUtils.setDrawable(tv,
                            null, null, drawableJewel, null);
                }
                return holder;
            }

            @Override
            protected void convert(BaseViewHolder helper, MyWalletMode item) {
                String price = "";
                if (mType == COIN) {
                    price = item.t.getCoin() + "";
                } else {
                    price = UserManager.getInstance().getRealMoney(item.t.getCoin()) + "M币";
                }
                helper.setText(R.id.item_my_wallet_tv_desc, Check.checkReplace(item.t.getDesc()))
                        .setText(R.id.item_my_wallet_tv_price, price);
            }
        };
        //分割线
        RecyclerViewItemDecoration mCurrentItemDecoration = new RecyclerViewItemDecoration.Builder(getContext())
                .color(ContextCompat.getColor(getContext(), R.color.divide_gray))
                .thickness(DisplayUtil.dip2px(getContext(), 0.5f))
                .create();
        mMyWalletRv.addItemDecoration(mCurrentItemDecoration);
        mMyWalletRv.setAdapter(mAdapter);
        mAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.base_list_empty, mMyWalletRv, false));
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setOnLoadMoreListener(() -> {
            loadData(mPage.nextpage(), mDateTime);
        }, mMyWalletRv);
    }


    public void loadData(int page, String data) {
        if (mListener != null) {
            mListener.getDataForType(mType, page, data);
        }
    }

    /**
     * 添加新数据
     *
     * @param walletMode
     */
    public void addData(MyWalletMode walletMode) {
        mAdapter.addData(walletMode);
    }

    /**
     * 设置新数据
     *
     * @param walletModes
     */
    public void setNewData(List<MyWalletMode> walletModes, boolean enableLoadMore) {
        mAdapter.setNewData(walletModes);
        mAdapter.setEnableLoadMore(enableLoadMore);

    }


    /**
     * 加载更多数据
     *
     * @param walletModes
     * @param isEnd       是否还有更多
     */
    public void loadMore(List<MyWalletMode> walletModes, boolean isEnd) {
        if (walletModes != null) {
            mAdapter.addData(walletModes);
        }
        if (isEnd) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
            mAdapter.setOnLoadMoreListener(() -> {
                loadData(mPage.nextpage(), mDateTime);
            }, mMyWalletRv);
        }

    }

    public void setDate(String date) {
        mDateTime = date;
        mPage.reset();
        loadData(mPage.nextpage(), mDateTime);
    }

    /**
     * 加载更多出错
     */
    public void loadMoreError() {
        mAdapter.loadMoreFail();
        if (mPage != null)
            mPage.setCurrentPage(mPage.getCurrentPage() - 1);
    }


}
