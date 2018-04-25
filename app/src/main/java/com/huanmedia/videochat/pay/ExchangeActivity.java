package com.huanmedia.videochat.pay;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.event.CoinChangeEvent;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.ClearEditText;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

public class ExchangeActivity extends BaseMVPActivity<ExchangePresenter> implements ExchangeView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.exchange_tv_canUse)
    TextView mExchangeTvCanUse;
    @BindView(R.id.exchange_tv_consume_all)
    TextView mExchangeTvConsumeAll;
    @BindView(R.id.exchange_btn_ok)
    Button mExchangeBtnOk;
    @BindView(R.id.exchange_et_money)
    ClearEditText mExchangeEtMoney;
    @BindView(R.id.exchange_tv_count_icon)
    TextView mExchangeTvCountIcon;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private Drawable mBtnDrawable;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ExchangeActivity.class);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange;
    }


    @Override
    protected void initView() {
        initToolbar();
        mExchangeEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int exchange = 0;
                if (s.length() > 0) {
                    exchange = Integer.parseInt(s.toString());
                }
                setExchangeCount(exchange);
            }
        });
        mExchangeTvConsumeAll.setOnClickListener(v -> {
            Double d = Double.parseDouble(mExchangeTvCanUse.getText().toString());
            BigDecimal b = new BigDecimal(d);
            d = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            mExchangeEtMoney.setText(d.intValue()*100 + "");
            mExchangeEtMoney.setSelection(mExchangeEtMoney.getText().length());
        });
    }

    private void setExchangeCount(int exchangeCoin) {
        if (exchangeCoin % 100 == 0) {
            int MCoin = UserManager.getInstance().getRealMoney(exchangeCoin).intValue();
            String str = String.format(getString(R.string.exchange_tv_consume), MCoin);
            Spanny spanny = new Spanny(str);
            spanny.findAndSpan(MCoin + "", () ->
                    new ForegroundColorSpan(ContextCompat.getColor(context(), R.color.base_blue)));
            mExchangeTvCountIcon.setText(spanny);
        }
    }

    @Override
    protected void initData() {
        setExchangeCount(0);
        setChangeCanUse();
    }

    private void setChangeCanUse() {
        mExchangeTvCanUse.setText(String.valueOf(UserManager.getInstance().getRealMoney(UserManager.getInstance().getCurrentUser().getUserinfo().getExchangecoin()).doubleValue()));
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    @OnClick({R.id.exchange_btn_ok})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.exchange_btn_ok:
                getBasePresenter().cashMoney(mExchangeEtMoney.getText().toString());
                break;
        }
    }

    @Override
    public Context context() {
        return this;
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAction(Intent action) {
        if (action == null || action.getAction() == null) return;
        switch (action.getAction()) {
            case EventBusAction.ACTION_COIN_CHANGED://金币更变
                mExchangeTvCanUse.setText(String.valueOf(UserManager.getInstance().getRealMoney(UserManager.getInstance().getCurrentUser().getUserinfo().getExchangecoin()).doubleValue()));
                break;
        }
    }

    @Override
    public void showHint(int type, String hint) {
        if (type == 1000) {
            setChangeCanUse();
        }
        switch (type) {
            case 4://兑换成功对话框确认
                CoinChangeEvent coinEvent = new CoinChangeEvent(CoinChangeEvent.CoinChangeType.EXCHANGE);
                EventBus.getDefault().post(coinEvent);
                new MaterialDialog.Builder(context())
                        .title("兑换成功")
                        .content(hint)
                        .positiveColorRes(R.color.base_yellow)
                        .positiveText("确定")
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        })
                        .show();
                break;
            case HintDialog.HintType.WARN:
                if (mHintDialog == null) {
                    mHintDialog = new HintDialog(context(), HintDialog.HintType.WARN);
                    mHintDialog.showToast(HintDialog.LENGTH_SHORT);
                    mHintDialog.setTitleText(hint);
                } else {
                    mHintDialog.setTitleText(hint);
                    mHintDialog.showToast(HintDialog.LENGTH_SHORT);
                }
                break;
        }
    }

}
