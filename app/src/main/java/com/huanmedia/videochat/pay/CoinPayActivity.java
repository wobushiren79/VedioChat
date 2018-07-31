package com.huanmedia.videochat.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.utils.recycledecoration.RecyclerViewItemDecoration;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.entity.PayCoinTypeMode;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import mvp.data.store.glide.GlideUtils;

public class CoinPayActivity extends BaseMVPActivity<CoinPayPresenter> implements CoinPayView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coin_pay_tv_myCoin)
    TextView mCoinPayTvMyCoin;
    @BindView(R.id.coin_pay_tv_lable_normal)
    TextView mCoinPayTvLableNormal;
    @BindView(R.id.coin_pay_rv_normal)
    RecyclerView mCoinPayRvNormal;
    @BindView(R.id.coin_pay_iv_header)
    RoundedImageView mCoinPayIvHeader;
    @BindView(R.id.coin_pay_tv_nickName)
    TextView mCoinPayTvNickName;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private BaseQuickAdapter<PayCoinTypeMode, BaseViewHolder> mAdapter;

    public static Intent getCallingIntent(Context context, String coin) {
        Intent intent = new Intent(context, CoinPayActivity.class);
        if (coin != null)
            intent.putExtra("type", coin);
        return intent;
    }

    @Override
    protected void initData() {
        getBasePresenter().getRechargeInfo();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coin_pay;
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getBasePresenter().getRechargeInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        String currentCoin = getIntent().getStringExtra("type");
        if (currentCoin == null) {
            currentCoin = UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() + "";
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        RecyclerViewItemDecoration mCurrentItemDecoration = new RecyclerViewItemDecoration.Builder(context())
                .color(ContextCompat.getColor(context(), R.color.divide_gray))
                .thickness(DisplayUtil.dip2px(context(), 0.5f))
                .create();
        mCoinPayRvNormal.addItemDecoration(mCurrentItemDecoration);
        mCoinPayRvNormal.setLayoutManager(new LinearLayoutManager(this));
        mCoinPayRvNormal.setNestedScrollingEnabled(true);
        mCoinPayTvMyCoin.setText(currentCoin);
        mAdapter = new BaseQuickAdapter<PayCoinTypeMode, BaseViewHolder>(
                R.layout.item_coin_normal) {
            @Override
            protected void convert(BaseViewHolder helper, PayCoinTypeMode item) {
                ImageView ivRecharge = helper.getView(R.id.iv_recharge);
                TextView tvCoinTag = helper.getView(R.id.item_coin_tv_tag);
                Button btCoinPrice = helper.getView(R.id.item_coin_btn_price);
                if (item.getCoin() == 300||item.getCoin() == 3000) {
                    ivRecharge.setImageResource(R.drawable.icon_recharge_petals_style_2);
                    tvCoinTag.setBackgroundResource(R.drawable.coin_pay_bg_round_tag_yellow);
                    btCoinPrice.setBackgroundResource(R.drawable.coin_pay_item_select_yellow);
                } else {
                    ivRecharge.setImageResource(R.drawable.icon_recharge_petals);
                    tvCoinTag.setBackgroundResource(R.drawable.coin_pay_bg_round_tag_solid);
                    btCoinPrice.setBackgroundResource(R.drawable.coin_pay_item_select);
                }
                String unit = "钻石";
                Spanny spanny = new Spanny(Check.checkReplace(item.getName()));
                spanny.findAndSpan(unit, () -> new ForegroundColorSpan(ContextCompat.getColor(context(), R.color.base_gray)));
                spanny.findAndSpan(unit, () -> new AbsoluteSizeSpan(DisplayUtil.sp2px(context(), 12)));
                helper.setText(R.id.item_coin_tv_price, spanny)
                        .setText(R.id.item_coin_tv_sale_icon, "+" + item.getExtra())
                        .setText(R.id.item_coin_btn_price, item.getPrice() + "元")
                        .addOnClickListener(R.id.item_coin_btn_price);

                if (item.getExtra() == 0) {
                    helper.getView(R.id.item_coin_fl_sale).setVisibility(View.GONE);
                    helper.getView(R.id.item_coin_tv_tag).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.item_coin_fl_sale).setVisibility(View.VISIBLE);
                    helper.getView(R.id.item_coin_tv_tag).setVisibility(View.VISIBLE);
                }
            }
        };
        mCoinPayRvNormal.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.item_coin_btn_price:
                    getBasePresenter().createOlder((PayCoinTypeMode) adapter.getItem(position));
                    break;
            }
        });
        mCoinPayTvNickName.setText(UserManager.getInstance().getCurrentUser().getUserinfo().getNickname());
        GlideUtils.getInstance().loadBitmapNoAnim(this, UserManager.getInstance().getCurrentUser().getUserinfo().getUserphoto_thumb(), mCoinPayIvHeader);
    }

    @Override
    public Context context() {
        return this;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void showData(List<PayCoinTypeMode> coinPayEntity) {
        ((BaseQuickAdapter) mCoinPayRvNormal.getAdapter()).setNewData(coinPayEntity);
    }

    @Override
    public void createOlderSuccess(PayCoinTypeMode payCoinTypeMode, String olderNum) {
        getNavigator().navtoOlderPay(this, payCoinTypeMode, olderNum);
    }

    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mLoadingDialog.showDelay(1);
            mLoadingDialog.setTitleText(getString(R.string.loading));
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.showDelay(1);
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public void showHint(int type, String hint) {
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(this, HintDialog.HintType.WARN);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
            mHintDialog.setTitleText(hint);
        } else {
            if (mHintDialog.getType() != type) {
                mHintDialog.setType(type);
            }
            mHintDialog.setTitleText(hint);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
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
        if (!Check.isEmpty(message))
            showHint(HintDialog.HintType.WARN, message);
    }

}
