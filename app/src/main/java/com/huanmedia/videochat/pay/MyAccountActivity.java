package com.huanmedia.videochat.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.huanmedia.ilibray.utils.TextViewDrawableUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.event.CoinChangeEvent;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.ClearEditText;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.entity.UserAccountBoundEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAccountActivity extends BaseMVPActivity<MyAccountPresenter> implements MyAccountView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.my_account_tv_canWithdraw)
    TextView mMyAccountTvCanWithdraw;
    @BindView(R.id.my_account_tv_bound)
    TextView mMyAccountTvBound;
    @BindView(R.id.my_account_cl_bound)
    ConstraintLayout mMyAccountClBound;
    @BindView(R.id.my_account_et_money)
    ClearEditText mMyAccountEtMoney;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MyAccountActivity.class);
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
        return R.layout.activity_my_account;
    }


    @Override
    protected void initView() {
        initToolbar();
    }

    @Override
    protected void initData() {
        getBasePresenter().getData();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void boundAccoundChange(Intent intent) {
        if (intent.getAction() == null) return;
        switch (intent.getAction()) {
            case EventBusAction.ACTION_BOUND_ACCOUND_CHANGE:
                getBasePresenter().getData();
                break;
                case EventBusAction.ACTION_COIN_CHANGED://金币更变
                    mMyAccountTvCanWithdraw.setText(String.valueOf(
                            UserManager.
                                    getInstance().
                                    getRealMoneyStr(
                                            UserManager.
                                            getInstance().
                                            getCurrentUser().
                                            getUserinfo().
                                            getExchangecoin()
                                    )));
                break;
        }
    }


    @OnClick({R.id.my_account_btn_ok, R.id.my_account_cl_bound})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.my_account_btn_ok:
                getBasePresenter().cashMoney(mMyAccountEtMoney.getText().toString());
                break;
            case R.id.my_account_cl_bound:
                if (getBasePresenter().getUserAccount() != null) {
                    getNavigator().navtoAccoundBound(MyAccountActivity.this,
                            getBasePresenter().getUserAccount().getIsBindPay() == 1
                            , getBasePresenter().getUserAccount().getBindType());
                }
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showHint(int type, String hint) {
        switch (type) {
            case 4://提现成功对话框确认
                EventBus.getDefault().post( new CoinChangeEvent(CoinChangeEvent.CoinChangeType.DRAWINGS));
                new MaterialDialog.Builder(context())
                        .title("提现成功")
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
                } else  {
                    mHintDialog.setTitleText(hint);
                    mHintDialog.showToast(HintDialog.LENGTH_SHORT);
                }
                break;
        }
    }

    @Override
    public void showData(UserAccountBoundEntity userAccountBoundEntity) {
        mMyAccountTvCanWithdraw.setText(String.valueOf(UserManager.getInstance().getRealMoneyStr(userAccountBoundEntity.getExchangecoin())));
        boolean isbound = userAccountBoundEntity.getIsBindPay() == 1;
        int boundType = userAccountBoundEntity.getBindType();
        int icon = boundType == 1 ? R.drawable.icon_wechat_mine : boundType == 2 ? R.drawable.icon_allpay_mine : 0;
        if (isbound && icon != 0) {
            TextViewDrawableUtils.setDrawable(mMyAccountTvBound, ContextCompat.getDrawable(context(), icon), null,
                    ContextCompat.getDrawable(context(), R.drawable.icon_arrows), null);
            mMyAccountTvBound.setText("");
        } else {
            TextViewDrawableUtils.setDrawable(mMyAccountTvBound, null, null, null, null);
            mMyAccountTvBound.setText("未绑定");
        }
    }
}
